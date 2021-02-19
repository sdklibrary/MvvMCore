package com.pretty.library.mvvm

import androidx.annotation.StringRes

sealed class ToastType

class TextToast(val value: String?) : ToastType()

class ResIdToast(@StringRes val value: Int) : ToastType()

