package com.eretana.firechat.fragments;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.eretana.firechat.R;
import com.eretana.firechat.utils.Form_utils;


public class Fragment_Login extends Fragment {

    EditText et_email, et_password;
    CheckBox check_remember;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_login,container,false);
        //init_controls(fragment);
        return fragment;
    }

    private void init_controls(View v){
        et_email = (EditText) v.findViewById(R.id.auth_email);
        et_password = (EditText) v.findViewById(R.id.auth_password);
        check_remember = (CheckBox) v.findViewById(R.id.auth_rememberme);
    }

}
