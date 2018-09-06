package com.example.heyi.demo

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.heyi.support.extend.*
import java.io.File

class MainActivity : AppCompatActivity() {
  private val TAG:String=this.javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

        //fullScreen()


        //setStatusBarColor()



        runOnIoThread {
            toast("i'm not run on ui thread! ")
        }




        val params = tvMsg.layoutParams as LinearLayout.LayoutParams
        params.topMargin = transparentStatusBar()
        tvMsg.layoutParams  = params

        lightStatusBar()


        Log.d(TAG,"current screen width is $screenWidth ; height is $screenHeight")


        runOnUiThreadDelayed(5000){
           // pickFromGallery(1,true)

            SimpleDialog(this).show()
        }


       // Uri.fromFile(File("")).toPath(this)



        runOnUiThreadDelayed(2000){
            ivPre.setImageBitmap(tvMsg.toBitmap())
        }
    }
}
