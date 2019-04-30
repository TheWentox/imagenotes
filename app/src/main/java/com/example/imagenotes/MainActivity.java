package com.example.imagenotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.imagenotes.adapter.SavedNotesListAdapter;
import com.example.imagenotes.bean.SavedNotes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ArrayList<SavedNotes> savedNotesArrayList;
    private ListView list;
    private ImageButton imageButton;
    private EditText editText;
    private Button saveImageButton;
    private SavedNotesListAdapter adapter;
    private Bitmap bitmap;
    private HashMap<Bitmap, String> map;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        editText = (EditText) findViewById(R.id.editText);
        saveImageButton = (Button) findViewById(R.id.saveImage);
        savedNotesArrayList = new ArrayList<SavedNotes>();
        bitmap = null;
        map = new HashMap<Bitmap, String>();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        adapter = new SavedNotesListAdapter(this, savedNotesArrayList);
        list = (ListView) findViewById(R.id.list_savednotes);
        list.setAdapter(adapter);

        imageButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});

        saveImageButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                if(bitmap == null){
                    Toast.makeText(MainActivity.this,"You must select an image first", Toast.LENGTH_LONG).show();
                }
                else{
                    if(map.get(bitmap) == null) {
                        map.put(bitmap, editText.getText().toString());
                    }
                    else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            map.replace(bitmap, editText.getText().toString());
                        }
                    }
                    Toast.makeText(MainActivity.this,"Note saved", Toast.LENGTH_LONG).show();
                    updateArrayList();
                }
            }});

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SavedNotes sn = (SavedNotes) parent.getItemAtPosition(position);
                bitmap = sn.getImage();
                imageButton.setImageBitmap(bitmap);
                editText.setText(sn.getText());
            }
        });
    }

   /* @Override
    public void onStop() {
        super.onStop();
        Gson gson = new Gson();
        String hashMapString = gson.toJson(map);
        editor.putString("map", hashMapString);
        editor.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Gson gson = new Gson();
        String storedHashMapString = sp.getString("map", "");
        Type type = new TypeToken<HashMap<Bitmap, String>>() {
        }.getType();
        if(map != null){
            map = gson.fromJson(storedHashMapString, type);
            updateArrayList();
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void updateArrayList() {
        for (HashMap.Entry<Bitmap, String> entry : map.entrySet()) {
            Iterator<SavedNotes> i = savedNotesArrayList.iterator();
            while(i.hasNext()){
                if(i.next().getImage().equals(entry.getKey())){
                    i.remove();
                }
            }
            SavedNotes sn = new SavedNotes();
            sn.setImage(entry.getKey());
            sn.setText(entry.getValue());
            savedNotesArrayList.add(sn);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                imageButton.setImageBitmap(bitmap);
                editText.setText(map.get(bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
