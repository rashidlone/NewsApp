package com.rashedlone.newsassig.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rashedlone.newsassig.NewsDetails;
import com.rashedlone.newsassig.R;
import com.rashedlone.newsassig.datamodel.NewsData;

import java.util.List;


public class AdapterListNews extends RecyclerView.Adapter<AdapterListNews.ViewHolder>{
    private final Context context;
    private List<NewsData> listdata;


    public AdapterListNews(Context context, List<NewsData> listdata) {
        this.context = context;
        this.listdata = listdata;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.news_view, parent, false);
        return new ViewHolder(listItem);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        NewsData data = listdata.get(position);

        holder.title.setText(data.getNewsTitle());
        holder.newsSite.setText(data.getNewsSite());
        holder.date.setText(data.getNewsDate());
        Glide.with(context).load(data.getNewsImage()).into(holder.image);

        holder.newsView.setOnClickListener(view -> {

            Intent i = new Intent(context, NewsDetails.class);
            i.putExtra("keys",data);
            context.startActivity(i);


            SharedPreferences sp = context.getSharedPreferences("last", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("last", data.getPid());
            editor.apply();
        });


    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<NewsData> list){
        listdata = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, newsSite;
        ImageView image;
        LinearLayout newsView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.title);
            this.newsView = itemView.findViewById(R.id.newsView);
            this.date = itemView.findViewById(R.id.date);
            this.newsSite = itemView.findViewById(R.id.newsSite);
            this.image = itemView.findViewById(R.id.image);



        }
    }
}
