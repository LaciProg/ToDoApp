package hu.bme.aut.android.tasktracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.tasktracker.adapter.TaskAdapter
import hu.bme.aut.android.tasktracker.data.Task
import hu.bme.aut.android.tasktracker.data.TaskDataBase
import hu.bme.aut.android.tasktracker.databinding.FragmentListBinding
import java.util.Calendar
import java.util.Collections
import kotlin.concurrent.thread


class ListFragment : Fragment(), TaskAdapter.TaskClickListener{
    private lateinit var binding: FragmentListBinding

    companion object {
         private lateinit var database: TaskDataBase
         private lateinit var adapter: TaskAdapter
         private const val day = 86400000L
    }
    private fun MenuEventHandler(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list_all_done -> {
                item.isChecked = true
                Log.i("Menu", "All done")
                loadItemsInBackground()
                true
            }
            R.id.list_done -> {
                item.isChecked = true
                Log.i("Menu", "Done")
                loadDoneItemsInBackground()
                true
            }
            R.id.list_pending -> {
                item.isChecked = true
                loadPendingItemsInBackground()
                Log.i("Menu", "Pending")
                true
            }
            R.id.days7Left -> {
                item.isChecked = true

                loadXDayLeftItemsInBackground(7)
                Log.i("Menu", "7 day")
                true
            }
            R.id.days3Left -> {
                item.isChecked = true

                loadXDayLeftItemsInBackground(3)
                Log.i("Menu", "3 day")
                true
            }
            R.id.days2Left -> {
                item.isChecked = true

                loadXDayLeftItemsInBackground(2)
                Log.i("Menu", "2 day")
                true
            }
            R.id.days1Left -> {
                item.isChecked = true

                loadXDayLeftItemsInBackground(1)
                Log.i("Menu", "1 day")
                true
            }
            R.id.deleteAll -> {
                item.isChecked = true
                deleteAllItemsInBackground()
                Log.i("Menu", "Delete all")
                true
            }
            else -> false
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onItemChanged(item: Task) {
        thread {
            database.taskDao().update(item)
            Log.d("ListADD", "Task update was successful")
        }
    }

    override fun onItemDeleted(item: Task) {
        thread {
            database.taskDao().deleteTask(item)
            Log.d("MainActivity", "Task removal was successful")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.inflateMenu(R.menu.list_menu)
        database = TaskDataBase.getInstance(requireContext())

        binding.toolbar.setOnMenuItemClickListener { item ->
            MenuEventHandler(item)
        }
        initRecyclerView()
    }


    private fun initRecyclerView() {
        adapter = TaskAdapter(this)
        binding.rvList.layoutManager = LinearLayoutManager(context)
        binding.rvList.adapter = adapter
        binding.rvList.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.taskDao().getAllTasks().sortedBy { it.deadline }
            activity?.runOnUiThread {
                adapter.update(items)
            }
        }
    }

    private fun loadDoneItemsInBackground() {
        thread {
            val items = database.taskDao().getTasksReady().sortedBy { it.deadline }
            activity?.runOnUiThread {
                adapter.update(items)
            }
        }
    }

    private fun loadPendingItemsInBackground() {
        thread {
            val items = database.taskDao().getTasksLeft().sortedBy { it.deadline }
            activity?.runOnUiThread {
                adapter.update(items)
            }
        }
    }

    private fun deleteAllItemsInBackground() {
        thread {
            val items = database.taskDao().getAllTasks()
            database.taskDao().deleteAllTasks(*items.toTypedArray())
            activity?.runOnUiThread {
                adapter.update(Collections.emptyList())
            }
        }
    }

    private fun loadXDayLeftItemsInBackground(x : Int) {
        thread {
            val items = database.taskDao().getSpecificTasks(day * x + Calendar.getInstance().timeInMillis).sortedBy { it.deadline }
            activity?.runOnUiThread {
                adapter.update(items)
            }
        }
    }

}