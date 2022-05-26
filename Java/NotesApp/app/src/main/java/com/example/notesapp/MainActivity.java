package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonSubmit;
    EditText editText;
    ScrollView scrollView;
    LinearLayout linearLayoutRoot;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get application context for new View creation
        context = getApplicationContext();

        // Allows editText to be visible while keyboard is shown
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Connect view components to class vars
        buttonSubmit = findViewById(R.id.buttonSubmit);
        editText = findViewById(R.id.editTextTextMultiLine);
        scrollView = findViewById(R.id.scrollView);
        linearLayoutRoot = findViewById(R.id.linearLayoutRoot);

        // Create tutorial note
        createNote("Hello and welcome to NotesApp! To begin, type the note you would like to make in the text box below, then press take note!\n\n" +
                "If you would like to delete a note, just press and hold it for a few moments!");
    }

    public void onClickSubmit(View v){

        // Get text from editText, create a new note with it
        createNote(editText.getText().toString());

        // Clear editText
        editText.setText("");

    }

    public void createNote(String noteText){
        // Get text from editText, get and display view.

        // Check to see if editText is empty
        if(!noteText.isEmpty()){

            // String is not empty, create new Note object with editText contents
            Note note = new Note(noteText);

            // Get note View and add it to the scrollview
            LinearLayout noteView = note.getNote(context);
            noteView.setPadding(1, 1, 1, 1);

            // Add it to the scrollView
            linearLayoutRoot.addView(noteView);

            // Add an onLongClick to the noteView for deleting said view
            noteView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    // Delete noteView from scrollView
                    linearLayoutRoot.removeView(noteView);

                    // Show user confirmation that the note has been deleted
                    Toast.makeText(context, "Note was deleted!", Toast.LENGTH_LONG).show();

                    return true;
                }
            });
        } else {
            // String is empty, show toast to user
            Toast.makeText(context, "Note cannot be empty!", Toast.LENGTH_LONG).show();
        }
    }
}