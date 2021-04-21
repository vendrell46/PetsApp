package com.carlos.myapps.petsapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val addTaskBtn = view.findViewById<Button>(R.id.add_btn)
        val closeTaskBtn = view.findViewById<ImageView>(R.id.close_btn)
        addTaskBtn.setOnClickListener {
            getDataFromTask()
        }
        closeTaskBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun getDataFromTask() {
        val taskNameTitle = requireView().findViewById<EditText>(R.id.task_title)
        val descriptionText = requireView().findViewById<EditText>(R.id.task_description)

        val taskData = Task(
            id = "0",
            name = taskNameTitle.text.toString(),
            description = descriptionText.text.toString(),
            complete = false
        )

        addTaskToDb(taskData)
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
                parentFragmentManager.popBackStack()
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    private fun taskAddedMessage(taskData: Task?) {
        Toast.makeText(requireContext(), taskData?.name, Toast.LENGTH_SHORT).show()
    }
}