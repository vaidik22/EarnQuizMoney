package com.binplus.earnquizmoney.common;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;


public class Common {
    Context context;


    public Common(Context context){
        this.context=context;


    }


    public void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(null).commit();


    }

}
