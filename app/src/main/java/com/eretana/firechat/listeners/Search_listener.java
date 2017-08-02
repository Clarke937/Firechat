package com.eretana.firechat.listeners;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eretana.firechat.Authentication;
import com.eretana.firechat.R;
import com.eretana.firechat.Search_result;
import com.eretana.firechat.models.Session;

/**
 * Created by Edgar on 23/6/2017.
 */

public class Search_listener implements View.OnClickListener {

    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Buscas un Usuario?");
        builder.setView(R.layout.vista_search_friend);
        builder.setPositiveButton("Buscar",new Search_user());
        builder.setNegativeButton("Busqueda Avanzada",new Avanced_search());
        builder.create().show();

    }

    //Buscar Usuario
    public class Search_user implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            AlertDialog dialogo = (AlertDialog) dialog;
            EditText et = (EditText) ((AlertDialog) dialog).findViewById(R.id.search_friend_box);
            String username = et.getText().toString();

            Intent intent = new Intent(dialogo.getContext(), Search_result.class);
            intent.putExtra("username",username);
            intent.putExtra("search","simple");
            dialogo.getContext().startActivity(intent);
        }
    }

    //Busqueda Avanzada
    public class Avanced_search implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            AlertDialog dialogo = (AlertDialog) dialog;
            dialogo.getContext().startActivity(new Intent(dialogo.getContext(), null));
        }
    }

}
