package com.binplus.earnquizmoney.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.binplus.earnquizmoney.Model.CommonModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HowToPlayFragment extends Fragment {

    private TextView howToPlay;
    Api apiService ;


    public HowToPlayFragment() {
        // Required empty public constructor
    }


    public static HowToPlayFragment newInstance(String param1, String param2) {
        HowToPlayFragment fragment = new HowToPlayFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_how_to_play, container, false);
        howToPlay = view.findViewById(R.id.how_to_play);
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(Api.class);
        fetchPrivacyPolicy();
    return view;
    }

    private void fetchPrivacyPolicy() {


        Call<CommonModel> call = apiService.getPrivacy();
        call.enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CommonModel.Datum> dataList = response.body().getData();
                    for (CommonModel.Datum data : dataList) {
                        if ("3".equals(data.getId())) {
                            howToPlay.setText(data.getMessage());
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}