package com.carlos.myapps.petsapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        deleteTaskBtn.setOnClickListener {
            delete()
        }
        closeTaskBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun delete() {
        val db = Firebase.firestore
        val document = arguments?.getString(TASK_ID_KEY)

        if (document != null) {
            db.collection("checklist").document(document)
                .delete()
                .addOnSuccessListener {
                    parentFragmentManager.popBackStack()
                }
                .addOnFailureListener { e -> Log.w("Error deleting document", e) }
        }
    }
}