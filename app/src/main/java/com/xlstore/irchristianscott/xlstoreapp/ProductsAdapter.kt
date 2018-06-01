package com.xlstore.irchristianscott.xlstoreapp

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.xlstore.irchristianscott.xlstoreapp.models.*
import kotlinx.android.synthetic.main.product_row.view.*


class ProductsAdapter(private val productList: ProductList): RecyclerView.Adapter<ProductsViewHolder>(){

    override fun getItemCount(): Int {
        return productList.products.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProductsViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val productRow = layoutInflater.inflate(R.layout.product_row, parent, false)

        return ProductsViewHolder(productRow)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder?, position: Int) {

        val product = productList.products[position]
        val userDataManager = UserDataManager()

        holder?.view?.productName?.text = product.name
        holder?.view?.productPrice?.text = product.getProductPrice()
        holder?.view?.companyName?.text = product.company.name
        holder?.view?.productDate?.text = product.category.name
        holder?.view?.productInteress?.text = product.interess.toString()
        holder?.view?.productComments?.text = product.comments.toString()
        holder?.view?.productLikes?.text = product.likes.toString()
        holder?.view?.productDislikes?.text = product.dislikes.toString()

        Picasso.get().load(product.getProductImage()).into(holder?.view?.productImage)
        Picasso.get().load(product.company.getCompanyProfileImage()).into(holder?.view?.companyImage)

        holder?.product = product

        val isInterested = userDataManager.checkUserInterestProduct(holder?.view!!.context, product.user_interests)
        val hasMentioned = userDataManager.checkUserMentionProduct(holder.view.context, product.mentions)

        if(isInterested && product.interess > 0){
            holder.view.productInteress.setTextColor(Color.GREEN)
        }

        if(hasMentioned != null && product.likes + product.dislikes > 0){
            if(hasMentioned == "like"){
                holder.view.productLikes.setTextColor(Color.RED)
            } else if (hasMentioned == "dislike"){
                holder.view.productDislikes.setTextColor(Color.RED)
            }
        }
    }
}