package com.example.notesapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private EditText editTextNote;
    private Button buttonAdd;
    private ListView listViewNotes;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> notesList;
    private ArrayList<Integer> noteIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DataBaseHelper(this);
        editTextNote = findViewById(R.id.editTextNote);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewNotes = findViewById(R.id.listViewNotes);
        notesList = new ArrayList<>();
        noteIds = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        listViewNotes.setAdapter(adapter);

        loadNotes();

        buttonAdd.setOnClickListener(v -> {
            String note = editTextNote.getText().toString().trim();
            if (!note.isEmpty()) {
                if (dbHelper.insertNote(note)) {
                    Toast.makeText(MainActivity.this, "Note added", Toast.LENGTH_SHORT).show();
                    loadNotes();
                    editTextNote.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add note", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewNotes.setOnItemClickListener((parent, view, position, id) -> showEditDeleteDialog(noteIds.get(position), notesList.get(position)));
    }

    private void loadNotes() {
        notesList.clear();
        noteIds.clear();
        Cursor cursor = dbHelper.getAllNotes();
        while (cursor.moveToNext()) {
            noteIds.add(cursor.getInt(0));
            notesList.add(cursor.getString(1));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void showEditDeleteDialog(int id, String oldNote) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Edit/Delete Note");

        final EditText input = new EditText(this);
        input.setText(oldNote);
        dialog.setView(input);

        dialog.setPositiveButton("Update", (dialog1, which) -> {
            String newNote = input.getText().toString();
            if (dbHelper.updateNote(id, newNote)) {
                Toast.makeText(MainActivity.this, "Note updated", Toast.LENGTH_SHORT).show();
                loadNotes();
            }
        });

        dialog.setNegativeButton("Delete", (dialog12, which) -> {
            if (dbHelper.deleteNote(id)) {
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                loadNotes();
            }
        });

        dialog.setNeutralButton("Cancel", null);
        dialog.show();
    }
}
