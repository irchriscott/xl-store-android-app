package com.xlstore.irchristianscott.xlstoreapp.models

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

class ProductModel(
        var id: Int,
        var company_id: Int,
        var company: CompanyModel,
        var name: String,
        var category_id: Int,
        var category: CategoriesModel,
        var image: String,
        var price: Float,
        var currency: String,
        var description: String,
        var posted_date: String,
        var advertisment: List<AdvertismentsModel>,
        var other_images: List<OtherImagesModel>,
        var interess: Int,
        var comments: Int,
        var likes: Int,
        var dislikes: Int,
        var mentions: List<Mentions>,
        var user_interests: List<UserInterests>

){
    fun getProductImage() = MAIN_MEDIA_ROOT_URL + this.image
    fun getProductPrice() = "${this.price} ${this.currency}"
}

class OtherImagesModel(var image: String)

class ProductList(var products: List<ProductModel>)

class Mentions(var product_id: Int, var user_id: Int, var mention: String, var mention_date: String)

class UserInterests(var product_id: Int, var user_id: Int, var interess_date: String)

class ProductCommentModel(
        var id: Int,
        var product_id: Int,
        var company_id: NullDataValueInt64,
        var user_id: NullDataValueInt64,
        var company: CompanyModel?,
        var user: UserModel?,
        var commenter: String,
        var comment: String,
        var comment_date: String
)

open class Product{

    private  fun fetchProductsList(): String?{

        val url = "$MAIN_API_URL/products/all/"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        return response.body()?.string()
    }

    fun getProductsList(): ProductList {

        val productsString = fetchProductsList()
        val productsJSON = GsonBuilder().setPrettyPrinting().create()
        val products: List<ProductModel> = productsJSON.fromJson(productsString, object : TypeToken<List<ProductModel>>() {}.type)

        return ProductList(products)
    }
}