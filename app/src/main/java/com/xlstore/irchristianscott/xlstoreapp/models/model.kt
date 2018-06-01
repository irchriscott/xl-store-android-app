package com.xlstore.irchristianscott.xlstoreapp.models

const val MAIN_API_URL = "http://10.0.2.2:8800/api"
const val MAIN_MEDIA_ROOT_URL = "http://10.0.2.2:8000/media/"

class NullDataValueString(var String: String, var Valid: Boolean)

class NullDataValueInt64(var Int64: Int, var Valid: Boolean)

class AddressModel(var address: String, var latitude: Float, var longitude: Float)

class ResponseMessage(var message: String, var status: String)