package com.rashedlone.newsassig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.rashedlone.newsassig.datamodel.NewsData;

import java.util.Objects;

public class NewsDetails extends AppCompatActivity {

    private TextView title, description, date;
    private ImageView image;
    private NewsData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        initToolbar();
    }

    private void initToolbar() {

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        image = findViewById(R.id.image);

        data = (NewsData) getIntent().getSerializableExtra("keys");
        title.setText(data.getNewsTitle());
        description.setText(data.getNewsSummary());
        date.setText(data.getNewsDate());
        Glide.with(this).load(data.getNewsImage()).into(image);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }else if(item.getItemId() == R.id.share)
        {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,data.getNewsUrl());
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Read this:");
            startActivity(Intent.createChooser(shareIntent, "Share..."));

        }else if(item.getItemId() == R.id.save)
        {
            SharedPreferences sp = getSharedPreferences("saved", MODE_PRIVATE);

            if(sp.contains(data.getPid())) {
                Toast.makeText(this, "Already Saved!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = sp.edit();
                editor.putString(data.getPid(), data.getPid());
                editor.apply();
            }



        }
        return super.onOptionsItemSelected(item);
    }
}
