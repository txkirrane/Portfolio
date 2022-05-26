package com.example.notesapp;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDateTime;

public class Note {

    private String noteText = "";
    private LocalDateTime creationDate;

    public Note(String noteText){
        this.noteText = noteText;
        this.creationDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return noteText;
    }

    public LinearLayout getNote(Context c){

        LinearLayout layout = new LinearLayout(c);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(c);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setText(noteText);

        TextView tvDate = new TextView(c);
        tvDate.setTextColor(Color.parseColor("#0000FF"));
        tvDate.setText("\n"+creationDate.toString());

        layout.addView(tv);
        layout.addView(tvDate);

        layout.setBackgroundColor(Color.parseColor("#CCCCCC"));

        return layout;
    }
}
