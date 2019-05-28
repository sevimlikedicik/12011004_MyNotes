package com.murad.mobileproject.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.murad.mobileproject.models.Note;
import com.murad.mobileproject.R;
import com.murad.mobileproject.util.SharedPreference;
import com.murad.mobileproject.edit.EditNoteActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    List<Note> noteList;
    private ListView mListView;
    private NotesAdapter notesAdapter;
    private TextView mTextViewNoNote;
    private TextView mTextViewEditList;
    private LinearLayout mLinearLayoutNotes;
    private EditText mEditTextSearch;
    private Boolean isDelete = false;

    @Override
    protected void onResume() {
        super.onResume();

        try {
            noteList = Note.parse(MainActivity.this, new JSONArray(SharedPreference.getInstance(MainActivity.this).getNotes()));
        } catch (JSONException e) {
            noteList = new ArrayList<>();
        }

        if (noteList != null && noteList.size() > 0){
           setNoteList(isDelete);
        } else {
            mTextViewNoNote.setVisibility(View.VISIBLE);
            mLinearLayoutNotes.setVisibility(View.GONE);
        }

        if (notesAdapter != null){
            mEditTextSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    notesAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void setNoteList(boolean isDelete){
        mLinearLayoutNotes.setVisibility(View.VISIBLE);
        notesAdapter = new NotesAdapter(MainActivity.this, noteList, isDelete);
        mListView.setAdapter(notesAdapter);
        mListView.setVerticalScrollBarEnabled(false);
        mTextViewNoNote.setVisibility(View.GONE);
        if (isDelete && mEditTextSearch.getText() != null){
            notesAdapter.getFilter().filter(mEditTextSearch.getText().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.dashboard_list);
        mTextViewNoNote = findViewById(R.id.tv_main_no_note);
        mLinearLayoutNotes = findViewById(R.id.ll_notes_layout);
        mEditTextSearch = findViewById(R.id.et_main_search);
        mTextViewEditList = findViewById(R.id.tv_main_edit_list);
        mTextViewEditList.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);

                intent.putExtra("noteItem", note);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_main_edit_list :
                isDelete = !isDelete;
                setNoteList(isDelete);
                break;
        }
    }
}
