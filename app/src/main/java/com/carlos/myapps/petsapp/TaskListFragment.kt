package com.carlos.myapps.petsapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carlos.myapps.petsapp.EditFragment.Companion.TASK_ID_KEY
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class TaskListFragment : Fragment() {

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

    private fun add() {
        parentFragmentManager.commit {
            replace<AddFragment>(R.id.fragment_container_view)
            addToBackStack(null)
        }
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

                adapter.clickListener = { task ->
                    val fragment = EditFragment()
                    val bundle = Bundle()
                    bundle.putString(TASK_ID_KEY, task.id)
                    fragment.arguments = bundle

                    parentFragmentManager.commit {
                        replace(R.id.fragment_container_view, fragment)
                        addToBackStack(null)
                    }
                }
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
    }
}