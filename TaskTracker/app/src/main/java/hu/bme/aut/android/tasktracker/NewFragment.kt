package hu.bme.aut.android.tasktracker

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.tasktracker.data.Task
import hu.bme.aut.android.tasktracker.data.TaskDataBase
import hu.bme.aut.android.tasktracker.databinding.FragmentNewBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.concurrent.thread


class NewFragment : Fragment() {

    private lateinit var binding : FragmentNewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDeadline.setOnClickListener {
            findNavController().navigate(R.id.action_newFragment_to_datePickerDialogFragment)
        }

        binding.btnSubmit.setOnClickListener{
            if(binding.subject.text.isEmpty() || binding.deadline.text.isEmpty() || binding.title.text.isEmpty())
                return@setOnClickListener

            val date = binding.deadline.text.toString()
            Log.i("Time", date + " new")

            val task = Task(null,
                binding.title.text.toString(),
                binding.subject.text.toString(),
                binding.description.text.toString(),
                getMilliFromDate(date),
                0)

            thread {
                context?.let {
                    val insertID = TaskDataBase.getInstance(it).taskDao().insertTask(task)
                    task.taskId = insertID

                }
                    activity?.runOnUiThread {
                        Snackbar.make(binding.root, "Task added", Snackbar.LENGTH_LONG)
                            .setAction("Undo", undo(task)).setTextColor(Color.RED).show()
                    }
            }

            binding.title.text.clear()
            binding.subject.text.clear()
            binding.description.text.clear()
            binding.deadline.text = ""

        }

        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<DatePickerDialogFragment.DatePickerResult>(DATE_SELECTED_KEY)
            ?.observe(viewLifecycleOwner) {
                Log.i("Time", it.toString() + " new")
                if((it.month + 1) < 10)
                    binding.deadline.text = "${it.year}-0${it.month+1}-${it.dayOfMonth}"
                else
                    binding.deadline.text = "${it.year}-${it.month+1}-${it.dayOfMonth}"
            }

    }

    private fun undo( task: Task) : OnClickListener {
        return OnClickListener {
            thread {
                context?.let { it1 -> TaskDataBase.getInstance(it1).taskDao().deleteTask(task) }
            }
        }
    }

    private fun getMilliFromDate(dateFormat: String?): Long {
        var date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time
    }

    companion object {
        const val DATE_SELECTED_KEY = "date_selected"
    }

}