package com.example.gandhi.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gandhi on 5/13/2016.
 */
public class NoteAdapter extends ArrayAdapter<Note> {

    public static class ViewHolder{
        TextView title;
        TextView note;
        ImageView noteIcon;
    }

    public NoteAdapter(Context context, ArrayList<Note> notes){
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data for this position.
        Note note = getItem(position);

        // create a new viewholder
        ViewHolder viewHolder;

        // Check if the existing view is being reused, otherwise inflate a new view from custom row layout.
        if(convertView == null){

            //If we don't have a view that is being used create one, and make sure you create a
            //view holder along with it to save our view references to.
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);

            //set our views to our view holder so that we no longer have to go back and use find view
            //by id every time we have a new row.
            viewHolder.title = (TextView)convertView.findViewById(R.id.listItemNoteTitle);
            viewHolder.note = (TextView) convertView.findViewById(R.id.listItemNoteBody);
            viewHolder.noteIcon = (ImageView)convertView.findViewById(R.id.listItemNoteImg);

            //use set tag to remember our view holder which is holding our reference to our widgets.
            convertView.setTag(viewHolder);
        }else{
            //we already have a view so just go to our viewholder and grab the widgets from it.
            viewHolder = (ViewHolder)convertView.getTag();
        }

        //Populate the data into the template view using the data object.
        viewHolder.title.setText(note.getTitle());
        viewHolder.note.setText(note.getMessage());
        viewHolder.noteIcon.setImageResource(note.getAssociatedDrawable());

        //Now we have modified the view to display appropriate data,
        //return it so it will be displayed.

        return convertView;
    }
}
