package com.example.recipes

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible

class TimerFragment() : Fragment(), View.OnClickListener {

    private var duration: Long = 0
    private var steps = arrayListOf<Int>(0)
    private var currentStep = 0
    private var seconds: Int = duration.toInt()
    private var running: Boolean = false
    private var wasRunning: Boolean = false
    private var setToggled: Boolean = false
    private var vibrationToggled: Boolean = false
    private var ringtoneToggled: Boolean = false
    private lateinit var layout: View
    private lateinit var vibrator: Vibrator
    private lateinit var ringtone: Ringtone

    private val vibrationPattern = longArrayOf(0, 1000, 500) // wait 0ms, vibrate for 1000ms, wait 500ms
    private val vibrationHandler = Handler(Looper.getMainLooper())
    private val vibrationRunnable = object : Runnable {
        override fun run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, 0))
            } else {
                vibrator.vibrate(vibrationPattern, 0)
            }

            vibrationHandler.postDelayed(this, vibrationPattern.sum()) // pattern.sum() = full vibration cycle
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            duration = savedInstanceState.getLong("duration")
            steps = savedInstanceState.getIntegerArrayList("steps")!!
            currentStep = savedInstanceState.getInt("currentStep")
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
            setToggled = savedInstanceState.getBoolean("setToggled")
            vibrationToggled = savedInstanceState.getBoolean("vibrationToggled")
            ringtoneToggled = savedInstanceState.getBoolean("ringtoneToggled")
        }

        val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, alarmSoundUri)
        vibrator = getSystemService(requireContext(), Vibrator::class.java)!!
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        val toggleTimerOption: ToggleButton = layout.findViewById(R.id.toggle_timer_option)
        toggleTimerOption.isChecked = setToggled
        toggleTimerOption.setOnClickListener(this)
        val toggleVibratorOption: ToggleButton = layout.findViewById(R.id.toggle_timer_vibration)
        toggleVibratorOption.isChecked = vibrationToggled
        toggleVibratorOption.setOnClickListener(this)
        val toggleRingtoneOption: ToggleButton = layout.findViewById(R.id.toggle_timer_ringtone)
        toggleRingtoneOption.isChecked = ringtoneToggled
        toggleRingtoneOption.setOnClickListener(this)
        val alarmStopButton: Button = layout.findViewById(R.id.alarm_stop_button)
        alarmStopButton.setOnClickListener(this)
        val previousStepButton: ImageButton = layout.findViewById(R.id.previous_timer_button)
        previousStepButton.setOnClickListener(this)
        val nextStepButton: ImageButton = layout.findViewById(R.id.next_timer_button)
        nextStepButton.setOnClickListener(this)

        updateVisibility()

        return layout
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("duration", duration)
        outState.putIntegerArrayList("steps", steps)
        outState.putInt("currentStep", currentStep)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
        outState.putBoolean("setToggled", setToggled)
        outState.putBoolean("vibrationToggled", vibrationToggled)
        outState.putBoolean("ringtoneToggled", ringtoneToggled)
    }

    override fun onStart() {
        super.onStart()
        updateVisibility()
    }

    fun setSteps(s: ArrayList<Int>) {
        steps = s
        setDuration(s[0].toLong())
    }

    private fun updateProgress() {
        val progressTextView: TextView = layout.findViewById(R.id.progress_text_view)
        progressTextView.text = "${currentStep+1}/${steps.size}"
    }

    private fun changeStep(step: Int) {
        running = false
        currentStep = step
        setDuration(steps[currentStep].toLong())
        updateTimeView()
        updateProgress()
    }

    private fun nextStep() {
        if (currentStep+1 < steps.size) {
            changeStep(++currentStep)
        }
    }

    private fun previousStep() {
        if (currentStep-1 >= 0) {
            changeStep(--currentStep)
        }
    }

    private fun setDuration(d: Long) {
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
        updateTimeView()
    }

    private fun onClickSet() {
        running = false

        val inputS: TextView = layout.findViewById(R.id.input_s)
        val inputM: TextView = layout.findViewById(R.id.input_m)
        val inputH: TextView = layout.findViewById(R.id.input_h)
        var s: Long? = inputS.text.toString().toLongOrNull()
        var m: Long? = inputM.text.toString().toLongOrNull()
        var h: Long? = inputH.text.toString().toLongOrNull()

        if (s == null)
            s = 0
        if (m == null)
            m = 0
        if (h == null)
            h = 0

        setDuration(s + 60 * m + 3600 * h)
        updateTimeView()
    }

    private fun updateVisibility() {
        val toggleButton: ToggleButton = layout.findViewById(R.id.toggle_timer_option)
        val timerInputLayout: LinearLayout = layout.findViewById(R.id.timer_input_layout)
        val setButton: Button = layout.findViewById(R.id.set_button)
        val progressLayout: LinearLayout = layout.findViewById(R.id.progress_layout)

        if (toggleButton.isChecked) {
            setToggled = true
            timerInputLayout.isVisible = true
            setButton.isVisible = true
            progressLayout.isVisible = false
        }
        else {
            setToggled = false
            timerInputLayout.isVisible = false
            setButton.isVisible = false
            progressLayout.isVisible = true
        }

        updateTimeView()
        updateProgress()
    }

    private fun onSetToggle() {
        updateVisibility()
        running = false

        if (!setToggled) {
            changeStep(currentStep)
        }
    }

    private fun onVibrationToggle() {
        val toggleButton: ToggleButton = layout.findViewById(R.id.toggle_timer_vibration)
        vibrationToggled = toggleButton.isChecked
    }

    private fun onRingtoneToggle() {
        val toggleButton: ToggleButton = layout.findViewById(R.id.toggle_timer_ringtone)
        ringtoneToggled = toggleButton.isChecked
    }

    private fun startRingtone() {
        ringtone.play()
    }

    private fun startVibrator() {
        vibrationHandler.post(vibrationRunnable)
    }

    private fun startAlarm() {
        val stopAlarmLayout: LinearLayout = layout.findViewById(R.id.alarm_stop_button_layout)
        stopAlarmLayout.isVisible = true

        if (ringtoneToggled)
            startRingtone()
        if (vibrationToggled)
            startVibrator()
    }

    private fun stopAlarm() {
        ringtone.stop()
        vibrator.cancel()
        vibrationHandler.removeCallbacks(vibrationRunnable)

        val stopAlarmLayout: LinearLayout = layout.findViewById(R.id.alarm_stop_button_layout)
        stopAlarmLayout.isVisible = false
        nextStep()
    }

    private fun updateTimeView() {
        val hours: Int = seconds / 3600
        val minutes: Int = (seconds % 3600) / 60
        val secs: Int = seconds % 60
        val time = String.format("%d:%02d:%02d", hours, minutes, secs)

        val timeView: TextView = layout.findViewById(R.id.time_view)
        timeView.text = time
    }

    private fun runTimer(view: View) {
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                updateTimeView()

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
            R.id.toggle_timer_option -> onSetToggle()
            R.id.toggle_timer_vibration -> onVibrationToggle()
            R.id.toggle_timer_ringtone -> onRingtoneToggle()
            R.id.alarm_stop_button -> stopAlarm()
            R.id.previous_timer_button -> previousStep()
            R.id.next_timer_button -> nextStep()
        }
    }
}