package com.ell.lyawatch.home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import com.ell.lyawatch.databinding.ActivityHomeBinding
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import java.util.*
import java.util.concurrent.ExecutionException


class HomeActivity : Activity() {

    private lateinit var binding: ActivityHomeBinding

    var date = Date()
    var cal: Calendar = Calendar.getInstance()

    var hours = cal[Calendar.HOUR_OF_DAY]
    var minutes = cal[Calendar.MINUTE]
    private val secs = cal[Calendar.SECOND]
    private val ms = cal[Calendar.MILLISECOND]

    var dayNumber = DateFormat.format("dd", date) as String
    var monthNumber = DateFormat.format("MM", date) as String
    var year = DateFormat.format("yyyy", date) as String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val timer = Timer(false)
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                exportData()
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 10000)

    }

    private fun exportData() {
        val sentData = "Data: " + getTimestampedDate()
        binding.text.text = sentData

        val dataPath = "/lya_path"
        Log.d("LYA", "Modulo Watch 01: $sentData")
        SendMessage(this, dataPath, sentData).start()
    }

    private fun getTimestampedDate(): String {
        val date = "$dayNumber-$monthNumber-$year"
        val time = getTime()
        return "$date $time"
    }

    private fun getTime(): String {
        val tHour: String = if (hours < 10) {
            "0$hours"
        } else {
            hours.toString()
        }
        val tMins: String = if (minutes < 10) {
            "0$minutes"
        } else {
            minutes.toString()
        }
        val tSecs: String = if (secs < 10) {
            "0$secs"
        } else {
            secs.toString()
        }
        val tMs: String = if (ms < 100) {
            if (ms < 10) {
                "00$ms"
            } else {
                "0$ms"
            }
        } else {
            ms.toString()
        }
        return "$tHour:$tMins:$tSecs" // + "." + tMs <-- to add mS
    }
}

internal class SendMessage (
    private val context: Context,
    private var path: String,
    private var message: String
    ) : Thread() {
    override fun run() {
        //Retrieve the connected devices//
        val nodeListTask: Task<List<Node>> =
            Wearable.getNodeClient(context).connectedNodes
        try {
            //Block on a task and get the result synchronously//
            val nodes: List<Node> = Tasks.await<List<Node>>(nodeListTask)
            for (node in nodes) {
                //Send the message///
                val sendMessageTask: Task<Int> = Wearable.getMessageClient(context)
                    .sendMessage(node.id, path, message.toByteArray())
                Log.d("LYA", "Modulo Watch 02: $message")
                try {
                    val result = Tasks.await<Int>(sendMessageTask)
                    //Handle the errors//
                } catch (exception: ExecutionException) {
                //TO DO//
                } catch (exception: InterruptedException) {
                //TO DO//
                }
            }
        } catch (exception: ExecutionException) {
        //TO DO//
        } catch (exception: InterruptedException) {
        //TO DO//
        }
    }
}