package com.steam.pablodiez.steamstats;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.List;



public class Adaptador extends BaseAdapter {


    static class ViewHolder {
        public TextView mNombreJuego;
        public ImageView mImagen;
        public ImageButton mBoton;
    }

    private List<Juego> mJuegos;
    public LayoutInflater mInflater;


    public Adaptador(Context context, List<Juego> juegos) {

        if (context == null || juegos == null) {
            throw new IllegalArgumentException();
        }

        this.mJuegos = juegos;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {

        return mJuegos.size();
    }

    @Override
    public Object getItem(int position) {

        return mJuegos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
        }
        else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
            viewHolder.mNombreJuego = (TextView) rowView.findViewById(R.id.textViewNombre);
            viewHolder.mImagen = (ImageView) rowView.findViewById(R.id.imageViewJuego);
            viewHolder.mBoton = (ImageButton) rowView.findViewById(R.id.imageButtonFav);
            viewHolder.mBoton.setTag(position);
            viewHolder.mBoton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View V) {
                    LinearLayout parentRow = (LinearLayout) V.getParent();
                    ImageButton boton= (ImageButton) parentRow.getChildAt(1);
                    int pos= (int) V.getTag();
                    if (mJuegos.get(pos).isFav()) {
                        mJuegos.get(pos).setFav(false);
                        boton.setImageResource(R.mipmap.ic_empty_favstar);
                    }
                    else{
                        mJuegos.get(pos).setFav(true);
                        boton.setImageResource(R.mipmap.ic_favstar);

                    }
                }
            });

            rowView.setTag(viewHolder);


        Juego juego = (Juego) getItem(position);
        viewHolder.mNombreJuego.setText(juego.getNombre());

        ImageLoader.getInstance().displayImage(juego.getImagen(), viewHolder.mImagen);


        if (juego.isFav()) {
            viewHolder.mBoton.setImageResource(R.mipmap.ic_favstar);

        }
        else {
            viewHolder.mBoton.setImageResource(R.mipmap.ic_empty_favstar);
        }

        return rowView;

    }



}

