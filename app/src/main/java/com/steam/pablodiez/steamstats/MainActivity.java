package com.steam.pablodiez.steamstats;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;



public class MainActivity extends ActionBarActivity implements FragmentList.Callbacks {

    public static final String ID = "ID";
    private boolean mTwoPanes = false;
    private boolean favoritos=false;
    private MenuItem fav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        if (findViewById(R.id.details_container) != null) {
            mTwoPanes = true;
        }

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                setTitle("SteamStats");
                toolbar.setTitleTextColor(Color.WHITE);
                toolbar.setBackgroundColor(Color.parseColor("#FF505050"));
                toolbar.setLogo(R.mipmap.ic_launcher);

        }

        if(savedInstanceState!=null){
            favoritos= savedInstanceState.getBoolean("fav");
        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fav:
                Favoritos();
                return true;
            case R.id.action_back:
                finish();
                return true;
        }
        return false;
    }

    public void Favoritos() {
        if(favoritos){
            favoritos=false;
            fav.setIcon(R.mipmap.ic_empty_favstar);
        }
        else{
            favoritos=true;
            fav.setIcon(R.mipmap.ic_favstar);
        }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentList fragment = (FragmentList) fragmentManager.findFragmentById(R.id.list_frag);
            fragment.Favoritos();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflar el men� y a�adir acciones al action bar si existe
        getMenuInflater().inflate(R.menu.menu_main, menu);
        fav= menu.findItem(R.id.action_fav);
        if(favoritos){
            fav.setIcon(R.mipmap.ic_favstar);
        }else{
            fav.setIcon(R.mipmap.ic_empty_favstar);
        }
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onJuegoSelected(Juego juego) {
        if ( !mTwoPanes ) {
            Intent intent2 = getIntent();
            String id = intent2.getStringExtra(ID);
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.NAME, juego.getNombre());
            intent.putExtra(DetailsActivity.ID, juego.getId());
            intent.putExtra(DetailsActivity.CONTROL, juego.isTieneLogros());
            intent.putExtra(DetailsActivity.HORAS, juego.getTiempo());
            intent.putExtra(DetailsActivity.ID64, id);
            startActivity(intent);
        } else {
            Intent intent = getIntent();
            String id = intent.getStringExtra(ID);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentDetails fragment = (FragmentDetails) fragmentManager.findFragmentById(R.id.details_frag);
            fragment.setDetails(juego.getNombre(),juego.getId(),juego.isTieneLogros(),juego.getTiempo(),id);


        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fav", favoritos);
    }

    @Override
    public String getID() {
        Intent intent = getIntent();
        String id = intent.getStringExtra(ID);
        return id;
    }

    @Override
    public void onBackPressed() {
        if(favoritos) {
            Favoritos();
        }
    }
}
