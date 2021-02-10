package com.carlos.myapps.petsapp

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity() {

    companion object {
        const val TASK_KEY = "TASK_KEY"
    }

    private lateinit var taskNameTitle: EditText
    private lateinit var descriptionText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        taskNameTitle = findViewById(R.id.task_title)
        descriptionText = findViewById(R.id.task_description)

        val addTaskBtn = findViewById<Button>(R.id.add_btn)
        addTaskBtn.setOnClickListener {
            getDataFromTask()
            finish()
        }
    }

    private fun getDataFromTask() {
        val taskData = Task(
            id = "0",
            name = taskNameTitle.text.toString(),
            description = descriptionText.text.toString(),
            complete = false
        )
        intent.putExtra(TASK_KEY, taskData)
        setResult(Activity.RESULT_OK, intent)
    }
}