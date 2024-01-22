package hu.bme.aut.android.tasktracker.adapter

import android.content.ContentValues
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.tasktracker.MainActivity
import hu.bme.aut.android.tasktracker.data.Task
import hu.bme.aut.android.tasktracker.data.TaskDataBase
import hu.bme.aut.android.tasktracker.databinding.TaskRowBinding
import java.text.SimpleDateFormat
import java.util.TimeZone
import kotlin.concurrent.thread

class TaskAdapter(private val listener: TaskClickListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val items = mutableListOf<Task>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskViewHolder(
        TaskRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskItem = items[position]

        holder.binding.tvSubject.text = taskItem.subject
        holder.binding.tvTitle.text = taskItem.title
        holder.binding.tvDeadline.text = dateToString(taskItem.deadline)
        holder.binding.cbDone.isChecked = taskItem.done == 1L
        holder.binding.tvDescription.text = taskItem.description

        Log.i("Time", (taskItem.deadline).toString()+ " calendar")
        Log.i("Time", dateToString(taskItem.deadline)+ " calendar")
        Log.i("Time", (System.currentTimeMillis()).toString()+ " now")
        Log.i("Time", dateToString(System.currentTimeMillis())+ " now")

        holder.binding.cbDone.setOnClickListener {
            taskItem.done = if (taskItem.done == 1L) 0 else 1
            listener.onItemChanged(taskItem)
        }

        holder.binding.btnDelete.setOnClickListener {
            val item = items[position]
            delete(position)
            listener.onItemDeleted(item)
        }


        holder.binding.btnExport.setOnClickListener {
            (holder.itemView.context as MainActivity).exportToCalendar(taskItem)
        }
    }
    private fun dateToString(date: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return  simpleDateFormat.format(date)
    }

    fun addItem(item: Task) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(items: List<Task>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    private fun delete(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    override fun getItemCount(): Int = items.size

    interface TaskClickListener {
        fun onItemChanged(item: Task)
        fun onItemDeleted(item: Task)
    }

    inner class TaskViewHolder(val binding: TaskRowBinding) : RecyclerView.ViewHolder(binding.root)
}