package edu.ewubd.foodblog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomPostActivity extends ArrayAdapter<Posts> {

    private final Context context;
    private final ArrayList<Posts> values;


    public CustomPostActivity(@NonNull Context context, @NonNull ArrayList<Posts> objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.layout_post_row, parent, false);

        TextView posttitle = rowView.findViewById(R.id.tvtitle);
        //TextView eventDateTime = rowView.findViewById(R.id.tvEventDateTime);
        TextView postdesc = rowView.findViewById(R.id.tvdesc);
        ImageView imageView = rowView.findViewById(R.id.tvimage);

        posttitle.setText(values.get(position).title +" by " + values.get(position).userMail);
        postdesc.setText(values.get(position).desc + " Posted at:("+ values.get(position).dateTime+")");
        showDecodedImage(imageView,values.get(position).img);


        return rowView;
    }
    public void showDecodedImage(ImageView myImageView, String encodedImageString){
        byte[] decodedString = Base64.decode(encodedImageString, Base64.DEFAULT);
        Bitmap myImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        myImageView.setImageBitmap(myImage);
    }
}


