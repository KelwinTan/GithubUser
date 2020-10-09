package com.example.dicodinguserapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class ReminderPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var REMINDER: String
    private lateinit var alarmReceiver: AlarmReceiver

    private lateinit var reminderPreference: SwitchPreference

    companion object {
        private const val DEFAULT_VALUE = "Tidak Ada"
    }

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }


    private fun init() {
        alarmReceiver = AlarmReceiver()
        REMINDER = resources.getString(R.string.key_reminder_setting_preference)
        reminderPreference = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
        if(reminderPreference.isChecked){
            context?.let { alarmReceiver.setRepeatingAlarm(it, AlarmReceiver.TYPE_REPEATING, "9:00", "Yuk balik lagi ke aplikasinya, udh jam 9 pagi, saatnya aktivitas :)") }
        }
    }

    private fun setSummaries(){
        val sh = preferenceManager.sharedPreferences
        reminderPreference.switchTextOn = sh.getBoolean(REMINDER, false).toString()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if(key == REMINDER){
            reminderPreference.isChecked = sharedPreferences.getBoolean(REMINDER, false)
            var msg = ""
            if(!reminderPreference.isChecked){
                msg = "Reminder at 9:00 AM disabled."
                context?.let { alarmReceiver.cancelAlarm(it, AlarmReceiver.TYPE_REPEATING) }
            }else {
                msg = "Reminder at 9:00 AM enabled."
                context?.let { alarmReceiver.setRepeatingAlarm(it, AlarmReceiver.TYPE_REPEATING, "9:00", "Yuk balik lagi ke aplikasinya, udh jam 9 pagi, saatnya aktivitas :)") }
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

    }

}