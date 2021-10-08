package com.example.fcmexample

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fcmexample.databinding.ActivityMainBinding
import com.example.fcmexample.db.FCMExampleDB
import com.example.fcmexample.utils.PREFS_NAME
import com.example.fcmexample.utils.TOKEN
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), HasDefaultViewModelProviderFactory {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: FCMViewModel by viewModels()
    private val clipboardManager by lazy { getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }
    private val sharedPreferences by lazy { getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(FCMViewModel::class.java)) {
                    return FCMViewModel(FCMExampleDB.getDatabase(this@MainActivity)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = viewModel
            copyClick = View.OnClickListener {
                clipboardManager.setPrimaryClip(
                    ClipData.newPlainText(
                        "token", sharedPreferences.getString(
                            TOKEN, ""
                        )
                    )
                )
                Snackbar.make(binding.root, "Copied to clipboard", Snackbar.LENGTH_SHORT).show()
            }
            onSendActivityClick =
                View.OnClickListener { SendActivity.start(this@MainActivity) }
            recycler.adapter = NotificationListAdapter(ItemClickListener {

            })
        }
    }
}