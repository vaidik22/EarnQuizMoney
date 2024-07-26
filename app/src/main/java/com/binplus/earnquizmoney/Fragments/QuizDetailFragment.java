package com.binplus.earnquizmoney.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binplus.earnquizmoney.R;


public class QuizDetailFragment extends Fragment {


    public QuizDetailFragment() {
        // Required empty public constructor
    }


    public static QuizDetailFragment newInstance(String param1, String param2) {
        QuizDetailFragment fragment = new QuizDetailFragment();
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
        View view =  inflater.inflate(R.layout.fragment_quiz_detail, container, false);
    return view;
    }
}