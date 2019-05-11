package com.tapshop.tapshop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 3000 //3 seconds
    private var fadeIn: Animation? = null
    private var fadeOut: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein)
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, splashDelay)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus){
            hideSystemUI()
        }
    }
    private fun hideSystemUI(){
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
    private val mRunnable: Runnable = Runnable {

        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
