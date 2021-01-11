package com.khomeapps.hearttoheart.ui.dashboard

import com.khomeapps.hearttoheart.R
import com.khomeapps.hearttoheart.databinding.ActivityDashboardBinding
import com.khomeapps.hearttoheart.ui.base.BaseActivity

class DashboardActivity : BaseActivity<DashboardViewModel, ActivityDashboardBinding>() {

    override fun getLayoutRes(): Int = R.layout.activity_dashboard

    override fun getViewModelType(): DashboardViewModel = DashboardViewModel()

}