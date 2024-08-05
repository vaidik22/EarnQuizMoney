package com.binplus.earnquizmoney.common;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.ConfigModel;
import com.binplus.earnquizmoney.retrofit.OngetConfigData;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Common {
    private AppCompatActivity activity;
    private Context context;
    Api apiInterface;

    public Common(Context context) {
        this.context = context;
        apiInterface = RetrofitClient.getRetrofitInstance().create(Api.class);
    }

    public Common(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(null).commit();
    }

    public void switchFragmentHome(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager ( );
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ( );
        fragmentTransaction.replace (R.id.homeFragment, fragment);
        fragmentTransaction.addToBackStack (null);
        fragmentTransaction.commit ( );
    }


    public void callGetIndexAPI(final OngetConfigData ongetConfigData) {
        Call<ConfigModel> call = apiInterface.getIndexApi();

        call.enqueue(new Callback<ConfigModel>() {
            @Override
            public void onResponse(Call<ConfigModel> call, Response<ConfigModel> response) {
                String resp = response.toString();
                if (response.isSuccessful()) {
                    Log.e("index api", "onResponse: " + resp);
                    ongetConfigData.OngetConfigData(resp);
                }
            }

            @Override
            public void onFailure(Call<ConfigModel> call, Throwable t) {

            }
        });
    }
}
