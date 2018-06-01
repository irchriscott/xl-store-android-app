package com.xlstore.irchristianscott.xlstoreapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.xlstore.irchristianscott.xlstoreapp.models.Mentions
import com.xlstore.irchristianscott.xlstoreapp.models.ProductModel
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : AppCompatActivity() {

    private lateinit var commentEditText: EditText
    private lateinit var submitComment: ImageView

    @SuppressLint("SetTextI18n", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val jsonGSON = Gson()
        val userDataManager = UserDataManager()
        val userData = userDataManager.getLoggedInUserObject(this@ProductActivity)
        val productOperations = ProductOperations()

        val productString = intent.getStringExtra(ProductsViewHolder.PRODUCT_OBJECT)
        val product: ProductModel = jsonGSON.fromJson(productString, ProductModel::class.java)

        supportActionBar!!.title = product.name
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        productName.text = product.name
        productPrice.text = product.getProductPrice()
        companyName.text = product.company.name
        productCategory.text = product.category.name
        productInteress.text = product.interess.toString()
        productComments.text = product.comments.toString()
        productLikes.text = product.likes.toString()
        productDislikes.text = product.dislikes.toString()

        when(product.comments){
            1 -> {
                commentNumbers.text = "${product.comments.toString()} Comment"
            }
            else -> {
                commentNumbers.text = "${product.comments.toString()} Comments"
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            productDescription.text = Html.fromHtml(product.description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            productDescription.text = Html.fromHtml(product.description)
        }

        Picasso.get().load(product.getProductImage()).into(productImage)
        Picasso.get().load(product.company.getCompanyProfileImage()).into(companyImage)
        Picasso.get().load(userData!!.getUserProfileImage()).into(profileImage)

        val isInterested = userDataManager.checkUserInterestProduct(this@ProductActivity, product.user_interests)
        val hasMentioned = userDataManager.checkUserMentionProduct(this@ProductActivity, mentions = product.mentions)

        if (isInterested && product.interess > 0) productInteress.setTextColor(Color.GREEN)

        if(hasMentioned != null && product.likes + product.dislikes > 0){
            if (hasMentioned == "like"){
                productLikes.setTextColor(Color.RED)
            } else if (hasMentioned == "dislike"){
                productDislikes.setTextColor(Color.RED)
            }
        }

        val mentions: List<Mentions> = product.mentions

        productInteress.setOnClickListener {
            productOperations.interestProduct(window.currentFocus, product)
            if(!isInterested){
                findViewById<TextView>(R.id.productInteress).text = "${product.interess + 1}"
                findViewById<TextView>(R.id.productInteress).setTextColor(Color.GREEN)
            }
        }

        productLikes.setOnClickListener {
            productOperations.mentionProduct(window.currentFocus, product, "like")

            if(mentions.count() > 0 || mentions.isNotEmpty()){

                if(hasMentioned != null && product.likes + product.dislikes > 0){
                    if (hasMentioned == "dislike"){
                        findViewById<TextView>(R.id.productDislikes).text = "${product.dislikes - 1}"
                        findViewById<TextView>(R.id.productDislikes).setTextColor(Color.parseColor("#666666"))
                        findViewById<TextView>(R.id.productLikes).text = "${product.likes + 1}"
                        findViewById<TextView>(R.id.productLikes).setTextColor(Color.RED)
                        ProductOperations.updateUserMention(this, "like", mentions, product)
                    }
                } else {
                    findViewById<TextView>(R.id.productLikes).text = "${product.likes + 1}"
                    findViewById<TextView>(R.id.productLikes).setTextColor(Color.RED)
                    ProductOperations.addUserMention(productOperations, this, "like", mentions, product)
                }
            } else {
                findViewById<TextView>(R.id.productLikes).text = "${product.likes + 1}"
                findViewById<TextView>(R.id.productLikes).setTextColor(Color.RED)
                ProductOperations.addUserMention(productOperations, this, "like", mentions, product)
            }
        }

        productDislikes.setOnClickListener {
            productOperations.mentionProduct(window.currentFocus, product, "dislike")

            if(mentions.count() > 0 || mentions.isNotEmpty()){

                if(hasMentioned != null && product.likes + product.dislikes > 0){
                    if (hasMentioned == "like"){
                        findViewById<TextView>(R.id.productLikes).text = "${product.likes - 1}"
                        findViewById<TextView>(R.id.productLikes).setTextColor(Color.parseColor("#666666"))
                        findViewById<TextView>(R.id.productDislikes).text = "${product.dislikes + 1}"
                        findViewById<TextView>(R.id.productDislikes).setTextColor(Color.RED)
                        ProductOperations.addUserMention(productOperations,this, "dislike", mentions, product)
                    }
                } else {
                    findViewById<TextView>(R.id.productDislikes).text = "${product.dislikes + 1}"
                    findViewById<TextView>(R.id.productDislikes).setTextColor(Color.RED)
                    ProductOperations.addUserMention(productOperations, this, "dislike", mentions, product)
                }
            } else {
                findViewById<TextView>(R.id.productDislikes).text = "${product.dislikes + 1}"
                findViewById<TextView>(R.id.productDislikes).setTextColor(Color.RED)
                ProductOperations.addUserMention(productOperations, this, "like", mentions, product)
            }
        }

        val bundle: Bundle = Bundle()
        bundle.putInt("PRODUCT_ID", product.id)
        val commentFragment = CommentsFragment()

        commentFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.commentsFragment, commentFragment).addToBackStack(null).commit()

        commentEditText = findViewById<EditText>(R.id.commentEditText) as EditText
        submitComment = findViewById<ImageView>(R.id.submitComment) as ImageView

        commentEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(text: Editable?) {
                if(text.isNullOrEmpty() || text.isNullOrEmpty()){
                    submitComment.drawable.setTint(Color.parseColor("#666666"))
                    submitComment.isClickable = false
                } else {
                    submitComment.drawable.setTint(Color.parseColor("#00C6D7"))
                    submitComment.isClickable = true
                }
            }
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
