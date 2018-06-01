package com.xlstore.irchristianscott.xlstoreapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.xlstore.irchristianscott.xlstoreapp.models.MAIN_API_URL
import com.xlstore.irchristianscott.xlstoreapp.models.ProductModel
import com.xlstore.irchristianscott.xlstoreapp.models.ResponseMessage
import com.xlstore.irchristianscott.xlstoreapp.models.UserInterests
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import ru.whalemare.sheetmenu.SheetMenu
import java.io.IOException
import java.util.*


class ProductsViewHolder(val view: View, var product: ProductModel? = null): RecyclerView.ViewHolder(view){

    companion object {

        const val PRODUCT_OBJECT = "PRODUCT_OBJECT"

    }

    private fun navigateToSingleProduct(){

        val intent = Intent(view.context, ProductActivity::class.java)

        val jsonGSON = Gson()
        val productOBJECT = jsonGSON.toJson(product)
        intent.putExtra(PRODUCT_OBJECT, productOBJECT)

        view.context.startActivity(intent)
    }

    private val productOperations = ProductOperations()

    init {

        view.findViewById<ImageView>(R.id.productImage).setOnClickListener {
            navigateToSingleProduct()
        }

        view.findViewById<TextView>(R.id.productName).setOnClickListener {
            navigateToSingleProduct()
        }

        view.findViewById<TextView>(R.id.productComments).setOnClickListener {
            navigateToSingleProduct()
        }

        view.findViewById<TextView>(R.id.productInteress).setOnClickListener {
            productOperations.interestProduct(view, product)
        }

        view.findViewById<TextView>(R.id.productLikes).setOnClickListener {
            productOperations.mentionProduct(view, product, "like")
        }

        view.findViewById<TextView>(R.id.productDislikes).setOnClickListener {
            productOperations.mentionProduct(view, product, "dislike")
        }

        view.findViewById<TextView>(R.id.companyName).setOnClickListener {
            Toast.makeText(view.context, "Navigated to company", Toast.LENGTH_SHORT).show()
        }

        view.setOnLongClickListener {

            SheetMenu().apply{
                titleId = R.string.product_menu_title
                menu = R.menu.product_menu
                click = MenuItem.OnMenuItemClickListener {
                    when(it.itemId){
                        R.id.product_interest -> {
                            productOperations.interestProduct(view, product)
                            return@OnMenuItemClickListener true
                        }
                        R.id.product_like -> {
                            productOperations.mentionProduct(view, product, "like")
                            return@OnMenuItemClickListener true
                        }
                        R.id.product_dislike -> {
                            productOperations.mentionProduct(view, product, "dislike")
                            return@OnMenuItemClickListener true
                        }
                        R.id.product_trade -> {
                            return@OnMenuItemClickListener true
                        }
                    }
                    return@OnMenuItemClickListener false
                }
            }.show(view.context)

            return@setOnLongClickListener true
        }
    }
}