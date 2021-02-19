package com.pretty.library.mvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.pretty.library.mvvm.ResIdToast
import com.pretty.library.mvvm.TextToast
import com.pretty.library.mvvm.ToastType
import com.pretty.library.mvvm.viewmodel.MvvMViewModel
import org.greenrobot.eventbus.EventBus

abstract class MvvMActivity<DB : ViewDataBinding> : AppCompatActivity(), IActivity {

    val simpleName = javaClass.simpleName

    lateinit var binding: DB
    private var mToast: Toast? = null
    override val useEventBus = false
    private val viewModels = arrayListOf<MvvMViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        if (useEventBus)
            EventBus.getDefault().register(this)
        initView(savedInstanceState)
        bindDefaultData()
        addViewModelObserve()
    }

    override fun bindDefaultData() {

    }

    override fun addViewModelObserve() {

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        val fragments = supportFragmentManager.fragments
        if (!fragments.isNullOrEmpty()) {
            fragments.forEach {
                it.onActivityResult(requestCode, resultCode, data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (KeyEvent.KEYCODE_BACK == keyCode) {
            finish()
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        dismissLoading()
        //取消订阅
        if (useEventBus && EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        viewModels.forEach {
            lifecycle.removeObserver(it)
        }
        super.onDestroy()
    }

    fun <T : MvvMViewModel> createViewModel(clazz: Class<T>): T {
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(clazz)
        viewModel.showLoadingEvent.observe(this) { data -> data?.apply { showLoading(data) } }
        viewModel.dismissLoadingEvent.observe(this) { data -> data?.apply { dismissLoading() } }
        viewModel.showToastEvent.observe(this) { data -> data?.apply { showMessage(data) } }
        viewModel.finishPageEvent.observe(this) { data -> data?.apply { finish() } }
        return viewModel.apply {
            viewModels += this
            lifecycle.addObserver(this)
        }
    }

    override fun showToast(message: String?) = showMessage(TextToast(message))

    override fun showToast(@StringRes strRes: Int) = showMessage(ResIdToast(strRes))

    override fun showMessage(toast: ToastType) {
        if (isFinishing || isDestroyed)
            return
        mToast?.cancel()
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT).apply {
            when (toast) {
                is TextToast -> {
                    setText(toast.value)
                    toast.value != null && toast.value != ""
                }
                is ResIdToast -> {
                    setText(toast.value)
                    toast.value != 0
                }
            }.let {
                if (it)
                    show()
            }
        }
    }

}