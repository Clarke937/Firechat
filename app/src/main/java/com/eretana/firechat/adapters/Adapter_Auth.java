package com.eretana.firechat.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.eretana.firechat.fragments.Fragment_Login;
import com.eretana.firechat.fragments.Fragment_Registry;

/**
 * Created by Edgar on 22/6/2017.
 */

public class Adapter_Auth extends FragmentPagerAdapter {

    public Adapter_Auth(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new Fragment_Login();
            case 1:
                return new Fragment_Registry();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Iniciar Session";
            case 1:
                return "Registrarme";
        }
        return "Firechat";
    }
}
