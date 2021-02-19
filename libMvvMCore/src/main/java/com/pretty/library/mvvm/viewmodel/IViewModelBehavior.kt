package com.pretty.library.mvvm.viewmodel

import androidx.annotation.StringRes

interface IViewModelBehavior {

    /**
     * 显示Loading
     * @author Arvin.xun
     */
    fun showLoading(message: String = "")

    /**
     * 关闭Loading
     * @author Arvin.xun
     */
    fun dismissLoading()

    /**
     * 刷新完成
     * @author Arvin.xun
     */
    fun finishRefresh(isSuccess: Boolean = true)

    /**
     * 加载更多完成
     * @author Arvin.xun
     */
    fun finishLoadMore(isSuccess: Boolean = true)

    /**
     * 加载更多是否可用
     * @author Arvin.xun
     */
    fun loadMoreEnable(enable: Boolean)

    /**
     * 显示提示语
     * @author Arvin.xun
     */
    fun showToast(message: String?)

    /**
     * 显示提示语
     * @author Arvin.xun
     */
    fun showToast(@StringRes strRes: Int)

    /**
     * 关闭页面
     * @author Arvin.xin
     */
    fun finishPage()
}