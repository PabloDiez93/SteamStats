package com.steam.pablodiez.steamstats;



import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;



public class FragmentDetails extends Fragment {
    private static final String Name_ARG = "NombreJuego";
    private static final String ID_ARG = "IDJuego";
    private static final String H_ARG = "HJuego";
    private static final String Control_ARG = "Control";
    private static final String ID64 = "ID64";
    private TextView textViewName;
    private AdaptadorLogros mAdapter = null;
    private Handler mhandler=new Handler();
    private ListView lvItems;
    private int horasJuego;
    private int id;
    private ArrayList<Logro> listaLogros=new ArrayList<>();
    private int contador;
    private String idioma;
    private FrameLayout layout;
    private ProgressBar progress;



    public static FragmentDetails newInstance(String nombre, int id, boolean control,int horas,String id64) {
        FragmentDetails fragment = new FragmentDetails();
        Bundle args = new Bundle();
        args.putString(Name_ARG, nombre);
        args.putInt(ID_ARG, id);
        args.putInt(H_ARG, horas);
        args.putBoolean(Control_ARG, control);
        args.putString(ID64, id64);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.fragment_fragment_details, container, false);

        Bundle args = getArguments();
        textViewName = (TextView) rootView.findViewById(R.id.textViewNombreJuego);

        idioma=getResources().getString(R.string.language);


        if (args != null) {
            String nombre = args.getString(Name_ARG);
            id = args.getInt(ID_ARG);
            int min = args.getInt(H_ARG);
            boolean control = args.getBoolean(Control_ARG);
            String id64=args.getString(ID64);
            textViewName.setText(nombre);
            textViewName.setTextColor(Color.WHITE);
            horasJuego=min/60;
            TextView HorasJuego= (TextView) rootView.findViewById(R.id.textViewHorasJuego);
            HorasJuego.setText(R.string.HJuegos);
            HorasJuego.setTextColor(Color.WHITE);
            TextView tiempo=(TextView) rootView.findViewById(R.id.textViewHJuego);
            tiempo.setText(Integer.toString(horasJuego)+" "+getString(R.string.hours));
            tiempo.setTextColor(Color.WHITE);

            lvItems = (ListView) rootView.findViewById(R.id.listViewLogros);

            if (control) {

                if(savedInstanceState!=null){
                    listaLogros= (ArrayList<Logro>) savedInstanceState.get("logros");
                    contador= (int) savedInstanceState.get("contador");
                    mAdapter = new AdaptadorLogros(getActivity(), listaLogros);
                    lvItems.setAdapter(mAdapter);
                    TextView NLogros= (TextView) rootView.findViewById(R.id.textViewNLogros);
                    NLogros.setText(Integer.toString(contador)+"/"+listaLogros.size());
                    TextView Porcentaje= (TextView) rootView.findViewById(R.id.textViewNPorc);
                    Porcentaje.setText(Integer.toString(contador*100/listaLogros.size())+"%");
                    Porcentaje.setTextColor(Color.WHITE);
                    NLogros.setTextColor(Color.WHITE);
                    TextView SinLogros= (TextView) rootView.findViewById(R.id.textViewSinLogros);
                    TextView LogrosObtenidos= (TextView) rootView.findViewById(R.id.textViewLogrosObtenidos);
                    LogrosObtenidos.setTextColor(Color.WHITE);
                    SinLogros.setText("");
                    LogrosObtenidos.setText(R.string.LogrosObtenidos);
                    TextView porcentaje=(TextView) rootView.findViewById(R.id.textViewPorcentaje);
                    porcentaje.setText(R.string.porcentaje);
                    porcentaje.setTextColor(Color.WHITE);
                }
                else{
                layout=(FrameLayout) rootView.findViewById(R.id.FrameLayoutDetails);
                progress=new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleLarge);
                layout.addView(progress);
                new JSONAsyncTask().execute("http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/?appid=" + id + "&key=AC95C91EC79892F20E251435B95B5054&steamid="+id64+"&l="+idioma);
                }
            }

            else{
                TextView SinLogros= (TextView) rootView.findViewById(R.id.textViewSinLogros);
                TextView LogrosObtenidos= (TextView) rootView.findViewById(R.id.textViewLogrosObtenidos);
                TextView porcentaje= (TextView) rootView.findViewById(R.id.textViewPorcentaje);
                porcentaje.setText("");
                SinLogros.setText(R.string.SinLogros);
                LogrosObtenidos.setText("");
            }
        }


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("logros", listaLogros);
        outState.putInt("contador", contador);
        outState.putString("ID64", ID64);
    }


    public void setDetails(String text, int Id, boolean control,int horas, String id64) {


        textViewName.setText(text);
        textViewName.setTextColor(Color.WHITE);
        lvItems = (ListView) getView().findViewById(R.id.listViewLogros);
        lvItems.setAdapter(null);
        listaLogros.clear();
        TextView HorasJuego= (TextView) getView().findViewById(R.id.textViewHorasJuego);
        HorasJuego.setText(R.string.HJuegos);
        HorasJuego.setTextColor(Color.WHITE);
        id=Id;
        horasJuego=horas/60;
        TextView tiempo=(TextView) getView().findViewById(R.id.textViewHJuego);
        tiempo.setText(Integer.toString(horasJuego)+" "+getString(R.string.hours));
        tiempo.setTextColor(Color.WHITE);
        TextView n= (TextView) getView().findViewById(R.id.textViewNLogros);
        n.setText("");
        TextView p= (TextView) getView().findViewById(R.id.textViewNPorc);
        p.setText("");

        if (control) {
            layout=(FrameLayout) getView().findViewById(R.id.FrameLayoutDetails);
            progress=new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleLarge);
            layout.addView(progress);
            new JSONAsyncTask().execute("http://api.steampowered.com/ISteamUserStats/GetPlayerAchievements/v0001/?appid=" + id + "&key=AC95C91EC79892F20E251435B95B5054&steamid="+id64+"&l="+idioma);
        }

        else{
            TextView SinLogros= (TextView) getView().findViewById(R.id.textViewSinLogros);
            TextView LogrosObtenidos= (TextView) getView().findViewById(R.id.textViewLogrosObtenidos);
            TextView porcentaje= (TextView) getView().findViewById(R.id.textViewPorcentaje);
            porcentaje.setText("");
            SinLogros.setText(R.string.SinLogros);
            LogrosObtenidos.setText("");

        }
    }


    public class JSONAsyncTask extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            layout.removeView(progress);
        }

        @Override
        protected Void doInBackground(String... params) {
            String url=params[0];

            try {
                String JSONText = obtenerJSON(url);

                JSONObject Root = new JSONObject(JSONText);
                JSONObject response = Root.getJSONObject("playerstats");
                final JSONArray logros = response.getJSONArray("achievements");


                JSONText = obtenerJSON("http://api.steampowered.com/ISteamUserStats/GetSchemaForGame/v2/?key=AC95C91EC79892F20E251435B95B5054&appid="+id+"&l="+idioma);
                Root = new JSONObject(JSONText);
                response = Root.getJSONObject("game");
                JSONObject availableGameStats = response.getJSONObject("availableGameStats");
                JSONArray achievements=availableGameStats.getJSONArray("achievements");

                    contador=0;

                    for (int i = 0; i < logros.length(); i++) {
                        String nombre = logros.getJSONObject(i).getString("name");
                        String descripcion=logros.getJSONObject(i).getString("description");
                        boolean conseguido;
                        if(logros.getJSONObject(i).getInt("achieved")==1){
                            conseguido=true;
                            contador++;
                        }
                        else conseguido=false;
                       String URL=achievements.getJSONObject(i).getString("icon");
                        listaLogros.add(new Logro(nombre, descripcion, conseguido,URL));
                    }


                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new AdaptadorLogros(getActivity(), listaLogros);
                        lvItems.setAdapter(mAdapter);
                        TextView NLogros= (TextView) getView().findViewById(R.id.textViewNLogros);
                        NLogros.setText(Integer.toString(contador)+"/"+listaLogros.size());
                        TextView Porcentaje= (TextView) getView().findViewById(R.id.textViewNPorc);
                        Porcentaje.setText(Integer.toString(contador*100/listaLogros.size())+"%");
                        Porcentaje.setTextColor(Color.WHITE);
                        NLogros.setTextColor(Color.WHITE);
                        TextView SinLogros= (TextView) getView().findViewById(R.id.textViewSinLogros);
                        TextView LogrosObtenidos= (TextView) getView().findViewById(R.id.textViewLogrosObtenidos);
                        LogrosObtenidos.setTextColor(Color.WHITE);
                        SinLogros.setText("");
                        LogrosObtenidos.setText(R.string.LogrosObtenidos);
                        TextView porcentaje= (TextView) getView().findViewById(R.id.textViewPorcentaje);
                        porcentaje.setText(R.string.porcentaje);
                        porcentaje.setTextColor(Color.WHITE);
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


