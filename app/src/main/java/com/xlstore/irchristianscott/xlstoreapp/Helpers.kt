package com.xlstore.irchristianscott.xlstoreapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import com.google.gson.Gson
import com.xlstore.irchristianscott.xlstoreapp.models.Mentions
import com.xlstore.irchristianscott.xlstoreapp.models.ProductModel
import com.xlstore.irchristianscott.xlstoreapp.models.UserInterests
import com.xlstore.irchristianscott.xlstoreapp.models.UserModel

class Helpers{

    fun checkConnection(context: Context): Boolean{

        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo = connectivityManager.activeNetworkInfo

        return  activeNetwork.isConnected && activeNetwork.isConnectedOrConnecting
    }

    fun showAlertInfo(title: String, message: String, drawable: Drawable?, context: Context){
        val alertDialog = AlertDialog.Builder(context).create()

        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setIcon(drawable)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", {
            _, _ ->
        })

        alertDialog.show()
    }

    fun formatDateTime(time: String){
        val now = System.currentTimeMillis() / 1000
    }
}

class UserDataManager{

    fun saveUserData(user: String, context: Context){
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()
        sharedPreferencesEditor.putString("user", user)
        sharedPreferencesEditor.apply()
        sharedPreferencesEditor.commit()
    }

    fun userIsLoggedIn(context: Context): Boolean{
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString("user", null) != null
    }

    fun getLoggedInUserObject(context: Context): UserModel?{

        if (userIsLoggedIn(context)){

            val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val user = sharedPreferences.getString("user", null)
            val jsonBuilder = Gson()

            return jsonBuilder.fromJson(user, UserModel::class.java)
        }

        return null
    }

    fun checkUserInterestProduct(context: Context, interests: List<UserInterests>): Boolean{

        val user = getLoggedInUserObject(context)

        if(interests.count() > 0){
            for (interest in interests){
                if(user != null && interest.user_id != null){
                    if (interest.user_id == user.token){
                        return true
                    }
                    return false
                }
                return false
            }
            return false
        }

        return false
    }

    fun checkUserMentionProduct(context: Context, mentions: List<Mentions>): String?{

        val user = getLoggedInUserObject(context)

        if (mentions.count() > 0) {
            for (mention in mentions) {
                if(user != null && mention.user_id != null) {
                    if (mention.user_id == user!!.token) {
                        return mention.mention
                    }
                    return null
                }
                return null
            }
            return null
        }

        return null
    }

}