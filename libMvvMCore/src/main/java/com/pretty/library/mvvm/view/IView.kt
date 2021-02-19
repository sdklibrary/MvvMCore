package com.pretty.library.mvvm.view

import androidx.annotation.StringRes
import com.pretty.library.mvvm.ToastType

interface IView {

    /**
     * 布局Id
     * @author Arvin.xun
     */
    val layoutId: Int

    /**
     * 是否使用事件
     * @author Arvin.xun
     */
    val useEventBus: Boolean

    /**
     * 绑定默认数据
     * @author Arvin.xun
     */
    fun bindDefaultData()

    /**
     * 添加viewModel订阅
     * @author Arvin.xun
     */
    fun addViewModelObserve()

    /**
     * 显示loading
     * @author Arvin.xun
     */
    fun showLoading(message: String = "")

    /**
     * 关闭Loading
     * @author Arvin.xun
     */
    fun dismissLoading()

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
     * 显示提示语
     * @author Arvin.xun
     */
    fun showMessage(toast: ToastType)

}