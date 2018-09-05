package com.example.heyi.demo

import android.content.Context
import android.view.Gravity
import org.heyi.base.base.BaseDialog

/**
 * @author Melrose
 * @since 1.0.0
 */
class SimpleDialog(cxt:Context):BaseDialog(cxt) {
    override fun getLayoutId() = R.layout.dialog_simple

    override fun init() {

    }
    override fun isWindowWidthFullScreen() = false

    override fun setGravity()  =  Gravity.CENTER
}