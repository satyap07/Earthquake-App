package com.example.udacity2;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class earthquakeAdapter extends ArrayAdapter<EarthQuake> {
    public earthquakeAdapter(@NonNull Context context, @NonNull List<EarthQuake> earthquakes) {
        /**
         * Whenever you make a class which extends a class then always call the
         * constructor of the base class
         */

        super(context, 0, earthquakes);
    }
    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * overwriting the get view method
     * @param position --- to get the current position of the list
     * @param convertView --- existing view
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View newView= convertView;
        if(newView==null){
            newView=LayoutInflater.from(getContext()).inflate(R.layout.newlayout,parent,false);

        }
        EarthQuake currentObject=getItem(position);
        String originalLocation=currentObject.getmPlace();
        String locationOffset;
        String locationPrimary;
        /**
         * Regex for breaking the string in two string
         */
        if(originalLocation.contains(LOCATION_SEPARATOR)){
            String[] split=originalLocation.split(LOCATION_SEPARATOR);
            locationOffset=split[0]+LOCATION_SEPARATOR;
            locationPrimary=split[1];
        }else{
            locationOffset="Near the";
            locationPrimary=originalLocation;
        }

        TextView magText= (TextView) newView.findViewById(R.id.magTextView);
        String formatMagnitude=magnitudeFormatter(currentObject.getmMagnitude());
        magText.setText(formatMagnitude);
        /**
        * to fetch the background color of the textview
         */
        GradientDrawable magnitudeCircle=(GradientDrawable)magText.getBackground();
        int color= getBackgroundColor(currentObject.getmMagnitude());
        magnitudeCircle.setColor(color);
        TextView placeText=(TextView)newView.findViewById(R.id.placeTextView);
        placeText.setText(locationPrimary);
        TextView offsetTextView=(TextView)newView.findViewById(R.id.offsetTextView);
        offsetTextView.setText(locationOffset);
        Date dateObject = new Date(currentObject.getmDate());
        TextView dateText=(TextView)newView.findViewById(R.id.dateTextView);
        String formattedDate = formatDate(dateObject);
        dateText.setText(formattedDate);
        TextView timeText=(TextView)newView.findViewById(R.id.timeTextView);
        String formattedTime= formatTime(dateObject);
        timeText.setText(formattedTime);
        return newView;
    }
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }


    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return the formatted magnitude string with one decimal digit
     */
    private String magnitudeFormatter(double magnitude){
        DecimalFormat decimalFormat=new DecimalFormat("0.0");
        String decimal=decimalFormat.format(magnitude);
        return decimal;
    }
    /**
     * To determine what should be the color of the magnitude
     */
    private int getBackgroundColor(double magnitude){
        int colour;
        int magfloor=(int) Math.floor(magnitude);
        switch(magfloor){
            case 0:
            case 1:
                colour=R.color.magnitude1;
                break;
            case 2:
                colour=R.color.magnitude2;
                break;
            case 3:
                colour=R.color.magnitude3;
                break;
            case 4:
                colour=R.color.magnitude4;
                break;
            case 5:
                colour=R.color.magnitude5;
                break;
            case 6:
                colour=R.color.magnitude6;
                break;
            case 7:
                colour=R.color.magnitude7;
                break;
            case 8:
                colour=R.color.magnitude8;
                break;
            case 9:
                colour=R.color.magnitude9;
                break;
            case 10:
                colour=R.color.magnitude10plus;
                break;
            default:
                colour=R.color.magnitude10plus;
                break;

        }
        return ContextCompat.getColor(getContext(), colour);
    }




}
