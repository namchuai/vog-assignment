package com.example.fcmexample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.fcmexample.databinding.ActivitySendBinding
import com.google.android.material.snackbar.Snackbar

class SendActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendBinding
    private lateinit var viewModel: SendViewModel

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, SendActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[SendViewModel::class.java]

        binding = DataBindingUtil.setContentView(this, R.layout.activity_send)

        binding.apply {
            lifecycleOwner = this@SendActivity
            viewModel = this@SendActivity.viewModel
            onSendClick = View.OnClickListener { this@SendActivity.viewModel.sendNotification() }
        }

        viewModel.apply {
            sendNotificationMessage.observe(this@SendActivity, { message ->
                Snackbar.make(
                    binding.root,
                    message,
                    Snackbar.LENGTH_SHORT
                ).show()
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}