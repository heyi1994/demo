package org.heyi.support.extend

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Looper
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import java.io.File
import java.io.FileOutputStream

/**
 * @author Melrose
 * @since 1.0.0
 */
/**
 * Creates a new [Canvas] to draw on this bitmap and executes the specified
 * [block] on the newly created canvas. Example:
 *
 * ```
 * return Bitmap.createBitmap(…).applyCanvas {
 *    drawLine(…)
 *    translate(…)
 *    drawRect(…)
 * }
 * ```
 */
inline fun Bitmap.applyCanvas(block: Canvas.() -> Unit): Bitmap {
    val c = Canvas(this)
    c.block()
    return this
}


/**
 * save bitmap data to file ;
 *
 * @param file target file ;
 * @param resultCallback 处理图片的结果,运行在子线程 ;
 */
 fun Bitmap.saveToFile(file:File,resultCallback:((Boolean)->Unit)?=null){
    if (Thread.currentThread()==Looper.getMainLooper().thread) runOnIoThread { progressToFile(file,resultCallback) }
    else progressToFile(file,resultCallback)
}

/**
 * must run on io thread !
 */
private  fun Bitmap.progressToFile(file: File,resultCallback:((Boolean)->Unit)?=null){
    check(Thread.currentThread()!=Looper.getMainLooper().thread){ " Must run on io thread !" }
    if (!file.exists()||!file.canWrite()){
        resultCallback?.invoke(false)
        return
    }
    FileOutputStream(file).also {
        try {
            this.compress(Bitmap.CompressFormat.JPEG,90,it)
            it.flush()
            it.close()
            resultCallback?.invoke(true)
        }catch (e:Exception){
            resultCallback?.invoke(false)
        }
    }
}



/**
 * 图片缩放比例 ;
 */
internal const val BITMAP_SCALE = 0.4f

/**
 * 最大模糊度(在0.0到25.0之间) ;
 */
internal const val BLUR_RADIUS = 0.25f

/**
 * 模糊图片 ;
 * @param scale 图片缩放比例 ;
 * @param blurRadius 最大模糊度 ;
 */
fun Bitmap.blur(context: Context,scale:Float = BITMAP_SCALE,blurRadius:Float = BLUR_RADIUS):Bitmap{
    val width =  Math.round(this.getWidth() * scale)
    val height = Math.round(this.getHeight() * scale)
    val inputBitmap = Bitmap.createScaledBitmap(this, width, height, false)
    val outputBitmap = Bitmap.createBitmap(inputBitmap)
    val rs = RenderScript.create(context)
    val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
    blurScript.setRadius(blurRadius)
    blurScript.setInput(tmpIn)
    blurScript.forEach(tmpOut)
    tmpOut.copyTo(outputBitmap)
    return  outputBitmap
}
