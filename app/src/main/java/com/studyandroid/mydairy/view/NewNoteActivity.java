package com.studyandroid.mydairy.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.studyandroid.mydairy.R;
import com.studyandroid.mydairy.controller.NoteDataSource;
import com.studyandroid.mydairy.utilily.Config;
import com.studyandroid.mydairy.utilily.Util;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewNoteActivity extends AppCompatActivity {
    // constants
    public final int PICK_PHOTO_FOR_NOTE = 0;

    private Toolbar toolBar;
    private ImageView imgAttach;
    private Bitmap bmpAttach;
    private EditText edtTitle;
    private EditText edtContent;

    private NoteDataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        // find views
        edtContent = (EditText) findViewById(R.id.edt_content);
        edtTitle = (EditText) findViewById(R.id.edt_title);
        imgAttach = (ImageView) findViewById(R.id.img_attach);

        // get action bar
        toolBar =(Toolbar) findViewById(R.id.my_toolbar);
        toolBar.setTitle("New Note");
        setSupportActionBar(toolBar);

        // get image attach
        imgAttach = (ImageView) findViewById(R.id.img_attach);

        // create data source
        dataSource = new NoteDataSource(this);
        dataSource.open();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.save_note:
                if(edtTitle.getText().toString().trim().length() <= 0)
                {
                    Toast.makeText(this, "Please input title of note!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if(edtContent.getText().toString().trim().length() <= 0)
                {
                    Toast.makeText(this, "Please input content of note!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                String dateTime =  Util.getCurrentDateTime();
                String imageName = Util.convertStringDatetimeToFileName(dateTime) + ".png";

                // save note to SQLite
                dataSource.addNewNote(edtTitle.getText().toString(), edtContent.getText().toString(), imageName, dateTime);
                // save image to SDcard
                if(bmpAttach != null)
                {
                    Util.saveImageToSDCard(bmpAttach, Config.FOLDER_IMAGES, imageName);
                }
                this.finish();
                Toast.makeText(this,"Add new note successfully",Toast.LENGTH_LONG).show();
                break;
            case R.id.attach_image:
                pickImage();
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
    // Select image from gallery
    private void pickImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PHOTO_FOR_NOTE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_NOTE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(this, "There is no selected photo", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                // get image from result
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                bmpAttach = BitmapFactory.decodeStream(bufferedInputStream);

                // show image in screen
                imgAttach.setImageBitmap(bmpAttach);
                imgAttach.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
