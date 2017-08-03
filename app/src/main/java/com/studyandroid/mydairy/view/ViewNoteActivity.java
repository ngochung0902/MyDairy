package com.studyandroid.mydairy.view;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.studyandroid.mydairy.R;
import com.studyandroid.mydairy.controller.NoteDataSource;
import com.studyandroid.mydairy.model.NoteModel;
import com.studyandroid.mydairy.utilily.Config;
import com.studyandroid.mydairy.utilily.Util;

public class ViewNoteActivity extends AppCompatActivity {
    private NoteModel model;
    private Toolbar toolBar;
    private TextView lblContent;
    private ImageView imgAttach;
    private NoteDataSource dataSource;
    private int noteID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        // get id of note
        noteID = getIntent().getExtras().getInt("note_id");
        String title = getIntent().getExtras().getString("title");
        String content = getIntent().getExtras().getString("content");
        String datetime = getIntent().getExtras().getString("date");
        String image = getIntent().getExtras().getString("image");

        // get action bar
        toolBar =(Toolbar) findViewById(R.id.my_toolbar);
        toolBar.setTitle(title);
        toolBar.setSubtitle(datetime);
        setSupportActionBar(toolBar);

        // find views
        lblContent = (TextView) findViewById(R.id.lbl_content);
        imgAttach = (ImageView) findViewById(R.id.img_attach);

        // set value
        lblContent.setText(content);
        Util.setBitmapToImage(this, Config.FOLDER_IMAGES, image, imgAttach);

        // create data source
        dataSource = new NoteDataSource(this);
        dataSource.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_note, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                dataSource.deleteNote(noteID);
                this.finish();
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
