package hu.bme.aut.android.tasktracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDAO {

    @Query("SELECT * FROM task WHERE subject = :subject")
    fun getSpecificTasks(subject: String): List<Task>

    @Query("SELECT * FROM task WHERE deadline <= :time")
    fun getSpecificTasks(time: Long): List<Task>

    @Query("SELECT * FROM task")
    fun getAllTasks(): List<Task>

    @Query("""SELECT * FROM task WHERE done="0"""")
    fun getTasksLeft(): List<Task>

    @Query("""SELECT * FROM task WHERE done="1"""")
    fun getTasksReady(): List<Task>

    @Insert
    fun insertTask(tasks: Task) : Long
    @Delete
    fun deleteTask(task: Task)

    @Delete
    fun deleteAllTasks(vararg tasks: Task)

    @Update
    fun update(task: Task)
}