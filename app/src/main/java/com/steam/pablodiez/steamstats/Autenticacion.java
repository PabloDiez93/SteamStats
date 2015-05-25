package com.steam.pablodiez.steamstats;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class Autenticacion extends ActionBarActivity {
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        Button boton= (Button) findViewById(R.id.button);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView texto= (TextView) findViewById(R.id.editText);
                String nombre= texto.getText().toString();
                new JSONAsyncTask().execute(nombre);
            }
        });

    }

    public class JSONAsyncTask extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            Intent i=new Intent(Autenticacion.this,MainActivity.class);
            i.putExtra(MainActivity.ID,id);
            startActivity(i);
        }

        @Override
        protected Void doInBackground(String... params) {
            String nombre = params[0];

            try {
                XmlPullParser parser = obtenerXML("http://steamcommunity.com/id/" + nombre + "?xml=1");

                String PROFILE_XML_TAG = "profile";
                String STEAMID64_XML_TAG = "steamID64";


                String namespace = null;
                parser.require(XmlPullParser.START_TAG, namespace, PROFILE_XML_TAG);
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();

                    if (name.equals(STEAMID64_XML_TAG)) {
                        parser.next();
                        id = parser.getText();
                    }
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
        }

        private XmlPullParser obtenerXML (String myUrl) {
            try {
                URL url = new URL(myUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                InputStream in = con.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();

                in.close();
                con.disconnect();

                return parser;

            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_autenticacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
