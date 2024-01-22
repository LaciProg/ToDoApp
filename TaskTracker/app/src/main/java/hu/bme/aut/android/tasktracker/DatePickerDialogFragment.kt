package hu.bme.aut.android.tasktracker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import java.io.Serializable
import java.util.Calendar

class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener{

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val picker = DatePickerDialog(requireContext(), this, year, month, day)
        picker.datePicker.minDate = c.timeInMillis
        return picker
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val result = DatePickerResult(year, month, dayOfMonth)
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(NewFragment.DATE_SELECTED_KEY, result)
    }

    data class DatePickerResult(
        val year: Int,
        val month: Int,
        val dayOfMonth: Int,
    ) : Serializable
}