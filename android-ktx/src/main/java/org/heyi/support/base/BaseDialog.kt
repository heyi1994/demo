package org.heyi.support.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.view.WindowManager
import org.heyi.support.R

/**
 * #### 第一次show的时候执行onCreate方法
 *
 * @author Heyi
 * @since 1.0.0
 * @property hasFrame 是否有遮罩 ;
 */
abstract class BaseDialog(context:Context,val hasFrame:Boolean = true): Dialog(context,if(hasFrame)
    R.style.org_heyi_base_dialogStyle else R.style.org_heyi_base_dialogStyle_No_Frame){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setWindowAnimations(getWindowAnimations())
        if (isWindowWidthFullScreen()){
            window.decorView.setPadding(0,0,0,0)
            val param= window.attributes
            param.width=WindowManager.LayoutParams.MATCH_PARENT
            if (!isWindowHeightFullScreen()){
                param.height=WindowManager.LayoutParams.WRAP_CONTENT
            }else{
                param.height=WindowManager.LayoutParams.MATCH_PARENT
            }
            window.attributes=param
        }
        window.setGravity(setGravity())
        setContentView(getLayoutId())
        init()
    }


    @LayoutRes
    protected abstract fun getLayoutId():Int

    protected abstract fun isWindowWidthFullScreen():Boolean

    protected abstract fun init()

    protected abstract fun setGravity():Int

    protected open fun isWindowHeightFullScreen() = false

    @StyleRes
    protected open fun getWindowAnimations() = R.style.org_heyi_base_dialog_window_style

}