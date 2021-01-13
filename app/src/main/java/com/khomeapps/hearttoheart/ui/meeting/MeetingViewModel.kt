package com.khomeapps.hearttoheart.ui.meeting

import androidx.lifecycle.MutableLiveData
import com.khomeapps.hearttoheart.ui.base.BaseViewModel

class MeetingViewModel : BaseViewModel() {
    var username = MutableLiveData("")
    var password = MutableLiveData("")
}