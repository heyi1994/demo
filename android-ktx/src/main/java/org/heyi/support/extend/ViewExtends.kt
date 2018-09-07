package org.heyi.support.extend

import android.graphics.Bitmap
import android.support.annotation.LayoutRes
import android.support.v4.view.ViewCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author Melrose
 * @since 1.0.0
 */
fun ViewGroup.inflate(@LayoutRes id:Int) = LayoutInflater.from(context).inflate(id,this,false)


val ViewGroup.screenWidth:Int get() = context.resources.displayMetrics.widthPixels
val ViewGroup.screenHeight:Int get() = context.resources.displayMetrics.heightPixels



fun View.dp2px(dp:Int)= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp.toFloat(),resources.displayMetrics).toInt()


/**
 * Return a [Bitmap] representation of this [View].
 *
 * - 得到的位图将与该视图当前布局尺寸相同的宽度和高度。这并不考虑任何转换，如缩放或平移 .
 *
 * - 注意，这将使用软件渲染管道将视图绘制到位图。这可能导致对硬件加速画布（如设备屏幕）渲染的不同绘制 .
 *
 * - 如果尚未设置此视图，此方法将抛出[IllegalStateException] .
 *
 * @param config Bitmap config of the desired bitmap. Defaults to [Bitmap.Config.ARGB_8888].
 */
fun View.toBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
    if (!ViewCompat.isLaidOut(this)) {
        throw IllegalStateException("View needs to be laid out before calling toBitmap()")
    }
    return Bitmap.createBitmap(width, height, config).applyCanvas {
        translate(-scrollX.toFloat(), -scrollY.toFloat())
        draw(this)
    }
}


/**
 *  view.isInvisible = true
 */
inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }


inline var View.isInvisible: Boolean
    get() = visibility == View.INVISIBLE
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }