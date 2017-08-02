package com.eretana.firechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.eretana.firechat.fragments.Fragment_Search;

public class Search_result extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private Intent intent;
    private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        this.setTitle("Resultados");
        init_controls();
    }

    public void init_controls(){
        intent = getIntent();
        bnv = (BottomNavigationView) findViewById(R.id.navigation);
        bnv.setOnNavigationItemSelectedListener(this);
        search(1);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_womens:
                search(0);
            break;

            case R.id.nav_mens:
                search(1);
            break;

            case R.id.nav_trans:
                search(2);
            break;
        }

        return true;
    }

    public void search(int sex) {
        Bundle bundle = new Bundle();
        bundle.putString("search", intent.getStringExtra("search"));
        bundle.putString("username", intent.getStringExtra("username"));
        bundle.putInt("sex",sex);

        Fragment fragment = new Fragment_Search();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.search_framelayout, fragment).commit();
    }


}
