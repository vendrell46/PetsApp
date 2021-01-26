package com.carlos.myapps.petsapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val addTaskBtn = findViewById<Button>(R.id.add_btn)
        addTaskBtn.setOnClickListener {
            taskAddedMessage()
        }
    }

    private fun taskAddedMessage() {
        val taskNameTitle = findViewById<EditText>(R.id.task_title)
        Toast.makeText(this, taskNameTitle.text, Toast.LENGTH_SHORT).show()
    }
}