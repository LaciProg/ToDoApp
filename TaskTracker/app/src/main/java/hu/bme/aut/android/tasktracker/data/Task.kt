package hu.bme.aut.android.tasktracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) var taskId: Long?,
    @ColumnInfo(name =  "title") var title: String,
    @ColumnInfo(name =  "subject") var subject: String,
    @ColumnInfo(name =  "description") var description: String,
    @ColumnInfo(name =  "deadline") var deadline: Long,
    @ColumnInfo(name =  "done") var done: Long
)
