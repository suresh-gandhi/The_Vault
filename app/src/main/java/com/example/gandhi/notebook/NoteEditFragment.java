package com.example.gandhi.notebook;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private ImageButton noteCatButton;
    private EditText title, message;
    private Note.Category savedButtonCategory;
    private AlertDialog categoryDialogObject, confirmDialogObject;
    private static final String MODIFIED_CATEGORY = "Modified Category";
    private boolean newNote = false;
    private long noteId = 0;

    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //grab the bundle that sends along whether or not our noteEditFragment is creating a new note.
        Bundle bundle = this.getArguments();
        if(bundle != null){
            newNote = bundle.getBoolean(NoteDetailActivity.NEW_NOTE_EXTRA, false);
        }
        if(savedInstanceState != null){
            savedButtonCategory = (Note.Category)savedInstanceState.get(MODIFIED_CATEGORY);
        }
        //inflate our fragment edit layout.
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        //grab widget references from layout.
        title = (EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        message = (EditText)fragmentLayout.findViewById(R.id.editNoteMessage);
        noteCatButton = (ImageButton)fragmentLayout.findViewById(R.id.editNoteButton);
        Button savedButton = (Button)fragmentLayout.findViewById(R.id.saveNote);

        //Populate widgets with note data.
        Intent intent = getActivity().getIntent();
        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA, ""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA, ""));
        noteId = intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA, 0);

        //If we grabbed a category from our bundle then we know we have changed the orientation and saved information
        //so set our image button background to that category.
        if(savedButtonCategory != null){
            noteCatButton.setImageResource(Note.categoryToDrawable(savedButtonCategory));
        }
        //otherwise we came from our list fragment so just do everything normally.
        else if (!newNote){
            Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
            savedButtonCategory = noteCat;
            noteCatButton.setImageResource(Note.categoryToDrawable(noteCat));
        }

        buildCategoryDialog();
        buildConfirmDialog();

        noteCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialogObject.show();
            }
        });

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogObject.show();
            }
        });

        // Inflate the layout for this fragment
        return fragmentLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(MODIFIED_CATEGORY, savedButtonCategory);
    }

    private void buildCategoryDialog(){
        final String[] categories = {"Personal", "Technical", "Quote", "Finance"};
        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());
        categoryBuilder.setTitle("Choose Note Type");
        categoryBuilder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //dismisses our dialogue window.
                categoryDialogObject.cancel();
                switch (item){
                    case 0:
                        savedButtonCategory = Note.Category.PERSONAL;
                        noteCatButton.setImageResource(R.drawable.p);
                        break;
                    case 1:
                        savedButtonCategory = Note.Category.TECHNICAL;
                        noteCatButton.setImageResource(R.drawable.t);
                        break;
                    case 2:
                        savedButtonCategory = Note.Category.QUOTE;
                        noteCatButton.setImageResource(R.drawable.q);
                        break;
                    case 3:
                        savedButtonCategory = Note.Category.FINANCE;
                        noteCatButton.setImageResource(R.drawable.f);
                        break;
                }
            }
        });
        categoryDialogObject = categoryBuilder.create();
    }

    private void buildConfirmDialog(){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Are you sure?");
        confirmBuilder.setMessage("Are you sure you want to save the note?");

        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                Log.d("Save Note", "Note title: " + title.getText() + "Note message: " + message.getText() + "Note category: " + savedButtonCategory);

                NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();
                //if it's a new note create it in our database.
                if(newNote){
                    dbAdapter.createNote(title.getText() + "", message.getText() + "", (savedButtonCategory == null)? Note.Category.PERSONAL : savedButtonCategory);
                }
                //otherwise it's an old note so update it in our database.
                else{
                    dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "", savedButtonCategory);
                }

                dbAdapter.close();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing here
            }
        });

        confirmDialogObject = confirmBuilder.create();
    }
}
