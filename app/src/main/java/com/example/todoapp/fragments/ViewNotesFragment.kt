package com.example.todoapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoapp.AllNotesActivity
import com.example.todoapp.R
import com.example.todoapp.RecyclerView.NotesInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewNotesFragment : Fragment(R.layout.fragment_view_notes) {

    private lateinit var headTextView: TextView
    private lateinit var contextTextView: TextView
    private lateinit var allNotesButton: Button

    private lateinit var navController: NavController

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("UserInfo")


        val reference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("UserInfo")
                .child(mAuth.currentUser?.uid!!)
        headTextView = view.findViewById(R.id.notes_heading1)
        contextTextView = view.findViewById(R.id.notes_content1)
        allNotesButton = view.findViewById(R.id.allNotesButton)
        navController = Navigation.findNavController(view)


        allNotesButton.setOnClickListener {

            val intent = Intent(activity, AllNotesActivity::class.java)

            reference.addValueEventListener(object :
                ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, "Error!!!!", Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (activity != null) {
                        snapshot.children.forEach {
                            val items = it.getValue(NotesInfo::class.java)

                            val headingL = items?.heading
                            val contentL = items?.content
                            var isInlist: Boolean = false

                            for (item in EditNotesFragment.noteList) {
                                if (item.heading == headingL && item.content == contentL) {
                                    isInlist = true
                                }
                            }

                            if (!isInlist && headingL != null && contentL != null) {
                                EditNotesFragment.noteList.add(NotesInfo(headingL, contentL))
                            }
                        }
                    }
                }

            })
            startActivity(intent)

        }


        if (mAuth.currentUser?.uid != null) {

            reference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val p = snapshot.getValue(NotesInfo::class.java)

                    if (p != null && activity != null) {
                        snapshot.children.forEach {
                            val items = it.getValue(NotesInfo::class.java)

                            headTextView.text = items?.heading
                            contextTextView.text = items?.content

                            var isInList: Boolean = false
                            for (i in EditNotesFragment.noteList) {
                                if (i.heading == p.heading && i.content == p.content) {
                                    isInList = true
                                }
                            }
                            if (!isInList && p.heading != null && p.content != null) {
                                EditNotesFragment.noteList.add(NotesInfo(p.heading, p.content))
                            }
                        }
                    }
                }
            })
        }
    }

}