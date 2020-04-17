package com.attors.educations.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.attors.educations.Helper.Utlity;
import com.attors.educations.Modal.NewsModal;
import com.attors.educations.Modal.TestModal;
import com.attors.educations.Modal.Weekmodal;
import com.attors.educations.Network.Apis;
import com.attors.educations.R;
import com.attors.educations.activity.News;
import com.attors.educations.activity.Post;
import com.attors.educations.adapter.NewsAdapter2;
import com.attors.educations.adapter.TestAdapter;
import com.attors.educations.adapter.WeekAdapter;
import com.attors.educations.databinding.WeeklytestfragmentBinding;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Newsfragments extends Fragment {

    WeeklytestfragmentBinding binding;
    private NewsAdapter2 newsAdapter;
    private ArrayList<NewsModal> newsModals;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.weeklytestfragment, container, false);

        if(getArguments()!=null) {
            newsModals=Utlity.gson.fromJson(getArguments().getString("blogs"), new TypeToken<List<NewsModal>>() {}.getType());
            if (newsModals != null && newsModals.size() != 0) {
                binding.weektest.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
                newsAdapter= new NewsAdapter2(getActivity(),newsModals);
                binding.weektest.setAdapter(newsAdapter);

                binding.weektest.setVisibility(View.VISIBLE);
                binding.noreords.setVisibility(View.GONE);
            }
            else
            {
                binding.weektest.setVisibility(View.GONE);
                binding.noreords.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            binding.weektest.setVisibility(View.GONE);
            binding.noreords.setVisibility(View.VISIBLE);
        }

        return binding.getRoot();
    }
}
