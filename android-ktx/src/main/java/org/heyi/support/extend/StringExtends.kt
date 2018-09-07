package org.heyi.support.extend

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import java.util.regex.Pattern
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject



/**
 * @author Melrose
 * @since 1.0.0
 */

fun String.isPhoneNumber(): Boolean {
    val p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$")
    val m = p.matcher(this)
    return m.matches()
}

/**
 *  Check if it is Email ;
 */
fun String.isEmail()= Pattern.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,})\$", this)


/**
 * Matches all IPV6 and IPV4 addresses ;
 */
fun String.isIpAddress()= Pattern.matches("([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4}|(\\d{1,3}\\.){3}\\d{1,3}", this)

/**
 * Check if the string is a MAC address ;
 */
fun String.isMacAddress()= Pattern.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", this)


/**
 * check if the string is valid JSON ;
 */
fun String.isJson():Boolean {
    try {
        JSONObject(this)
    } catch (ex: JSONException) {
        try {
            JSONArray(this)
        } catch (ex1: JSONException) {
            return false
        }
        return false
    }
    return true
}

/**
 * 复制文字 ;
 *
 * @param after 复制完成后执行的闭包 ;
 */
fun String.copy(cxt:Context,after:(()->Unit)?=null){
     ClipData.newPlainText("text", this).apply {
         val cm = cxt.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
         cm.primaryClip = this
         after?.invoke()
     }
}


