package com.example.elpaseoapp;

//import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AdapterHelper {

    public static void getListViewSize(ListView myListView) {
        /* Esta clase sirve para mostrar el tamanio correcto de cada ListView dependiendo de la
           cantidad de elementos en el adapter y el tamanio del divider
         */
        ListAdapter myListAdapter = myListView.getAdapter();

        if (myListAdapter == null) {
            //do nothing return null
            return;
        }

        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            //Sumo al total el tamanio de cada elemento de la lista
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        //Configuro los parametros de la lista para que tengan el tamanio adecuado
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + 40 + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        //Log.i("height of listItem:", String.valueOf(totalHeight));
    }
}