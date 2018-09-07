package org.heyi.support.utils

import android.view.Window
import android.view.WindowManager

/**
 * @author Heyi
 * @since 1.0.0
 */
internal object StatusBarUtils {
     fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
                 var result = false
                 if (window != null) {
                     val clazz = window.javaClass
                     try {
                         var darkModeFlag = 0
                         val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                         val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                         darkModeFlag = field.getInt(layoutParams)
                         val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                         if (dark) {
                             extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
                         } else {
                             extraFlagField.invoke(window, 0, darkModeFlag)
                         }
                         result = true
                     } catch (e: Exception) {

                     }

                 }
                 return result
             }


     fun FlymeSetStatusBarLightMode(window: Window,dark: Boolean):Boolean{
                 var result =false
                 try {
                     val lp=window.attributes
                    val darkFlag= WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                     val meizuFlags=WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
                     darkFlag.isAccessible=true
                     meizuFlags.isAccessible=true
                     val bit=darkFlag.getInt(null)
                       var value=meizuFlags.getInt(lp)
                     if (dark) {
                         value = value or bit
                     } else {
                         value = value and bit.inv()
                     }
                     meizuFlags.setInt(lp, value)
                     window.setAttributes(lp)
                     result = true
                 }catch (e :Exception){}
                 return result
             }



}