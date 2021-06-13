package com.rashedlone.newsassig.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class HomeFragment extends Fragment {

    private AdapterListNews adapter, adapter2;
    private ProgressBar pb;
    private List<NewsData> data = new ArrayList<>();
    private List<NewsData> data2 = new ArrayList<>();
    private String url = "https://api.spaceflightnewsapi.net/v3/articles";
    private LinearLayout lastViewed;
    private SharedPreferences sp;
    private RecyclerView recyclerView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        data.clear();
        data2.clear();
        adapter = null;
        adapter2 = null;

        sp = requireContext().getSharedPreferences("last", MODE_PRIVATE);
        lastViewed = root.findViewById(R.id.lastViewed);

        pb = root.findViewById(R.id.process);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter  =  new AdapterListNews(getContext(), data);
        recyclerView.setAdapter(adapter);


        recyclerView2 = root.findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));


        EditText filter = root.findViewById(R.id.filter);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().isEmpty())
                    update(s.toString());

            }
        });


        getNews(url);

        if(sp.contains("last")){
            getLastViewedNews(url+"/"+sp.getString("last",""));
        }

        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    void update(String text){
        List<NewsData> temp = new ArrayList();
        for(NewsData d: data){
            if(d.getNewsTitle().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }

    private void getNews(String URL) {

        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {

                    pb.setVisibility(View.GONE);

                    try {
                        JSONArray resp = new JSONArray(response);

                        for(int i = 0; i< resp.length();i++) {
                            JSONObject obj = resp.getJSONObject(i);

                            NewsData entry  =  new NewsData(""+obj.getString("id"),""+obj.getString("title"),""+obj.getString("imageUrl"),""+obj.getString("newsSite"),""+obj.getString("publishedAt"), ""+obj.getString("summary"),""+obj.getString("url"));
                            data.add(entry);
                            adapter.notifyDataSetChanged();
                        }

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


    private void getLastViewedNews(String URL) {
        data2.clear();
        adapter2 = null;


        @SuppressLint("NotifyDataSetChanged") StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                response -> {

                    pb.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);

                        NewsData entry  =  new NewsData(""+obj.getString("id"),""+obj.getString("title"),""+obj.getString("imageUrl"),""+obj.getString("newsSite"),""+obj.getString("publishedAt"), ""+obj.getString("summary"),""+obj.getString("url"));
                        data2.add(entry);

                        if(data2.size()>1)
                            data2.remove(1);

                        adapter2  =  new AdapterListNews(getContext(), data2);
                        recyclerView2.setAdapter(adapter2);
                        lastViewed.setVisibility(View.VISIBLE);

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
    public void onResume() {

        super.onResume();
       if(sp.contains("last")){

               data2.clear();
               getLastViewedNews(url+"/"+sp.getString("last",""));
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}