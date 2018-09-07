package org.heyi.support.extend

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Looper
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
