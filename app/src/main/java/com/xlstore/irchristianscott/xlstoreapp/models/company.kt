package com.xlstore.irchristianscott.xlstoreapp.models

class CompanyModel(
        var id: Int,
        var name: String,
        var urlname: String,
        var email: String,
        var profile_image: String,
        var cover_image: NullDataValueString,
        var phone_number: String,
        var country: String,
        var town: String,
        var category_id: Int,
        var category: CompanyCategoryModel,
        var motto: NullDataValueString,
        var description: NullDataValueString,
        var is_authenticated: Boolean,
        var has_seen_tutorial: Boolean,
        var registration_date: String,
        var addresses: List<AddressModel>,
        var url: String,
        var products: Int,
        var followers: Int,
        var categories: Int,
        var posts: Int,
        var advertisments: Int
){
    fun getCompanyProfileImage() = MAIN_MEDIA_ROOT_URL + this.profile_image
    fun getCompanyCoverImage() = MAIN_MEDIA_ROOT_URL + this.cover_image.String
}

class CompanyCategoryModel(var id: Int, var name: String, var image: String,var description: String )

class CategoriesModel(var id: Int, var name: String, var description: String, var created_date: String)

class AdvertismentsModel(var id: Int,var description: String, var video: String,var posted_date: String)