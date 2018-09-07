package org.heyi.support.extend

import android.content.Context
import android.os.*
import android.support.annotation.StringRes
import android.util.Size
import android.util.SizeF
import android.util.TypedValue
import android.widget.Toast
import java.io.Serializable
import java.util.concurrent.locks.Lock

/**
 * @author Melrose
 * @since 1.0.0
 */

/**
 * ##### 给一个函数加锁 ;
 * @param lock  see [Lock] ;
 * @param body  要被锁的函数 ;
 */
inline fun <T> lockFun(lock: Lock, body: () -> T): T {
    lock.lock()
    try {
        return body()
    } finally {
        lock.unlock()
    }
}

/**
 * dp to px ;
 */
fun dp2px(dp: Float, context: Context) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        dp, context.resources.displayMetrics).toInt()

/**
 * sp to px ;
 */
fun sp2px(sp: Float, context: Context) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
        sp, context.resources.displayMetrics).toInt()

/**
 * #### show toast ! run any thread !
 */
fun toast(msg: String, cxt: Context?) {
    if (Thread.currentThread() == Looper.getMainLooper().thread) Toast.makeText(cxt, msg, Toast.LENGTH_SHORT).show()
    else runOnUiThread {
        Toast.makeText(cxt, msg, Toast.LENGTH_SHORT).show()
    }
}

/**
 * #### show toast ! run any thread !
 */
 fun toast(@StringRes id: Int, cxt: Context?) {
    if (Thread.currentThread() == Looper.getMainLooper().thread) Toast.makeText(cxt, id, Toast.LENGTH_SHORT).show()
    else runOnUiThread {
        Toast.makeText(cxt, id, Toast.LENGTH_SHORT).show()
    }
}



/**
 * Returns a new [Bundle] with the given key/value pairs as elements ;
 *
 * @throws IllegalArgumentException When a value is not a supported type of [Bundle] ;
 */
fun bundleOf(vararg pairs: Pair<String, Any?>) = Bundle(pairs.size).apply {
    for ((key, value) in pairs) {
        when (value) {
            null -> putString(key, null) // Any nullable type will suffice.

            // Scalars
            is Boolean -> putBoolean(key, value)
            is Byte -> putByte(key, value)
            is Char -> putChar(key, value)
            is Double -> putDouble(key, value)
            is Float -> putFloat(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Short -> putShort(key, value)

            // References
            is Bundle -> putBundle(key, value)
            is CharSequence -> putCharSequence(key, value)
            is Parcelable -> putParcelable(key, value)

            // Scalar arrays
            is BooleanArray -> putBooleanArray(key, value)
            is ByteArray -> putByteArray(key, value)
            is CharArray -> putCharArray(key, value)
            is DoubleArray -> putDoubleArray(key, value)
            is FloatArray -> putFloatArray(key, value)
            is IntArray -> putIntArray(key, value)
            is LongArray -> putLongArray(key, value)
            is ShortArray -> putShortArray(key, value)

            // Reference arrays
            is Array<*> -> {
                val componentType = value::class.java.componentType
                @Suppress("UNCHECKED_CAST") // Checked by reflection.
                when {
                    Parcelable::class.java.isAssignableFrom(componentType) -> {
                        putParcelableArray(key, value as Array<Parcelable>)
                    }
                    String::class.java.isAssignableFrom(componentType) -> {
                        putStringArray(key, value as Array<String>)
                    }
                    CharSequence::class.java.isAssignableFrom(componentType) -> {
                        putCharSequenceArray(key, value as Array<CharSequence>)
                    }
                    java.io.Serializable::class.java.isAssignableFrom(componentType) -> {
                        putSerializable(key, value)
                    }
                    else -> {
                        val valueType = componentType.canonicalName
                        throw IllegalArgumentException(
                                "Illegal value array type $valueType for key \"$key\"")
                    }
                }
            }

            is Serializable -> putSerializable(key, value)

            else -> {
                if (Build.VERSION.SDK_INT >= 18 && value is Binder) {
                    putBinder(key, value)
                } else if (Build.VERSION.SDK_INT >= 21 && value is Size) {
                    putSize(key, value)
                } else if (Build.VERSION.SDK_INT >= 21 && value is SizeF) {
                    putSizeF(key, value)
                } else {
                    val valueType = value.javaClass.canonicalName
                    throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
                }
            }
        }
    }
}



