package com.binplus.earnquizmoney.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.binplus.earnquizmoney.R;


public class ProfileFragment extends Fragment {

LinearLayout layout_basic,layout_basic_edit,layout_social,layout_social_media,layout_refer,layout_basic_refer;
ImageView img_down_basic,img_down_social,img_down_refer;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initview(view);
        allClick();
        return  view;
    }

    private void allClick() {
        layout_basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_basic_edit.getVisibility() == View.VISIBLE) {
                    layout_basic_edit.setVisibility(View.GONE);
                    img_down_basic.animate().rotation(0).setDuration(300).start();
                } else {
                    layout_basic_edit.setVisibility(View.VISIBLE);
                    img_down_basic.animate().rotation(180).setDuration(300).start();
                }
            }
        });
        layout_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_social_media.getVisibility() == View.VISIBLE){
                    layout_social_media.setVisibility(View.GONE);
                    img_down_social.animate().rotation(0).setDuration(300).start();
                }else {
                    layout_social_media.setVisibility(View.VISIBLE);
                    img_down_social.animate().rotation(180).setDuration(300).start();
                }
            }
        });
        layout_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout_basic_refer.getVisibility() == View.VISIBLE){
                    layout_basic_refer.setVisibility(View.GONE);
                    img_down_refer.animate().rotation(0).setDuration(300).start();
                }else {
                    layout_basic_refer.setVisibility(View.VISIBLE);
                    img_down_refer.animate().rotation(180).setDuration(300).start();
                }
            }
        });
    }

    private void initview(View view) {
        layout_basic = view.findViewById(R.id.layout_basic);
        layout_basic_edit = view.findViewById(R.id.layout_basic_edit);
        img_down_basic = view.findViewById(R.id.img_down_basic);
        layout_social = view.findViewById(R.id.layout_social);
        layout_social_media = view.findViewById(R.id.layout_social_media);
        img_down_social = view.findViewById(R.id.img_down_social);
        layout_refer = view.findViewById(R.id.layout_refer);
        layout_basic_refer = view.findViewById(R.id.layout_basic_refer);
        img_down_refer = view.findViewById(R.id.img_down_refer);
    }
}