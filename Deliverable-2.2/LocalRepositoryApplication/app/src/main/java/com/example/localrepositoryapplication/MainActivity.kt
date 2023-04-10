package com.example.localrepositoryapplication

import android.database.sqlite.SQLiteConstraintException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.localrepositoryapplication.db.MainDatabase
import com.example.localrepositoryapplication.db.Student
import com.example.localrepositoryapplication.util.MainRecyclerViewAdapter

class MainActivity : AppCompatActivity() {
    // Main Database & Dataset
    private val mDatabase: MainDatabase by lazy {
        MainDatabase.getInstance(applicationContext)
    }
    private var mDataset: List<Student> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI components
        val mRecyclerView: RecyclerView = findViewById(R.id.mainRecyclerView)
        val fullNameEditText: EditText = findViewById(R.id.fullNameEditText)
        val puidEditText: EditText = findViewById(R.id.puidEditText)

        val addBtn: Button = findViewById(R.id.btnAdd)
        val dropBtn: Button = findViewById(R.id.btnDrop)

        // Initialize RecyclerView First.
        val mAdapter = MainRecyclerViewAdapter(mDataset)
        updateDataset(mAdapter)
        mRecyclerView.adapter = mAdapter

        addBtn.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val puid = puidEditText.text.toString()

            if (fullName == "" || puid == "") {
                Toast.makeText(
                    this,
                    "Please provide both full name and PUID.",
                    Toast.LENGTH_LONG).show()
            } else {
                try {
                    val temp = Student(
                        puid,
                        fullName.split(" ")[0],
                        fullName.split(" ")[1]
                    )

                    // Access Database consumes lots of computing resources. It is required to
                    // access Database at the separated thread.
                    Thread {
                        try {
                            mDatabase.mainDao().insertNewStudent(temp)
                            // Update dataset in Adapter
                            updateDataset(mAdapter)
                        } catch (e: SQLiteConstraintException) {
                            Handler(mainLooper).post {
                                Toast.makeText(
                                    this,
                                    "Student whose PUID is ${puid} is already in the roster.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }.start()
                } catch (e: ArrayIndexOutOfBoundsException) {
                    Toast.makeText(
                        this,
                        "Please provide both first name and last name",
                        Toast.LENGTH_LONG).show()
                }
            }
        }

        dropBtn.setOnClickListener {
            val puid = puidEditText.text.toString()

            if (puid == "") {
                Toast.makeText(
                    this,
                    "Please provide PUID.",
                    Toast.LENGTH_LONG).show()
            } else {
                // Access Database consumes lots of computing resources. It is required to
                // access Database at the separated thread.
                Thread {
                    val targetItem = mDatabase.mainDao().getStudent(puid)
                    if (targetItem != null) {
                        mDatabase.mainDao().deleteStudent(targetItem)
                    }
                    updateDataset(mAdapter)
                }.start()
            }
        }

    }

    private fun updateDataset(adapter: MainRecyclerViewAdapter) {
        Thread {
            Handler(mainLooper).post {
                adapter.dataset = mDataset
                adapter.notifyDataSetChanged()
            }
            mDataset = mDatabase.mainDao().getAllStudent()
        }.start()

    }
}