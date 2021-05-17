package com.carlos.myapps.petsapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditFragment : Fragment() {

    companion object {
        const val TASK_ID_KEY = "TASK_ID_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val deleteTaskBtn = view.findViewById<Button>(R.id.delete_btn)
        val closeTaskBtn = view.findViewById<ImageView>(R.id.close_btn)
        val taskTitle = view.findViewById<EditText>(R.id.task_title)
        val taskDescription = view.findViewById<EditText>(R.id.task_description)
        val id = arguments?.getString(TASK_ID_KEY)


        id?.let {
            loadTask(it) { task ->
                taskTitle.setText(task.name)
                taskDescription.setText(task.description)
            }
        }

        deleteTaskBtn.setOnClickListener {
            delete()
        }

        closeTaskBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun loadTask(id: String, callback: ((Task) -> Unit)) {
        val db = Firebase.firestore

        db.collection("checklist").document(id)
            .get()
            .addOnSuccessListener { document ->
                val task = Task(
                    document.id,
                    document["name"].toString(),
                    document["description"].toString(),
                    document["complete"] as Boolean
                )
                callback.invoke(task)
            }
            .addOnFailureListener { e -> Log.w("Error", e) }
    }

    private fun delete() {
        val db = Firebase.firestore
        val id = arguments?.getString(TASK_ID_KEY)

        if (id != null) {
            db.collection("checklist").document(id)
                .delete()
                .addOnSuccessListener {
                    parentFragmentManager.popBackStack()
                }
                .addOnFailureListener { e -> Log.w("Error deleting document", e) }
        }
    }
}