package com.carlos.myapps.petsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val updateBtn = findViewById<Button>(R.id.update_btn)
        updateBtn.setOnClickListener {
            update()
        }

        val addBtn = findViewById<Button>(R.id.add_task_btn)
        addBtn.setOnClickListener {
            add()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.task_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun add() {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    private fun update() {
        val db = Firebase.firestore

        db.collection("checklist")
            .get()
            .addOnSuccessListener { result ->
                val taskList: MutableList<Task> = mutableListOf()

                for (document in result) {
                    val task = Task(
                        document.id,
                        document["name"].toString(),
                        document["description"].toString(),
                        document["complete"] as Boolean
                    )
                    taskList.add(task)
                }
                val recyclerView = findViewById<RecyclerView>(R.id.task_list)
                val adapter = TaskAdapter(taskList)
                adapter.checkListener = { task, checked ->
                    db.collection("checklist").document(task.id)
                        .set(
                            hashMapOf(
                                "complete" to checked
                            ), SetOptions.merge()
                        )
                }
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }
}