package org.heyi.support.extend

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

/**
 * @author 何怡
 * @since 1.0.0
 */
private val IO_EXECUTOR = Executors.newSingleThreadExecutor()
private val mMainHandler = Handler(Looper.getMainLooper())

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun runOnIoThread(body : () -> Unit) {
    IO_EXECUTOR.execute(body)
}



/**
 * run on ui thread .
 */
fun runOnUiThread(block:()->Unit){
    mMainHandler.post(block)
}

/**
 * run on UI Thread delayed ;
 */
fun runOnUiThreadDelayed(delayMillis:Long,block:()->Unit){
    mMainHandler.postDelayed(block,delayMillis)
}