package com.xlstore.irchristianscott.xlstoreapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.xlstore.irchristianscott.xlstoreapp.models.MAIN_API_URL
import com.xlstore.irchristianscott.xlstoreapp.models.ResponseMessage
import com.xlstore.irchristianscott.xlstoreapp.models.UserModel
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var signinButton : Button
    private lateinit var userEmailText : EditText
    private  lateinit var userPasswordText : EditText
    val helpers = Helpers()
    val userDataManager = UserDataManager()

    //When the view is created, all my input forms will be initialized

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userEmailText = findViewById<EditText>(R.id.user_email) as EditText
        userPasswordText = findViewById<EditText>(R.id.user_password) as EditText

        val userEmail = userEmailText.text
        val userPassword = userPasswordText.text

        signinButton = findViewById<Button>(R.id.signin) as Button
        signinButton.setOnClickListener {
            userAuthentication(userEmail, userPassword)
        }
    }

    //This function authenticate the user and keep user's data in local storage

    private fun userAuthentication(email: Any, password: Any){

        val url = "$MAIN_API_URL/user/authenticate/"

        //create user object in JSON format

        val jsonObject: JSONObject = JSONObject()
        val MEDIA_TYPE: MediaType = MediaType.parse("application/json")!!

        val client = OkHttpClient()

        try {
            jsonObject.put("email", email)
            jsonObject.put("password", password)
        }
        catch (e: JSONException){
            e.printStackTrace()
        }

        //send the request

        val body = RequestBody.create(MEDIA_TYPE, jsonObject.toString())

        val request = Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .build()

        client.newCall(request).enqueue(object: Callback{

            //on Request failed

            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, e?.message, Toast.LENGTH_SHORT).show()
                    user_password.text = null
                }
            }

            //on request success

            override fun onResponse(call: Call?, response: Response?) {

                val responseBody = response!!.body()!!.string()
                val jsonBuilder =  Gson()

                if (responseBody.contains("message", ignoreCase = true)){

                    //show the message if errors

                    val errorMessage: ResponseMessage = jsonBuilder.fromJson(responseBody, ResponseMessage::class.java)

                    runOnUiThread {
                        helpers.showAlertInfo("Error", errorMessage.message, resources.getDrawable(R.drawable.ic_error_black_24dp), this@LoginActivity)
                    }
                } else if (responseBody.contains("token", ignoreCase = true)){

                    //login user if no error

                    val userData: UserModel = jsonBuilder.fromJson(responseBody, UserModel::class.java)
                    userDataManager.saveUserData(responseBody, this@LoginActivity)

                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "You're Logged In as ${userData.username}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    }
                }
            }
        })

    }
}

//String mMessage = response.body().string();
//if (response.isSuccessful()){
//    try {
//        JSONObject json = new JSONObject(mMessage);
//        final String serverResponse = json.getString("Your Index");
//
//    } catch (Exception e){
//        e.printStackTrace();
//    }
