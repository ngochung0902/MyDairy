package com.studyandroid.mydairy.controller;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studyandroid.mydairy.R;
import com.studyandroid.mydairy.model.NoteModel;
import com.studyandroid.mydairy.utilily.Config;
import com.studyandroid.mydairy.utilily.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by tuanlq on 7/29/2016.
 */
public class NoteAdapter extends BaseAdapter {
    private String[] arrColor = {"#CDDC39", "#FF3D00", "#FFFF00", "#AEEA00", "#00B8D4", "#311B92", "#FF1744", "#4A148C", "#F44336"};
    private Context context;
    private ArrayList<NoteModel> arr;
    private Random rand;

    public NoteAdapter(Context context, ArrayList<NoteModel> arr)
    {
        this.context = context;
        this.arr = arr;

        rand = new Random();
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)arr.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View rowView = (View)inflater.inflate(R.layout.note_layout, parent, false);
        // find view
        LinearLayout layoutMark = (LinearLayout) rowView.findViewById(R.id.layout_mark);
        TextView lblTitle = (TextView) rowView.findViewById(R.id.lbl_title);
        TextView lblContent = (TextView) rowView.findViewById(R.id.lbl_content);
        ImageView imgAttach = (ImageView) rowView.findViewById(R.id.imageView);
        TextView lblDay = (TextView) rowView.findViewById(R.id.lbl_day);
        TextView lblDate = (TextView) rowView.findViewById(R.id.lbl_date);
        TextView lblTime = (TextView) rowView.findViewById(R.id.lbl_time);

        // update value
        layoutMark.setBackgroundColor(Color.parseColor(arrColor[rand.nextInt(arrColor.length)]));

        NoteModel model = arr.get(position);
        lblTitle.setText(model.title);
        lblContent.setText(model.content);
        // set datetime
        Date date = Util.convertStringToDate(model.datetime);
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date);//Thursday
        String stringMonth = (String) DateFormat.format("MMM", date); //Jun
        String day = (String) DateFormat.format("dd", date); //20
        String time = (String) DateFormat.format("hh:mm", date); //09:23

        lblDay.setText(dayOfTheWeek);
        lblDate.setText(day + " " +stringMonth);
        lblTime.setText(time);
        // set image
        Util.setBitmapToImage(context, Config.FOLDER_IMAGES, model.image, imgAttach);

        return rowView;
    }
}
