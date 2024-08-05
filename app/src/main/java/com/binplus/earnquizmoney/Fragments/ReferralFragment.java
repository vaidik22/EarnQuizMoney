package com.binplus.earnquizmoney.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binplus.earnquizmoney.Adapters.ReferAdapter;
import com.binplus.earnquizmoney.Model.ReferModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferralFragment extends Fragment {

    private TextView earnedAmountTextView;
    private TextView totalReferCountTextView;
    private TextView user_name;
    private TextView user_id;
    private TextView tvNoData;
    private RecyclerView referRecyclerView;
    private ReferAdapter referAdapter;
    private List<ReferModel.Data> referList = new ArrayList<>();
    Api apiInterface;

    public ReferralFragment() {
        // Required empty public constructor
    }

    public static ReferralFragment newInstance(String param1, String param2) {
        ReferralFragment fragment = new ReferralFragment();
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

        View view = inflater.inflate(R.layout.fragment_referral, container, false);
        initView(view);
        setupRecyclerView();
        callReferralApi();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Default name");
        return view;
    }

    private void callReferralApi() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "Default Id");
        String userName = sharedPreferences.getString("userName", "Default name");

        JsonObject object = new JsonObject();
        object.addProperty("user_id", userId);

        apiInterface = RetrofitClient.getRetrofitInstance().create(Api.class);
        Call<ReferModel> call = apiInterface.getReferApi(object);
        call.enqueue(new Callback<ReferModel>() {
            @Override
            public void onResponse(Call<ReferModel> call, Response<ReferModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                    Log.d("ReferralFragment", "Data loaded successfully, referList size: " + referList.size());
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReferModel> call, Throwable t) {
                Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUI(ReferModel referModel) {
        if (referModel != null && referModel.getData() != null) {
            earnedAmountTextView.setText("Rs. " + referModel.getData().getTotal_amount_earned());
            totalReferCountTextView.setText("Total Refer: " + referModel.getData().getTotal_refer_count());
            user_name.setText(referModel.getData().getName());
            user_id.setText("ID:#" + referModel.getData().getId());

            referList.clear();
            referList.addAll(referModel.getRefer_history());

            if (referList.isEmpty()) {
                tvNoData.setVisibility(View.VISIBLE);
                referRecyclerView.setVisibility(View.GONE);
            } else {
                tvNoData.setVisibility(View.GONE);
                referRecyclerView.setVisibility(View.VISIBLE);
            }
            referAdapter.notifyDataSetChanged();
        }
    }

    private void setupRecyclerView() {
        referAdapter = new ReferAdapter(referList);
        referRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        referRecyclerView.setAdapter(referAdapter);
    }

    private void initView(View view) {
        earnedAmountTextView = view.findViewById(R.id.earner_amount);
        totalReferCountTextView = view.findViewById(R.id.total_refered_count);
        referRecyclerView = view.findViewById(R.id.refered_recycler_view);
        user_name = view.findViewById(R.id.user_name);
        user_id = view.findViewById(R.id.user_id);
        tvNoData = view.findViewById(R.id.tv_no_data);
    }
}
