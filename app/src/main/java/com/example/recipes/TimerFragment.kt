package com.example.recipes

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible

class TimerFragment() : Fragment(), View.OnClickListener {

    private var duration: Long = 0
    private var seconds: Int = duration.toInt()
    private var running: Boolean = false
    private var wasRunning: Boolean = false
    private var layout: View? = null
    private lateinit var vibrator: Vibrator

    private lateinit var ringtone: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            duration = savedInstanceState.getLong("duration")
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }

        val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, alarmSoundUri)
        vibrator = getSystemService(requireContext(), Vibrator::class.java)!!
        onToggle()
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone.stop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_timer, container, false)
        runTimer(layout)
        this.layout = layout

        val startButton: Button = layout.findViewById(R.id.start_button)
        startButton.setOnClickListener(this)
        val stopButton: Button = layout.findViewById(R.id.stop_button)
        stopButton.setOnClickListener(this)
        val resetButton: Button = layout.findViewById(R.id.reset_button)
        resetButton.setOnClickListener(this)
        val setButton: Button = layout.findViewById(R.id.set_button)
        setButton.setOnClickListener(this)
        val toggleTimerOption: Button = layout.findViewById(R.id.toggle_timer_option)
        toggleTimerOption.setOnClickListener(this)
        val alarmStopButton: Button = layout.findViewById(R.id.alarm_stop_button)
        alarmStopButton.setOnClickListener(this)

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

    private fun onClickSet() {
        running = false

        val inputS: TextView? = layout?.findViewById(R.id.input_s)
        val inputM: TextView? = layout?.findViewById(R.id.input_m)
        val inputH: TextView? = layout?.findViewById(R.id.input_h)
        var s: Long? = inputS?.text.toString().toLongOrNull()
        var m: Long? = inputM?.text.toString().toLongOrNull()
        var h: Long? = inputH?.text.toString().toLongOrNull()

        if (s == null)
            s = 0
        if (m == null)
            m = 0
        if (h == null)
            h = 0

        seconds = (s + 60 * m + 3600 * h).toInt()
    }

    private fun onToggle() {
        val toggleButton: ToggleButton? = layout?.findViewById(R.id.toggle_timer_option)

        if (toggleButton != null) {
            val timerInputLayout: LinearLayout? = layout?.findViewById(R.id.timer_input_layout)
            val setButton: Button? = layout?.findViewById(R.id.set_button)

            if (toggleButton.isChecked) {
                timerInputLayout?.isVisible = true
                setButton?.isVisible = true
            }
            else {
                timerInputLayout?.isVisible = false
                setButton?.isVisible = false
            }
        }
    }

    private fun startRingtone() {
        ringtone.play()
    }

    private fun startVibrator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 1000, 500), -1))
        } else {
            vibrator.vibrate(longArrayOf(0, 1000, 500), -1)
        }
    }

    private fun startAlarm() {
        startRingtone()
        startVibrator()
    }

    private fun stopAlarm() {
        ringtone.stop()
        vibrator.cancel()
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

                if (running && seconds == 0) {
                    running = false
                    startAlarm()
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
            R.id.set_button -> onClickSet()
            R.id.toggle_timer_option -> onToggle()
            R.id.alarm_stop_button -> stopAlarm()
        }
    }
}