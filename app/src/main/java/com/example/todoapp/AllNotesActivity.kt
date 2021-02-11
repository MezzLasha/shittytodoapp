package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.RecyclerView.NotesAdapter
import com.example.todoapp.fragments.EditNotesFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AllNotesActivity : AppCompatActivity() {

    private lateinit var db : DatabaseReference
    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_notes)

        recyclerView = findViewById(R.id.recycler)
        db = FirebaseDatabase.getInstance().reference

        val adapter = NotesAdapter(EditNotesFragment.noteList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}