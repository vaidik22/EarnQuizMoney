package com.binplus.earnquizmoney.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binplus.earnquizmoney.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LiveGamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveGamesFragment extends Fragment {



    public LiveGamesFragment() {
        // Required empty public constructor
    }


    public static LiveGamesFragment newInstance(String param1, String param2) {
        LiveGamesFragment fragment = new LiveGamesFragment();
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
        View view = inflater.inflate(R.layout.fragment_live_games, container, false);
   return view;
    }
}