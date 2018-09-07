package com.example.heyi.demo

import android.Manifest
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.heyi.support.extend.*
import java.io.File
import java.util.*

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



            if (checkSinglePermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    &&checkSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                tvMsg.toBitmap().saveToFile(
                        File(Environment.getExternalStorageDirectory().absolutePath+
                                "/"+Environment.DIRECTORY_DCIM+"/test_${System.currentTimeMillis()}.jpg").apply {
                            if (!this.exists())this.createNewFile()
                        }){
                    isSuccess->
                    toast(if (isSuccess)"The bitmap save success !"
                    else "Get some error !")
                }
            }
        }






    }
}
