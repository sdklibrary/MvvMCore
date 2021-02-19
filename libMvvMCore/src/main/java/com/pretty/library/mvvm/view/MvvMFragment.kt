package com.pretty.library.mvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pretty.library.mvvm.ResIdToast
import com.pretty.library.mvvm.TextToast
import com.pretty.library.mvvm.ToastType
import com.pretty.library.mvvm.viewmodel.MvvMViewModel
import org.greenrobot.eventbus.EventBus

abstract class MvvMFragment<DB : ViewDataBinding> : Fragment(), IFragment {
    val simpleName = javaClass.simpleName

    lateinit var binding: DB
    override val useEventBus = false
    private val viewModels = arrayListOf<MvvMViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (useEventBus)
            EventBus.getDefault().register(this)
        initView(view, savedInstanceState)
        bindDefaultData()
        addViewModelObserve()
    }

    override fun addViewModelObserve() {

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val fragments = childFragmentManager.fragments
        if (!fragments.isNullOrEmpty()) {
            fragments.forEach {
                it.onActivityResult(requestCode, resultCode, data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDetach() {
        dismissLoading()
        if (useEventBus && EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        viewModels.forEach {
            lifecycle.removeObserver(it)
        }
        super.onDetach()
    }

    fun <T : MvvMViewModel> createViewModel(clazz: Class<T>): T {
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(clazz)
        viewModel.showLoadingEvent.observe(this) { data -> data?.apply { showLoading(data) } }
        viewModel.dismissLoadingEvent.observe(this) { data -> data?.apply { dismissLoading() } }
        viewModel.showToastEvent.observe(this) { data -> data?.apply { showMessage(data) } }
        viewModel.finishPageEvent.observe(this) { data -> data?.apply { activity?.finish() } }
        return viewModel.apply {
            viewModels += this
            lifecycle.addObserver(this)
        }
    }

    override fun showToast(message: String?) = showMessage(TextToast(message))

    override fun showToast(@StringRes strRes: Int) = showMessage(ResIdToast(strRes))

    override fun showMessage(toast: ToastType) {
        if (activity is MvvMActivity<*>)
            (activity as MvvMActivity<*>).showMessage(toast)
    }

    override fun showLoading(message: String) {
        if (activity is MvvMActivity<*>)
            (activity as MvvMActivity<*>).showLoading(message)
    }

    override fun dismissLoading() {
        if (activity is MvvMActivity<*>)
            (activity as MvvMActivity<*>).dismissLoading()
    }
}