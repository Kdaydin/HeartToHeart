package com.khomeapps.hearttoheart.ui.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {

    open fun onStart() {}

    open fun onStop() {}

    open fun onPause() {}

    open fun onCreate() {}

    open fun onResume() {}

    open fun onCreateView() {}

    open fun onViewCreated() {}
}