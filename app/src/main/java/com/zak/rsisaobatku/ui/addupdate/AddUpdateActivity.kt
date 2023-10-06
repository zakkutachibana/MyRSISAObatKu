package com.zak.rsisaobatku.ui.addupdate

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.zak.rsisaobatku.R
import com.zak.rsisaobatku.data.local.Medicine
import com.zak.rsisaobatku.databinding.ActivityAddUpdateBinding
import com.zak.rsisaobatku.notification.ReminderReceiver
import com.zak.rsisaobatku.util.DateHelper
import com.zak.rsisaobatku.util.TimePickerFragment
import com.zak.rsisaobatku.util.ViewModelFactory
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.SimpleDateFormat
import java.util.*

class AddUpdateActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private var isEdit = false
    private var medicine: Medicine? = null

    private lateinit var addUpdateViewModel: AddUpdateViewModel
    private var _activityAddUpdateBinding: ActivityAddUpdateBinding? = null
    private val binding get() = _activityAddUpdateBinding

    private var futureId: Int = 0
    private lateinit var reminderReceiver: ReminderReceiver

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityAddUpdateBinding = ActivityAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        addUpdateViewModel = obtainViewModel(this@AddUpdateActivity)

        medicine = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(EXTRA_MEDICINE, Medicine::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_MEDICINE)
        }

        if (medicine != null) {
            isEdit = true
        } else {
            medicine = Medicine()
        }

        reminderReceiver = ReminderReceiver()

        addUpdateViewModel.getFutureId().observe(this) {
            if (it != null) {
                getFutureId(it)
            }
        }

        fillDropDown()
        setView()
        setAction()

    }

    private fun fillDropDown() {
        val actType = binding?.actType
        val actFreq = binding?.actFrequency
        val actInstruct = binding?.actInstruction

        val listType = resources.getStringArray(R.array.listType)
        val listFreq = resources.getStringArray(R.array.listFreq)
        val listInstruct = resources.getStringArray(R.array.listInstruct)

        val typeAdapter = ArrayAdapter(this, R.layout.drop_down_items, listType)
        val freqAdapter = ArrayAdapter(this, R.layout.drop_down_items, listFreq)
        val instructAdapter = ArrayAdapter(this, R.layout.drop_down_items, listInstruct)

        actType?.setAdapter(typeAdapter)
        actFreq?.setAdapter(freqAdapter)
        actInstruct?.setAdapter(instructAdapter)
    }

    private fun getFutureId(id: Int) {
        futureId = id
    }

    private fun setAction() {

        binding?.btnAdd?.setOnClickListener {
            val name = binding?.edMedicineName?.text.toString().trim()
            val medication = binding?.edMedication?.text.toString().trim()
            val type = binding?.actType?.text.toString().trim()
            val dosage = binding?.edDosage?.text.toString().trim()
            val instruction = binding?.actInstruction?.text.toString().trim()
            val frequency = binding?.actFrequency?.text.toString().trim()

            Toast.makeText(this, "id nya " + futureId, Toast.LENGTH_SHORT).show()
            when {
                name.isEmpty() -> binding?.layoutMedicineName?.error = getString(R.string.empty)
                medication.isEmpty() -> binding?.layoutMedication?.error = getString(R.string.empty)
                type.isEmpty() -> binding?.layoutType?.error = getString(R.string.empty)
                dosage.isEmpty() -> binding?.layoutDosage?.error = getString(R.string.empty)
                instruction.isEmpty() -> binding?.layoutInstruction?.error = getString(R.string.empty)
                frequency.isEmpty() -> binding?.layoutFrequency?.error = getString(R.string.empty)

                else -> {
                    medicine.let { medicine ->
                        medicine?.name = name
                        medicine?.medication = medication
                        medicine?.dosage = dosage
                        medicine?.type = type
                        medicine?.instruction = instruction
                        medicine?.frequency = frequency
                    }
                    if (isEdit) {
                        addUpdateViewModel.update(medicine as Medicine)
                        showToast(getString(R.string.changed), 3)
                    } else {
                        medicine.let { medicine ->
                            medicine?.date = DateHelper.getCurrentDate()
                        }
                        addUpdateViewModel.insert(medicine as Medicine)
                        showToast(getString(R.string.added), 1)
                    }
                    finish()
                }
            }
            //TODO: Ambil ID dari Room +1
            val repeatTimeMorning = binding?.tvMorningTime?.text.toString()
            val repeatTimeNoon = binding?.tvNoonTime?.text.toString()
            val repeatTimeNight = binding?.tvNightTime?.text.toString()
            val repeatMessage = "Saatnya minum obat " + binding?.edMedicineName?.text.toString() + ". Semoga lekas sembuh, jangan lupa berdoa."


            when (frequency) {
                "3x sehari" -> {
                    reminderReceiver.setRepeatingAlarm(this, ReminderReceiver.TYPE_MORNING,
                        repeatTimeMorning, repeatMessage, ReminderReceiver.ID_MORNING)
                    reminderReceiver.setRepeatingAlarm(this, ReminderReceiver.TYPE_NOON,
                        repeatTimeNoon, repeatMessage, ReminderReceiver.ID_NOON)
                    reminderReceiver.setRepeatingAlarm(this, ReminderReceiver.TYPE_NIGHT,
                        repeatTimeNight, repeatMessage, ReminderReceiver.ID_NIGHT)
                    Toast.makeText(this, "Night Notif", Toast.LENGTH_SHORT).show()
                }
                "2x sehari" -> {
                    reminderReceiver.setRepeatingAlarm(this, ReminderReceiver.TYPE_MORNING,
                        repeatTimeMorning, repeatMessage, ReminderReceiver.ID_MORNING)
                    reminderReceiver.setRepeatingAlarm(this, ReminderReceiver.TYPE_NOON,
                        repeatTimeNoon, repeatMessage, ReminderReceiver.ID_NOON)
                    Toast.makeText(this, "Noon Notif", Toast.LENGTH_SHORT).show()
                }
                "1x sehari" -> {
                    reminderReceiver.setRepeatingAlarm(this, ReminderReceiver.TYPE_MORNING,
                        repeatTimeMorning, repeatMessage, ReminderReceiver.ID_NIGHT)
                    Toast.makeText(this, "Morning Notif", Toast.LENGTH_SHORT).show()
                }

            }

        }

        binding?.btnMorning?.setOnClickListener {
            val timePickerFragmentOne = TimePickerFragment()
            timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_MORNING)
        }

        binding?.btnNoon?.setOnClickListener {
            val timePickerFragmentOne = TimePickerFragment()
            timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_NOON)
        }

        binding?.btnNight?.setOnClickListener {
            val timePickerFragmentOne = TimePickerFragment()
            timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_NIGHT)
        }
    }

    private fun setView() {
        val actionBarTitle: String
        val btnTitle: String
        if (isEdit) {
            actionBarTitle = getString(R.string.change)
            btnTitle = getString(R.string.update)
            if (medicine != null) {
                medicine?.let { medicine ->
                    binding?.edMedicineName?.setText(medicine.name)
                    binding?.edMedication?.setText(medicine.medication)
                    binding?.edDosage?.setText(medicine.dosage)
                }
            }
        } else {
            actionBarTitle = getString(R.string.add)
            btnTitle = getString(R.string.save)
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.btnAdd?.text = btnTitle

        binding?.edMedication?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding?.layoutMedication?.error = null
            }
        })
        binding?.edDosage?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding?.layoutDosage?.error = null
            }
        })
        binding?.edMedicineName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding?.layoutMedicineName?.error = null
            }
        })
        binding?.actInstruction?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding?.layoutInstruction?.error = null
            }
        })
        binding?.actType?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding?.layoutType?.error = null
            }
        })
        binding?.actFrequency?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding?.layoutFrequency?.error = null
                when (s.toString()) {
                    "3x sehari" -> {
                        binding?.lineStart?.visibility = View.VISIBLE
                        binding?.cardRemindOne?.visibility = View.VISIBLE
                        binding?.cardRemindTwo?.visibility = View.VISIBLE
                        binding?.cardRemindThree?.visibility = View.VISIBLE
                        binding?.lineEnd?.visibility = View.VISIBLE
                    }
                    "2x sehari" -> {
                        binding?.cardRemindOne?.visibility = View.VISIBLE
                        binding?.cardRemindTwo?.visibility = View.VISIBLE
                        binding?.cardRemindThree?.visibility = View.GONE
                        binding?.lineEnd?.visibility = View.VISIBLE
                        binding?.lineStart?.visibility = View.VISIBLE
                    }
                    "1x sehari" -> {
                        binding?.cardRemindOne?.visibility = View.VISIBLE
                        binding?.cardRemindTwo?.visibility = View.GONE
                        binding?.cardRemindThree?.visibility = View.GONE
                        binding?.lineEnd?.visibility = View.VISIBLE
                        binding?.lineStart?.visibility = View.VISIBLE
                    }
                    else -> {
                        binding?.cardRemindOne?.visibility = View.GONE
                        binding?.cardRemindTwo?.visibility = View.GONE
                        binding?.cardRemindThree?.visibility = View.GONE
                        binding?.lineEnd?.visibility = View.VISIBLE
                        binding?.lineStart?.visibility = View.VISIBLE
                    }
                }
            }
        })

    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            TIME_PICKER_MORNING -> binding?.tvMorningTime?.text = dateFormat.format(calendar.time)
            TIME_PICKER_NOON -> binding?.tvNoonTime?.text = dateFormat.format(calendar.time)
            TIME_PICKER_NIGHT -> binding?.tvNightTime?.text = dateFormat.format(calendar.time)
            else -> {
            }
        }
    }

    private fun showToast(message: String, type: Int) {
        var title = ""
        var style = MotionToastStyle.WARNING
        when (type) {
            1 -> {
                title = "Tambah Obat"
                style = MotionToastStyle.SUCCESS
            }
            2 -> {
                title = "Hapus Obat"
                style = MotionToastStyle.DELETE
            }
            3 -> {
                title = "Update Obat"
                style = MotionToastStyle.INFO
            }
        }
        MotionToast.createColorToast(
            this,
            title,
            message,
            style,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, R.font.geomatrix_light))
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityAddUpdateBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): AddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[AddUpdateViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel)
            dialogMessage = getString(R.string.message_cancel)
        } else {
            dialogMessage = getString(R.string.message_delete)
            dialogTitle = getString(R.string.delete)
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (!isDialogClose) {
                    addUpdateViewModel.delete(medicine as Medicine)
                    showToast(getString(R.string.deleted), 2)
                }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        const val EXTRA_MEDICINE = "extra_medicine"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
        private const val TIME_PICKER_MORNING = "TimePickerMorning"
        private const val TIME_PICKER_NOON = "TimePickerNoon"
        private const val TIME_PICKER_NIGHT = "TimePickerNight"
    }

}






