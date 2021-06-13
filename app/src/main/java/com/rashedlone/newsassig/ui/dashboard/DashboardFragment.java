package com.rashedlone.newsassig.ui.dashboard;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rashedlone.newsassig.R;
import com.rashedlone.newsassig.adapter.AdapterListNews;
import com.rashedlone.newsassig.datamodel.NewsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private AdapterListNews adapter;
    private ProgressBar pb;
    private List<NewsData> data = new ArrayList<>();
    private String url = "https://api.spaceflightnewsapi.net/v3/articles/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        data.clear();
        adapter = null;

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        pb = root.findViewById(R.id.process);
        recyclerView.setHasFixedSize(true);
        adapter  =  new AdapterListNews(getContext(), data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sp = requireContext().getSharedPreferences("saved", MODE_PRIVATE);

        if(sp.getAll().isEmpty()){
            pb.setVisibility(View.GONE);

        }
        Map<String,?> keys = sp.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet())
        {
            getNews(url+entry.getKey());
        }

        return root;
    }
    private void getNews(String URL) {

        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {

                    pb.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                            NewsData entry  =  new NewsData(""+obj.getString("id"),""+obj.getString("title"),""+obj.getString("imageUrl"),""+obj.getString("newsSite"),""+obj.getString("publishedAt"), ""+obj.getString("summary"),""+obj.getString("url"));
                            data.add(entry);
                            adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pb.setVisibility(View.GONE);

                    }

                },
                error -> {
                    pb.setVisibility(View.GONE);
                    getNews(URL);


                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}