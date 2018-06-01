package com.xlstore.irchristianscott.xlstoreapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import java.lang.Thread.sleep

class SplashScreenActivity : AppCompatActivity() {

    lateinit var splashScreenText: TextView
    lateinit var animation: Animation
    private var delayHandler: Handler? = null
    private val SPLASH_SCREEN_TIME: Long = 5000
    private val userDataManager = UserDataManager()

    /*  Check if the user is logged in so that the splash screen might redirect him
        to home page or login page
    */

    private val runnable: Runnable = Runnable {

        if(!isFinishing){

            if (userDataManager.userIsLoggedIn(this)){
                startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
            }
            else{
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            }

            finish()
        }
    }

    //Display the app name and animate it

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splashScreenText = findViewById<TextView>(R.id.splashText) as TextView
        animation = AnimationUtils.loadAnimation(this, R.anim.animate_splash_screen)
        splashScreenText.startAnimation(animation)

        delayHandler = Handler()

        delayHandler!!.postDelayed(runnable, SPLASH_SCREEN_TIME)
    }

    //cancel thread when activity is destroyed

    override fun onDestroy() {
        super.onDestroy()

        if (delayHandler != null){
            delayHandler!!.removeCallbacks(runnable)
        }
    }
}
