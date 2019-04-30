package com.example.imagenotes.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imagenotes.bean.SavedNotes;
import com.example.imagenotes.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SavedNotesListAdapter extends ArrayAdapter<SavedNotes> {

    private Activity context;
    private ArrayList<SavedNotes> savedNotesList;

    public SavedNotesListAdapter(Activity context, ArrayList<SavedNotes> savedNotesList) {
        super(context, com.example.imagenotes.R.layout.savednotes_list_item, savedNotesList);
        this.savedNotesList = savedNotesList;
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.savednotes_list_item, null, true);

        ImageView image = (ImageView) rowView.findViewById(R.id.list_item_image);
        TextView note = (TextView) rowView.findViewById(R.id.list_item_note);

        note.setText(savedNotesList.get(position).getText());
        image.setImageBitmap(savedNotesList.get(position).getImage());

        return rowView;
    }
}
