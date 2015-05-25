package com.steam.pablodiez.steamstats;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by UO224433 on 26/03/2015.
 */
public class Logro implements Parcelable{

    private String Nombre;
    private String Descripcion;
    private boolean Conseguido;
    private String urlImagen;

    public Logro(String nombre, String descripcion,boolean conseguido,String url) {
        Nombre = nombre;
        Descripcion=descripcion;
        Conseguido=conseguido;
        urlImagen=url;
    }

    public Logro(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        Nombre=parcel.readString();
        Descripcion=parcel.readString();
        urlImagen=parcel.readString();
        boolean[] BooleanArr = new boolean[1];
        parcel.readBooleanArray(BooleanArr);
        Conseguido = BooleanArr[0];
    }

    public String getNombre() {

        return Nombre;
    }


    public String getDescripcion() {
        return Descripcion;
    }


    public boolean isConseguido() {
        return Conseguido;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Nombre);
        dest.writeString(Descripcion);
        dest.writeString(urlImagen);
        dest.writeBooleanArray(new boolean[]{Conseguido});

    }

    public static final Parcelable.
            Creator<Juego> CREATOR =
            new Parcelable.Creator<Juego>() {
                public Juego createFromParcel(Parcel in) {
                    return new Juego(in);
                }

                @Override
                public Juego[] newArray(int size) {
                    return new Juego[size];
                }
            };
}
