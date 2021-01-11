package com.khomeapps.hearttoheart.ui.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.khomeapps.hearttoheart.R
import com.khomeapps.hearttoheart.data.entities.Permissions

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    protected var viewModel: VM? = null
    protected var binding: DB? = null
    protected var indicator: ContentLoadingProgressBar? = null
    protected var loadStackCount: Int = 0

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun getViewModelType(): VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, getLayoutRes())
        viewModel = getViewModelType()
        binding?.setVariable(BR.viewModel, viewModel)
        binding?.lifecycleOwner = this
        val parent = this.binding?.root as ViewGroup
        indicator = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_loading, parent, false) as ContentLoadingProgressBar
        indicator?.hide()
        (this.binding?.root as ViewGroup).addView(
            indicator, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        if (indicator?.isShown == true) {
            resetLoadStack()
            hideLoading()
        }
        viewModel?.onCreate()
    }

    override fun onStart() {
        super.onStart()
        viewModel?.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewModel?.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel?.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel?.onStop()
    }

    override fun onDestroy() {
        binding?.unbind()
        binding?.lifecycleOwner = null
        binding = null
        super.onDestroy()
    }

    fun showLoading() {
        loadStackCount += 1
        indicator?.show()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideLoading() {
        if (loadStackCount <= 1) {
            loadStackCount = 0
            indicator?.hide()
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else
            loadStackCount -= 1
    }

    fun resetLoadStack() {
        loadStackCount = 0
    }

    fun showDialog(
        title: String? = null,
        message: String? = null,
        positiveButtonText: String,
        positiveButtonAction: (() -> Unit)? = null,
        negativeButtonText: String? = null,
        negativeButtonAction: (() -> Unit)? = null,
        neutralButtonText: String? = null,
        neutralButtonAction: (() -> Unit)? = null,
        cancellable: Boolean? = true
    ) {
        val builder = MaterialAlertDialogBuilder(this)
        title?.let {
            builder.setTitle(it)
        }
        message?.let {
            builder.setMessage(it)
        }
        builder.setPositiveButton(
            positiveButtonText
        ) { _, _ -> positiveButtonAction?.invoke() }
        negativeButtonText?.let {
            builder.setNegativeButton(it) { _, _ -> negativeButtonAction?.invoke() }
        }
        neutralButtonText?.let {
            builder.setNeutralButton(it) { _, _ -> neutralButtonAction?.invoke() }
        }
        builder.setCancelable(cancellable == true)
        builder.show()
    }

    fun checkAndRequestPermission(permission: Permissions): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission.string
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    permission.string
                )
            ) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(permission.msgHeader)
                    .setMessage(permission.msgText)
                    .setNegativeButton(getString(R.string.general_cancel)) { _, _ ->
                    }
                    .setPositiveButton(getString(R.string.general_allow)) { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(permission.string),
                            permission.code
                        )
                    }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission.string),
                    permission.code
                )

            }
        } else {
            return true
        }
        return false
    }

}