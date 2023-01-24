package com.ell.lyawear.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ell.lyawatch.R
import com.ell.lyawatch.databinding.ActivityHomeBinding


open class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var myHandler: Handler? = null
    var textview: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textview = findViewById(R.id.text)

        myHandler = Handler { message ->
            val stuff: Bundle = message.data
            stuff.getString("messageText")?.let { text -> messageText(text) }
            return@Handler true
        }

        val messageFilter = IntentFilter(Intent.ACTION_SEND)
        val messageReceiver = Receiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter)
    }

    open fun messageText(newInfo: String) {
        if (newInfo.compareTo("") != 0) {
            binding.text.append("\n" + newInfo)
        }
    }
}

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //Upon receiving each message from the wearable, display the following text//
        val message = intent?.getStringExtra("message").toString()
        HomeActivity().textview?.text = message
    }
}