package com.example.elpaseoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;


public class AdaptadorProducto extends ArrayAdapter {

    private Context ListadoProductoAct;
    private ArrayList<Producto> productos;

    public AdaptadorProducto(Context context, int textViewResourceId, ArrayList<Producto> objects) {
        super(context, textViewResourceId, objects);
        productos = objects;
        this.ListadoProductoAct = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.producto_list_element, null);
        Button b = (Button) v.findViewById(R.id.boton_id);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionPreferences.getPrefUsername(getContext()).length() != 0) {
                    ((ListadoProductoActivity) ListadoProductoAct).mostrarDialogCantidad(productos.get(position).getId());
                } else {
                    ((ListadoProductoActivity) ListadoProductoAct).mostrarDialogInicioSesion();
                }
            }
        });
        Button desc = (Button) v.findViewById(R.id.descripcion);
        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListadoProductoActivity) ListadoProductoAct).mostrarDescripcion(productos.get(position).getId());
            }
        });
        TextView nombreText = (TextView) v.findViewById(R.id.nombre);
        nombreText.setText(productos.get(position).getTitle().substring(0, 1).toUpperCase() + productos.get(position).getTitle().substring(1));
        TextView precio = (TextView) v.findViewById(R.id.precio);
        precio.setText("$" + productos.get(position).getPrice());
        return v;
    }

}
