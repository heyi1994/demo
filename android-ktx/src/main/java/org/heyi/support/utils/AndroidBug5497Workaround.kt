package org.heyi.support.utils

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout

/**
 * @author Melrose
 * @since 1.0.0
 */
class AndroidBug5497Workaround private constructor(private val mActivity: Activity,
                                                   private val isFullScreen: Boolean){
    companion object {
        /**
         * @param isFullScreen 是否为全屏,包括透明状态栏模式 ;
         */
        fun assistActivity(activity: Activity,isFullScreen:Boolean = true){
            AndroidBug5497Workaround(activity, isFullScreen)
        }
    }


    private var mChildOfContent:View?=null
    private var usableHeightPrevious = 0
    private var frameLayoutParams: FrameLayout.LayoutParams?=null
    private var contentHeight = 0
    private var isfirst = true
    private var statusBarHeight = 0



    init {
        val resourceId = mActivity.resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBarHeight = mActivity.resources.getDimensionPixelSize(resourceId)
        val content = mActivity.findViewById(android.R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)
        mChildOfContent?.getViewTreeObserver()?.addOnGlobalLayoutListener {
            if (isfirst) {
                contentHeight = mChildOfContent?.getHeight()?:0
                isfirst = false
            }
            possiblyResizeChildOfContent()
        }

        frameLayoutParams = mChildOfContent?.getLayoutParams() as FrameLayout.LayoutParams
    }


    private fun possiblyResizeChildOfContent() {

        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = mChildOfContent?.getRootView()?.height?:0
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    frameLayoutParams?.height = usableHeightSansKeyboard - heightDifference + statusBarHeight
                } else {
                    frameLayoutParams?.height = usableHeightSansKeyboard - heightDifference
                }
            } else {
                frameLayoutParams?.height = contentHeight
            }

            mChildOfContent?.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent?.getWindowVisibleDisplayFrame(r)
        return if (isFullScreen)(r.bottom - r.top) else r.bottom
    }

}