package com.sburba.tvdbapi.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by DILSHAN FERNANDO on 4/24/2016.
 */
public class MySeriesAdapter extends RecyclerView.Adapter<MySeriesAdapter.myViewHolder>{
    private LayoutInflater inflater;
    List<MySeriesInformation> data = Collections.emptyList();

    public MySeriesAdapter(Context context, List<MySeriesInformation> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_tv_series_custom_row, parent, false);


        myViewHolder holder = new myViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        MySeriesInformation current=data.get(position);
        holder.title.setText(current.getTitle());
        File imgFile = new  File(current.getImgPath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.icon.setImageBitmap(myBitmap);
        }

        //
        // holder.icon.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        CardView card;
        public myViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);

        }
    }
}
