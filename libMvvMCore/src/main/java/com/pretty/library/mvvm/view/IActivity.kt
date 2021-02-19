package com.pretty.library.mvvm.view

import android.os.Bundle

interface IActivity : IView {

    /**
     * 初始化view
     * @author Arvin.xun
     */
    fun initView(savedInstanceState: Bundle?)

}