package com.carlos.myapps.petsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class TaskListFragment : Fragment() {

    companion object {
        private const val ADD_REQUEST_CODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val updateBtn = view.findViewById<Button>(R.id.update_btn)
        updateBtn.setOnClickListener {
            update()
        }

        val addBtn = view.findViewById<Button>(R.id.add_task_btn)
        addBtn.setOnClickListener {
            add()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.task_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        update()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val taskData = data?.extras?.getParcelable<Task>(AddActivity.TASK_KEY)
            addTaskToDb(taskData)
        }
    }

    private fun addTaskToDb(taskData: Task?) {
        val db = Firebase.firestore

        val data = hashMapOf(
            "name" to taskData?.name,
            "description" to taskData?.description,
            "complete" to taskData?.complete
        )

        db.collection("checklist")
            .add(data)
            .addOnSuccessListener {
                taskAddedMessage(taskData)
                update()
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    private fun add() {
        val intent = Intent(requireContext(), AddActivity::class.java)
        startActivityForResult(intent, ADD_REQUEST_CODE)
    }

    private fun taskAddedMessage(taskData: Task?) {
        Toast.makeText(requireContext(), taskData?.name, Toast.LENGTH_SHORT).show()
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
                val recyclerView = requireView().findViewById<RecyclerView>(R.id.task_list)
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