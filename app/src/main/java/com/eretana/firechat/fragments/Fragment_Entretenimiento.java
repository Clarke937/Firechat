package com.eretana.firechat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.eretana.firechat.R;
import com.eretana.firechat.adapters.Adapter_Entretenimiento;

/**
 * Created by Edgar on 27/7/2017.
 */

public class Fragment_Entretenimiento extends Fragment {

    private GridView gridv;
    private Adapter_Entretenimiento adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_entretenimiento,container,false);
        init_controls(fragment);
        return fragment;
    }

    private void init_controls(View v){
        adapter = new Adapter_Entretenimiento(this.getContext());
        gridv = (GridView) v.findViewById(R.id.entreto_grid);
        gridv.setAdapter(adapter);
    }

}
