package com.steam.pablodiez.steamstats;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by UO224433 on 26/03/2015.
 */
public class Juego implements Parcelable, Serializable {

    private String Nombre;
    private int id;
    private boolean tieneLogros;
    private String urlImagen;
    private int Tiempo;
    private boolean fav;

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public boolean isTieneLogros() {
        return tieneLogros;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTiempo() {
        return Tiempo;
    }

    public Juego(String nombre, int Id, int tiempo, boolean control, String imagen) {
        Nombre = nombre;
        id = Id;
        tieneLogros = control;
        urlImagen = imagen;
        Tiempo = tiempo;
        fav=false;


    }

    public Juego(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        Nombre = parcel.readString();
        id = parcel.readInt();
        urlImagen = parcel.readString();
        Tiempo = parcel.readInt();
        boolean[] BooleanArr = new boolean[1];
        parcel.readBooleanArray(BooleanArr);
        tieneLogros = BooleanArr[0];

    }

    public String getNombre() {

        return Nombre;
    }


    public String getImagen() {
        return urlImagen;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Nombre);
        dest.writeInt(id);
        dest.writeString(urlImagen);
        dest.writeInt(Tiempo);
        dest.writeBooleanArray(new boolean[]{tieneLogros});

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
