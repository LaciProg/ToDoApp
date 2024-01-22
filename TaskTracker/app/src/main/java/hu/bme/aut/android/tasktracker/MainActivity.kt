package hu.bme.aut.android.tasktracker

import android.content.ContentValues
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.SubMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import hu.bme.aut.android.tasktracker.data.Task
import hu.bme.aut.android.tasktracker.databinding.ActivityMainBinding
import hu.bme.aut.android.tasktracker.databinding.FragmentListBinding
import hu.bme.aut.android.tasktracker.databinding.FragmentMenuBinding
import java.util.TimeZone
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var menuBinding: FragmentListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        menuBinding = FragmentListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun exportToCalendar(taskItem: Task) {
        requestNeededPermission()
        writer(taskItem)
    }

    private fun writer(taskItem: Task) {
        try {
            val values = ContentValues()

            values.put(CalendarContract.Events.DTSTART, taskItem.deadline)
            values.put(CalendarContract.Events.DTEND, taskItem.deadline)
            values.put(CalendarContract.Events.TITLE, "${taskItem.subject}  ${taskItem.title}")
            values.put(CalendarContract.Events.DESCRIPTION, taskItem.description)
            values.put(CalendarContract.Events.CALENDAR_ID, 1)
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)

            val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }


    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.WRITE_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.WRITE_CALENDAR),
                101
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this@MainActivity, "Permissions granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Permissions are NOT granted", Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

}