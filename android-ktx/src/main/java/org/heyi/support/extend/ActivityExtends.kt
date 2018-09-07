package org.heyi.support.extend

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.os.PowerManager
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import org.heyi.support.R
import org.heyi.support.utils.AndroidBug5497Workaround
import org.heyi.support.utils.Manufacturer
import org.heyi.support.utils.StatusBarUtils
import java.io.File

/**
 * @author Heyi
 * @since 1.0.0
 */

/**
 * #### 保持页面常亮 ; 更多实现方式参考[PowerManager] ;
 */
fun Activity.keepScreenOn() {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}


/**
 * #### activity的当前window全屏 ;
 */
fun Activity.fullScreen() {
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

/**
 * #### 给状态栏染色 ;
 * @param colorRes 颜色ResId ,默认为App默认给控件染色主题颜色 ;
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.setStatusBarColor(@ColorRes colorRes: Int = R.color.md_org_heyi_base_pink_100) {
    if (Build.VERSION.SDK_INT >= 21) window.statusBarColor = resources.getColor(colorRes)
}


/**
 * #### 将状态栏设置为透明模式,并返回状态栏的高度 ;
 * @return 返回状态栏高度 ;
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
fun Activity.transparentStatusBar(): Int {
    if (Build.VERSION.SDK_INT >= 21) {
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
    } else return 0
}


/**
 * #### 将状态栏字体颜色设置成DIM色 ;因为一些厂商把rom进行了更改,利用反射将某些厂商适配 ;
 */
@TargetApi(Build.VERSION_CODES.M)
fun Activity.lightStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.MANUFACTURER == Manufacturer.XIAOMI.info) StatusBarUtils.MIUISetStatusBarLightMode(window, true)
        else if (Build.MANUFACTURER == Manufacturer.MEIZU.info) StatusBarUtils.FlymeSetStatusBarLightMode(window, true)
    }
}

/**
 * 从图库选择图片,需要自己处理选择结果 ;
 * @param allowMultiple 是否支持多选 ;
 * @see [Activity.onActivityResult]
 */
fun Activity.pickFromGallery(requestCode: Int,allowMultiple:Boolean=false,@StringRes title:Int = R.string.org_heyi_base_select_photo_title) {
    val intent = Intent()
    intent.setType("image/*")
    intent.setAction(Intent.ACTION_GET_CONTENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,allowMultiple)
    startActivityForResult(Intent.createChooser(intent, getString(title)), requestCode)
}


/**
 * 将图片文件添加到相册 ;
 * @param photo 图片 ;
 */
fun Activity.galleryAddPic(photo: File) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    val contentUri = Uri.fromFile(photo)
    mediaScanIntent.setData(contentUri)
    sendBroadcast(mediaScanIntent)
}


/**
 * #### check the permission's has be granted !
 * @return true if the app has the permission !
 */
fun Activity.checkSinglePermission(permission: String, requestCode: Int = 4): Boolean {
    return if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        false
    } else true
}

/**
 * #### 修复网页或Activity中的的输入框被遮挡问题 ;
 * @param isFullScreen 是否为全屏状态,透明状态栏模式也为全屏模式 ;
 */
fun Activity.fixBug5497(isFullScreen:Boolean){
    AndroidBug5497Workaround.assistActivity(this,isFullScreen)
}

/**
 * 当前屏幕的宽度 ;
 */
val Activity.screenWidth:Int get() = resources.displayMetrics.widthPixels

/**
 * 当前屏幕的高度 ;
 */
val Activity.screenHeight:Int get() = resources.displayMetrics.heightPixels


/**
 * #### show toast ! run any thread !
 */
fun Activity.toast(msg: String) {
    if (Thread.currentThread() == Looper.getMainLooper().thread) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    else runOnUiThread {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

/**
 * #### show toast ! run any thread !
 */
fun Activity.toast(@StringRes id: Int) {
    if (Thread.currentThread() == Looper.getMainLooper().thread) Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
    else runOnUiThread {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
    }
}


/**
 * 调整到外部网页 ;
 */
fun Activity.goWebPage(url: String) {
    Uri.parse(url).apply {
        Intent(Intent.ACTION_VIEW, this).also {
            webIntent->
            webIntent.resolveActivity(this@goWebPage.getPackageManager())?.let { startActivity(webIntent) }
        }
    }
}
