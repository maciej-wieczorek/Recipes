package com.example.recipes

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class TimerFragment() : Fragment(), View.OnClickListener {

    private var duration: Long = 0
    private var seconds: Int = duration.toInt()
    private var running: Boolean = false
    private var wasRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            duration = savedInstanceState.getLong("duration")
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_timer, container, false)
        runTimer(layout)

        val startButton: Button = layout.findViewById(R.id.start_button)
        startButton.setOnClickListener(this)
        val stopButton: Button = layout.findViewById(R.id.stop_button)
        stopButton.setOnClickListener(this)
        val resetButton: Button = layout.findViewById(R.id.reset_button)
        resetButton.setOnClickListener(this)

        return layout
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("duration", duration)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
    }

    fun setDuration(d: Long) {
        duration = d
        seconds = duration.toInt()
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    override fun onResume() {
        super.onResume()
        if (wasRunning) {
            running = true
        }
    }

    private fun onClickStart() {
        running = true
    }

    private fun onClickStop() {
        running = false
    }

    private fun onClickReset() {
        running = false
        seconds = duration.toInt()
    }

    private fun alarm() {
        // TODO
    }

    private fun runTimer(view: View) {
        val timeView: TextView = view.findViewById(R.id.time_view)
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val hours: Int = seconds / 3600
                val minutes: Int = (seconds % 3600) / 60
                val secs: Int = seconds % 60
                val time: String = String.format("%d:%02d:%02d", hours, minutes, secs)
                timeView.text = time

                if (seconds == 0) {
                    alarm()
                }

                if (running) {
                    seconds--
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start_button -> onClickStart()
            R.id.stop_button -> onClickStop()
            R.id.reset_button -> onClickReset()
        }
    }
}