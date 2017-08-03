package com.studyandroid.mydairy.utilily;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tuanlq on 7/30/2016.
 */
public class Util {
    // PUBLIC METHODS
    // Save image to internal memory
    public static boolean saveImageToInternalStorage(Context context, Bitmap image, String name) {
        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    public static boolean saveImageToSDCard(Bitmap image, String folder, String name) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folder + "/";

        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            OutputStream fOut = null;
            File file = new File(fullPath, name);
            if(!file.exists()) {
                file.createNewFile();
            }
            fOut = new FileOutputStream(file);

            // 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            //MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            return true;

        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }

    }
    // check status readable of SD card

    public static Bitmap getImageFromMemory(Context context, String folder, String filename) {

        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath()  + "/" + folder + "/";
        Bitmap image = null;

        // Look for the file on the external storage
        try {
            if (Util.isSDReadable() == true) {
                image = BitmapFactory.decodeFile(fullPath + "/" + filename);
            }
        } catch (Exception e) {
            Log.e("get image on external storage: ", e.getMessage());
        }

        // If no file on external storage, look in internal storage
        if (image == null) {
            try {
                File filePath = context.getFileStreamPath(filename);
                FileInputStream fi = new FileInputStream(filePath);
                image = BitmapFactory.decodeStream(fi);
            } catch (Exception ex) {
                Log.e("get image on internal storage: ", ex.getMessage());
            }
        }
        return image;
    }
    public static String getCurrentDateTime()
    {
        // get datetime
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = dateFormat.format(new Date());

        return datetime;
    }
    //PRIVATE METHOHS
    // check readable ability of SD card
    private static boolean isSDReadable() {

        boolean mExternalStorageAvailable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = true;
            Log.i("isSdReadable", "External storage card is readable.");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            Log.i("isSdReadable", "External storage card is readable.");
            mExternalStorageAvailable = true;
        } else {
            // Something else is wrong. It may be one of many other
            // states, but all we need to know is we can neither read nor write
            mExternalStorageAvailable = false;
        }

        return mExternalStorageAvailable;
    }
    // Convert String to datetime
    public static Date convertStringToDate(String datetime)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(datetime);
            return date;
        }catch (Exception e)
        {
            return null;
        }

    }


    public static void setBitmapToImage(final Context context, final String folder, final String name, final ImageView imageView)
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Bitmap bitmap = (Bitmap) msg.obj;
                if(bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }else
                {
                    imageView.setVisibility(View.GONE);
                }
            }
        };
        // set flag
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = Util.readImage(folder, name, context);
                    Message msg = new Message();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
            });
            thread.start();

        }catch (Exception ex)
        {

        }
    }
    // read image from SD or Internal memory
    public static Bitmap readImage(String foler, String filename, Context context)
    {
        Bitmap img = null;
        // read image from SD card
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +foler + "/" + filename;
        try
        {
            img = BitmapFactory.decodeFile(fullPath);
        }catch (Exception e)
        {
            Log.i("DemoReadWriteImage","Can not read image from SD card");
        }

        // read image from internal memory
        try {
            File myFile = context.getFileStreamPath(filename);
            FileInputStream fIn = new FileInputStream(myFile);

            img = BitmapFactory.decodeStream(fIn);
        }catch (Exception e)
        {
            Log.i("DemoReadWriteImage","Can not read image from internal memory");
        }
        return img;
    }
    public static String convertStringDatetimeToFileName(String date)
    {
        return date.toString().replace(":","").replace(" ","").replace("-","");
    }

}
