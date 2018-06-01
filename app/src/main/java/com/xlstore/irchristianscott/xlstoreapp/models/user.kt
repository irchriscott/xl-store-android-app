package com.xlstore.irchristianscott.xlstoreapp.models


class UserModel(
        var token: Int,
        var name: String,
        var username: String,
        var email: String,
        var profile_image: String,
        var cover_image: NullDataValueString,
        var gender: String,
        var country: String,
        var town: String,
        var phone_number: NullDataValueString,
        var address: AddressModel,
        var registration_date: String,
        var url: String,
        var posts: Int,
        var following: Int,
        var followers: Int,
        var companies: Int,
        var interess: Int,
        var replies: Int,
        var comments: Int,
        var likes: Int,
        var dislikes: Int,
        var trades: Int,
        var categories: Int,
        var bills_carts: Int

){
    fun getUserProfileImage() = MAIN_MEDIA_ROOT_URL + this.profile_image
    fun getUserCoverImage() = MAIN_MEDIA_ROOT_URL + this.cover_image.String
}

