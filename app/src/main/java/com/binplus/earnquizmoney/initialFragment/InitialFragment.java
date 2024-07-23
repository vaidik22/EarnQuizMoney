package com.binplus.earnquizmoney.initialFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.binplus.earnquizmoney.Fragments.LoginFragment;
import com.binplus.earnquizmoney.Fragments.SignUpFragment;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.common.Common;


public class InitialFragment extends Fragment {
    Button existingMemberButton;
    Button createAccountButton;
    Common common;


    public InitialFragment() {
        // Required empty public constructor
    }

    public static InitialFragment newInstance(String param1, String param2) {
        InitialFragment fragment = new InitialFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        common = new Common(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_initial, container, false);
        initView(view);
        allClick();
        return  view;
    }

    private void allClick() {
        existingMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LoginFragment();
                common.switchFragment(fragment);
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SignUpFragment();
                common.switchFragment(fragment);
            }
        });
    }

    private void initView(View view) {
        existingMemberButton = view.findViewById(R.id.existingMemberButton);
        createAccountButton = view.findViewById(R.id.createAccountButton);

    }
}