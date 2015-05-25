package com.steam.pablodiez.steamstats;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;



public class FragmentList extends Fragment implements AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Adaptador mAdapter = null;
    private Callbacks mCallback;
    private Handler mhandler=new Handler();
    private ListView lvItems;
    private ArrayList<Juego> listaJuegos=new ArrayList<>();
    private ArrayList<Juego> listaFav = new ArrayList<>();
    private int NJuegos;
    private int horas;
    private boolean favoritos=false;
    private String LIST_fILENAME;
    private FrameLayout layout;
    private ProgressBar progress;

    // TODO: Rename and change types of parameters
    public static FragmentList newInstance() {
        FragmentList fragment = new FragmentList();
        return fragment;
    }

    public FragmentList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView; rootView = inflater.inflate(R.layout.fragment_fragment_list, container, false);

        lvItems = (ListView) rootView.findViewById(R.id.listViewListaJuegos);


        if(savedInstanceState!=null){
            listaJuegos= (ArrayList<Juego>) savedInstanceState.get("juegos");
            horas= (int) savedInstanceState.get("horas");
            NJuegos= (int) savedInstanceState.get("NJuegos");
            LIST_fILENAME= (String) savedInstanceState.get("File");
            favoritos= (boolean) savedInstanceState.get("Fav");
            listaFav= (ArrayList<Juego>) savedInstanceState.get("FavList");
            if(favoritos){
                mAdapter = new Adaptador(getActivity(), listaFav);
                lvItems.setAdapter(mAdapter);
            }else {
                mAdapter = new Adaptador(getActivity(), listaJuegos);
                lvItems.setAdapter(mAdapter);
            }
            TextView njuegos= (TextView) rootView.findViewById(R.id.textViewNJuegos);
            njuegos.setText(Integer.toString(NJuegos));
            njuegos.setTextColor(Color.WHITE);
            TextView hjuegos= (TextView) rootView.findViewById(R.id.textViewHJuegos);
            hjuegos.setText(horas+" "+ getString(R.string.hours));
            hjuegos.setTextColor(Color.WHITE);
            TextView texto1= (TextView) rootView.findViewById(R.id.textViewnJuegos);
            TextView texto2= (TextView) rootView.findViewById(R.id.textViewhJuegos);
            texto1.setText(R.string.NJuegos);
            texto2.setText(R.string.HJuegos);
            texto1.setTextColor(Color.WHITE);
            texto2.setTextColor(Color.WHITE);
        }

        else {
            String id=mCallback.getID();
            LIST_fILENAME=id;
            restoreList();
            layout=(FrameLayout) rootView.findViewById(R.id.FrameLayoutList);
            progress=new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleLarge);
            layout.addView(progress);
            new JSONAsyncTask().execute("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=AC95C91EC79892F20E251435B95B5054&steamid="+id+"&include_appinfo=1&include_played_free_games=1&format=json");
            }


        lvItems.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("juegos", listaJuegos);
        outState.putInt("horas", horas);
        outState.putInt("NJuegos", NJuegos);
        outState.putString("File",LIST_fILENAME);
        outState.putBoolean("Fav",favoritos);
        outState.putParcelableArrayList("FavList",listaFav);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Juego juego = (Juego) parent.getItemAtPosition(position);
        mCallback.onJuegoSelected(juego);
    }



    public interface Callbacks {
        public void onJuegoSelected(Juego juego);
        public String getID();
    }



    public void Favoritos() {
        if (!favoritos) {
            favoritos = true;
            listaFav.clear();
            for (int i = 0; i < listaJuegos.size(); i++) {
                if (listaJuegos.get(i).isFav()) listaFav.add(listaJuegos.get(i));
            }
            mAdapter = new Adaptador(getActivity(), listaFav);
            lvItems.setAdapter(mAdapter);
        } else {
            favoritos = false;
            mAdapter = new Adaptador(getActivity(), listaJuegos);
            lvItems.setAdapter(mAdapter);
        }
    }



    private void saveList(){

        listaFav.clear();
        for (int i = 0; i < listaJuegos.size(); i++) {
            if (listaJuegos.get(i).isFav()) listaFav.add(listaJuegos.get(i));
        }

        FileOutputStream file=null;
        OutputStream buffer=null;
        ObjectOutput output=null;
        
        try{
            file=getActivity().openFileOutput(LIST_fILENAME, Context.MODE_PRIVATE);
            buffer=new BufferedOutputStream(file);
            output=new ObjectOutputStream(buffer);
            output.writeObject(listaFav);
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try{
                output.close();
            }catch (IOException e){

            }
        }
    }

    private boolean restoreList(){
        InputStream buffer=null;
        ObjectInput input=null;

        try{
            buffer=new BufferedInputStream(getActivity().openFileInput(LIST_fILENAME));
            input=new ObjectInputStream(buffer);
            listaFav= (ArrayList<Juego>) input.readObject();
            return true;
        }catch (Exception ex){

        }finally {
            try {
                input.close();
            }catch (Exception e){

            }
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveList();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Callbacks");
        }
    }

    public class JSONAsyncTask extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            if(restoreList()) {
                for (int i = 0; i < listaJuegos.size(); i++) {
                    for (int j = 0; j < listaFav.size(); j++) {
                        if (listaJuegos.get(i).getId() == listaFav.get(j).getId()) {
                            listaJuegos.get(i).setFav(true);
                        }
                    }
                }
            }

            mAdapter = new Adaptador(getActivity(), listaJuegos);
            lvItems.setAdapter(mAdapter);
            layout.removeView(progress);
        }


        @Override
        protected Void doInBackground(String... params) {
            String url=params[0];

            try {
                String JSONText = obtenerJSON(url);

                JSONObject Root = new JSONObject(JSONText);
                JSONObject response =Root.getJSONObject("response");
                JSONArray juegos=response.getJSONArray("games");
                NJuegos= response.getInt("game_count");

                int min=0;


                for (int i=0; i< juegos.length();i++) {
                    if (!juegos.getJSONObject(i).getString("img_logo_url").isEmpty()) {
                    String nombre = juegos.getJSONObject(i).getString("name");
                    int id=juegos.getJSONObject(i).getInt("appid");
                    boolean control=juegos.getJSONObject(i).has("has_community_visible_stats");
                    String imagen=juegos.getJSONObject(i).getString("img_logo_url");
                        String urlImagen = "http://media.steampowered.com/steamcommunity/public/images/apps/" + id + "/" + imagen + ".jpg";
                        horas = juegos.getJSONObject(i).getInt("playtime_forever");
                        min += horas;
                        listaJuegos.add(new Juego(nombre, id, horas, control, urlImagen));
                    }

                }

                final String horas=Integer.toString(min/60);

                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView njuegos= (TextView) getView().findViewById(R.id.textViewNJuegos);
                        njuegos.setText(Integer.toString(NJuegos));
                        TextView hjuegos= (TextView) getView().findViewById(R.id.textViewHJuegos);
                        hjuegos.setText(horas+" "+ getString(R.string.hours));
                        hjuegos.setTextColor(Color.WHITE);
                        njuegos.setTextColor(Color.WHITE);
                        TextView texto1= (TextView) getView().findViewById(R.id.textViewnJuegos);
                        TextView texto2= (TextView) getView().findViewById(R.id.textViewhJuegos);
                        texto1.setText(R.string.NJuegos);
                        texto2.setText(R.string.HJuegos);
                        texto1.setTextColor(Color.WHITE);
                        texto2.setTextColor(Color.WHITE);

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String obtenerJSON (String myUrl) {
            String respuesta = null;
            try {
                URL url = new URL(myUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader is = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                StringBuffer buf = new StringBuffer();

                int c;
                while ((c = is.read()) != -1) {
                    buf.append((char) c);
                }

                con.disconnect();
                respuesta = buf.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return respuesta;

        }
    }
}

