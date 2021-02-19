package com.pretty.library.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pretty.library.mvvm.ResIdToast
import com.pretty.library.mvvm.TextToast
import com.pretty.library.mvvm.ToastType
import com.pretty.library.mvvm.repository.MvvMRepository
import org.greenrobot.eventbus.EventBus

open class MvvMViewModel : ViewModel(), IViewModelLifecycle, IViewModelBehavior {

    lateinit var application: Application

    open val useEventBus = false

    private val repositories = arrayListOf<MvvMRepository>()

    var showLoadingEvent = MutableLiveData<String>()
        private set

    var dismissLoadingEvent = MutableLiveData<Any>()

    var refreshEvent = MutableLiveData<Boolean>()
        private set

    var loadMoreEvent = MutableLiveData<Boolean>()
        private set

    var loadMoreEnable = MutableLiveData<Boolean>()
        private set

    var finishPageEvent = MutableLiveData<Any>()
        private set

    var showToastEvent = MutableLiveData<ToastType>()
        private set

    override fun onCreate() {
        if (useEventBus)
            EventBus.getDefault().register(this)
    }

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun onStop() {}

    override fun onDestroy() {
        showLoadingEvent.value = null
        dismissLoadingEvent.value = null
        showToastEvent.value = null
        refreshEvent.value = null
        loadMoreEvent.value = null
        loadMoreEnable.value = null
        repositories.forEach { it.onDestroy() }
        repositories.clear()
        if (useEventBus && EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    override fun showLoading(message: String) {
        showLoadingEvent.postValue(message)
    }

    override fun dismissLoading() {
        dismissLoadingEvent.postValue(true)
    }

    override fun finishRefresh(isSuccess: Boolean) {
        refreshEvent.postValue(isSuccess)
    }

    override fun finishLoadMore(isSuccess: Boolean) {
        loadMoreEvent.postValue(isSuccess)
    }

    override fun loadMoreEnable(enable: Boolean) {
        loadMoreEnable.postValue(enable)
    }

    override fun showToast(message: String?) {
        showToastEvent.postValue(TextToast(message))
    }

    override fun showToast(strRes: Int) {
        showToastEvent.postValue(ResIdToast(strRes))
    }

    override fun finishPage() {
        finishPageEvent.postValue(true)
    }

    fun <T : MvvMRepository> createRepository(repository: Class<T>): T {
        val instance = repository.newInstance()
        this.repositories += instance
        return instance
    }
}