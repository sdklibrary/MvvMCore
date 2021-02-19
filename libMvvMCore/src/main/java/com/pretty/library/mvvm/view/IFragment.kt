package com.pretty.library.mvvm.view

import android.os.Bundle
import android.view.View

internal interface IFragment : IView {

    /**
     * 初始化view
     * @author Arvin.xun
     */
    fun initView(view: View, savedInstanceState: Bundle?)

}