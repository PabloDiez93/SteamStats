package com.steam.pablodiez.steamstats;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class AdaptadorLogros extends BaseAdapter{

    static class ViewHolder {
        public TextView mNombreLogro;
        public TextView mDescripcion;
        public TextView mConseguido;
        public ImageView mImagen;
        public LinearLayout mLayout;
    }

    private final List<Logro> mLogros;
    public LayoutInflater mInflater;


    public AdaptadorLogros(Context context, List<Logro> logros) {

        if (context == null || logros == null ) {
            throw new IllegalArgumentException();
        }

        this.mLogros = logros;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return mLogros.size();
    }

    @Override
    public Object getItem(int position) {

        return mLogros.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.list_item_logros, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mNombreLogro = (TextView) rowView.findViewById(R.id.textViewNombre);
            viewHolder.mDescripcion = (TextView) rowView.findViewById(R.id.textViewDescripcion);
            viewHolder.mConseguido = (TextView) rowView.findViewById(R.id.textViewConseguido);
            viewHolder.mImagen = (ImageView) rowView.findViewById(R.id.imageViewLogros);
            viewHolder.mLayout= (LinearLayout) rowView.findViewById(R.id.LinearLayoutLogros);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        Logro logro = (Logro) getItem(position);
        viewHolder.mNombreLogro.setText(logro.getNombre());
        viewHolder.mDescripcion.setText(logro.getDescripcion());
        if(logro.isConseguido()){
            viewHolder.mConseguido.setText(R.string.si);
            viewHolder.mLayout.setBackgroundColor(Color.parseColor("#FF707070"));
        }
        else{
            viewHolder.mConseguido.setText(R.string.no);
            viewHolder.mLayout.setBackgroundColor(Color.parseColor("#FFD0D0D0"));
        }

        ImageLoader.getInstance().displayImage(logro.getUrlImagen(), viewHolder.mImagen);

        return rowView;
    }


}
