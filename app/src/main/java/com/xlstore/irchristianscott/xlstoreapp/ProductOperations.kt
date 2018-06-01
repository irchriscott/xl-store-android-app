package com.xlstore.irchristianscott.xlstoreapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.xlstore.irchristianscott.xlstoreapp.models.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.concurrent.thread

open class ProductOperations(){

    private val userDataManager = UserDataManager()
    private val helpers = Helpers()

    companion object {

        private val userDataManager = UserDataManager()

        public fun addUserMention(productOperations: ProductOperations, context: Context, mention: String, mentions: List<Mentions>, product: ProductModel?){
            val user = productOperations.userDataManager.getLoggedInUserObject(context)
            val userMention: Mentions = Mentions(product!!.id, user!!.token, mention, Date().toGMTString())
            mentions.plus(userMention)
        }

        public fun updateUserMention(context: Context, mention: String, mentions: List<Mentions>, product: ProductModel?){
            val user = userDataManager.getLoggedInUserObject(context)
            val hasMentioned = userDataManager.checkUserMentionProduct(context, mentions)
            if(hasMentioned != null && product!!.likes + product.dislikes > 0) {
                for (m in mentions) {
                    if (m.user_id == user!!.token) {
                        m.mention = mention
                    }
                }
            }
        }
    }

    private fun showToast(view: View, message: String){
        Handler(Looper.getMainLooper()).post { Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show() }
    }

    public fun interestProduct(view: View,  product: ProductModel?){

        val url = "$MAIN_API_URL/product/${product!!.id}/interess/"
        val user = userDataManager.getLoggedInUserObject(view.context)

        val jsonObject: JSONObject = JSONObject()
        val MEDIA_TYPE: MediaType = MediaType.parse("application/json")!!

        val client = OkHttpClient()

        try {
            jsonObject.put("product_id", product.id)
            jsonObject.put("user_id", user!!.token)
        }
        catch (e: JSONException){
            e.printStackTrace()
        }

        val body = RequestBody.create(MEDIA_TYPE, jsonObject.toString())

        val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call?, e: IOException?) {
                showToast(view, e!!.message.toString())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call?, response: Response?) {
                val responseString = response!!.body()!!.string()
                val jsonGSON = Gson()

                val responseMessage = jsonGSON.fromJson(responseString, ResponseMessage::class.java)

                if(responseMessage.status.toInt() == 200){

                    try {
                        val interests: List<UserInterests> = product.user_interests
                        val interest: UserInterests = UserInterests(product.id, user!!.token, Date().toGMTString())
                        interests.plus(interest)
                        view.findViewById<TextView>(R.id.productInteress).text = "${product.interess + 1}"
                        view.findViewById<TextView>(R.id.productInteress).setTextColor(Color.GREEN)
                    }
                    catch(e: Exception){
                        println(e.message)
                    }
                    showToast(view, "Product Interested Successfully !!!")

                } else {
                    helpers.showAlertInfo("Error", responseMessage.message, view.resources.getDrawable(R.drawable.ic_error_black_24dp), view.context)
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun forLike(view: View, mentions: List<Mentions>, product: ProductModel?) {

        try{
            if(mentions.count() > 0 || mentions.isNotEmpty()){

                val hasMentioned = userDataManager.checkUserMentionProduct(view.context, mentions)
                if(hasMentioned != null && product!!.likes + product.dislikes > 0){
                    if (hasMentioned == "dislike"){
                        view.findViewById<TextView>(R.id.productDislikes).text = "${product!!.dislikes - 1}"
                        view.findViewById<TextView>(R.id.productDislikes).setTextColor(Color.parseColor("#666666"))
                        view.findViewById<TextView>(R.id.productLikes).text = "${product!!.likes + 1}"
                        view.findViewById<TextView>(R.id.productLikes).setTextColor(Color.RED)
                        updateUserMention(view.context, "like", mentions, product)
                    }
                } else {
                    view.findViewById<TextView>(R.id.productLikes).text = "${product!!.likes + 1}"
                    view.findViewById<TextView>(R.id.productLikes).setTextColor(Color.RED)
                    Companion.addUserMention(this, view.context, "like", mentions, product)
                }
            } else {
                view.findViewById<TextView>(R.id.productLikes).text = "${product!!.likes + 1}"
                view.findViewById<TextView>(R.id.productLikes).setTextColor(Color.RED)
                Companion.addUserMention(this, view.context, "like", mentions, product)
            }
        }
        catch (e: Exception){
            println(e.message)
        }
        showToast(view, "Product Liked Successfully !!!")
    }

    @SuppressLint("SetTextI18n")
    private fun forDislike(view: View, mentions: List<Mentions>, product: ProductModel?) {
        try{
            if(mentions.count() > 0 || mentions.isNotEmpty()){

                val hasMentioned = userDataManager.checkUserMentionProduct(view.context, mentions)
                if(hasMentioned != null && product!!.likes + product.dislikes > 0){
                    if (hasMentioned == "like"){
                        view.findViewById<TextView>(R.id.productLikes).text = "${product!!.likes - 1}"
                        view.findViewById<TextView>(R.id.productLikes).setTextColor(Color.parseColor("#666666"))
                        view.findViewById<TextView>(R.id.productDislikes).text = "${product!!.dislikes + 1}"
                        view.findViewById<TextView>(R.id.productDislikes).setTextColor(Color.RED)
                        Companion.addUserMention(this, view.context, "dislike", mentions, product)
                    }
                } else {
                    view.findViewById<TextView>(R.id.productDislikes).text = "${product!!.dislikes + 1}"
                    view.findViewById<TextView>(R.id.productDislikes).setTextColor(Color.RED)
                    Companion.addUserMention(this, view.context, "dislike", mentions, product)
                }
            } else {
                view.findViewById<TextView>(R.id.productDislikes).text = "${product!!.dislikes + 1}"
                view.findViewById<TextView>(R.id.productDislikes).setTextColor(Color.RED)
                Companion.addUserMention(this, view.context, "like", mentions, product)
            }
        }
        catch(e: Exception){
            println(e.message)
        }
        showToast(view, "Product Disliked Successfully !!!")
    }

    public fun mentionProduct(view: View,  product: ProductModel?, mention: String){

        if(mention == "like" || mention == "dislike"){

            val url = "$MAIN_API_URL/product/${product!!.id}/mention/"
            val user = userDataManager.getLoggedInUserObject(view.context)

            val jsonObject: JSONObject = JSONObject()
            val MEDIA_TYPE: MediaType = MediaType.parse("application/json")!!

            val client = OkHttpClient()

            try {
                jsonObject.put("product_id", product.id)
                jsonObject.put("user_id", user!!.token)
                jsonObject.put("mention", mention)
            }
            catch (e: JSONException){
                e.printStackTrace()
            }

            val body = RequestBody.create(MEDIA_TYPE, jsonObject.toString())

            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build()

            client.newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call?, e: IOException?) {
                    showToast(view, e!!.message.toString())
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call?, response: Response?) {
                    val responseString = response!!.body()!!.string()
                    val jsonGSON = Gson()

                    val responseMessage = jsonGSON.fromJson(responseString, ResponseMessage::class.java)

                    if(responseMessage.status.toInt() == 200){
                        val mentions: List<Mentions> = product.mentions
                        when(mention){
                            "like" -> {
                                forLike(view, mentions, product)
                            }
                            "dislike" -> {
                                forDislike(view, mentions, product)
                            }
                            else -> {
                                showToast(view, "Unknown Mention $mention")
                            }
                        }
                    } else {
                        helpers.showAlertInfo("Error", responseMessage.message, view.resources.getDrawable(R.drawable.ic_error_black_24dp), view.context)
                    }
                }
            })

        } else {
            showToast(view, "Unknown Mention $mention")
        }
    }
}