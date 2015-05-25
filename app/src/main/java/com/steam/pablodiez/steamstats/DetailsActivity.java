package com.steam.pablodiez.steamstats;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;


public class DetailsActivity extends ActionBarActivity {
    public static final String NAME = "NAME";
    public static final String ID = "ID";
    public static final String CONTROL = "CONTROL";
    public static final String HORAS = "HORAS";
    public static final String ID64 = "ID64";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        Intent intent = getIntent();
        String nombre = intent.getStringExtra(NAME);
        int id = intent.getIntExtra(ID,0);
        int horas = intent.getIntExtra(HORAS,0);
        boolean control = intent.getBooleanExtra(CONTROL,false);
        String id64=intent.getStringExtra(ID64);

        if (findViewById(R.id.fragment_container) != null) {
            // Si estamos restaurando desde un estado previo no hacemos nada
            if (savedInstanceState != null) {
                return;
            }
            // Crear el fragmento pasándole el parámetro
            FragmentDetails fragment = FragmentDetails.newInstance(nombre,id,control,horas,id64);
            // Añadir el fragmento al contenedor
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit(); }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

}
