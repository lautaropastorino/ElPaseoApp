package com.example.elpaseoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DireccionEnvioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DireccionEnvioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final float TEXT_SIZE = 18;
    private MapView map;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Direccion direccionSeleccionada;
    private Tarjeta tarjetaSeleccionada;

    public DireccionEnvioFragment() {
        // Required empty public constructor
    }

    /**
     * Constructor que recibe la tarjeta seleccionada
     * en el fragment anterior, correspondiente a método de pago
     *
     * @param tarjeta si es null significa que se selecciono como
     *                método de pago 'efectivo'.
     */
    public DireccionEnvioFragment(Tarjeta tarjeta){
        this.tarjetaSeleccionada = tarjeta;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DireccionEnvioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DireccionEnvioFragment newInstance(String param1, String param2) {
        DireccionEnvioFragment fragment = new DireccionEnvioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        org.osmdroid.config.IConfigurationProvider osmConf = org.osmdroid.config.Configuration.getInstance();
        File basePath = new File(getActivity().getCacheDir().getAbsolutePath(), "osmdroid");
        osmConf.setOsmdroidBasePath(basePath);
        File tileCache = new File(osmConf.getOsmdroidBasePath().getAbsolutePath(), "tile");
        osmConf.setOsmdroidTileCache(tileCache);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_direccion_envio, container, false);
        this.deshabilitarBotonContinuar(view);
        // Recupero la direccion de registro del usuario.
        Direccion direccionRegistro = SessionPreferences.getDireccionRegistro(getContext());
        TextView textDireccionRegistro = (TextView) view.findViewById(R.id.boton_direccion_registro);
        textDireccionRegistro.setText("Calle " + direccionRegistro.getStreet() + ", número " + direccionRegistro.getNumber());

        RadioGroup rg = (RadioGroup) view.findViewById(R.id.radio_group_direcciones);
        ArrayList<Direccion> listaDirecciones = SessionPreferences.getDireccionesGuardadas(getContext());
        if (listaDirecciones != null){
            RadioButton rb;
            int index = 0;
            for (Direccion dir: listaDirecciones){
                rb = new RadioButton(getContext());
                String text = "Calle " + dir.getStreet() + ", número " + dir.getNumber();
                rb.setText(text);
                rb.setTextSize(TEXT_SIZE);
                rb.setId(index);
                rg.addView(rb);
                index++;
            }
        }

        // Listeners para cada boton
        Button botonContinuar = (Button) view.findViewById(R.id.boton_continuar);
        Button botonAtras = (Button) view.findViewById(R.id.boton_atras);

        botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManagement.reemplazarFragmentDesdeDetalle(getActivity().getSupportFragmentManager(), new ConfirmarCompraFragment(direccionSeleccionada, tarjetaSeleccionada));
            }
        });
        botonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManagement.reemplazarFragmentDesdeDetalle(getActivity().getSupportFragmentManager(), new MetodoPagoFragment());
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.boton_retiro_nodo:
                        habilitarBotonContinuar(view);
                        setDireccionSeleccionada(null);
                        mostrarMapa(view);
                        break;
                    case R.id.boton_direccion_registro:
                        habilitarBotonContinuar(view);
                        setDireccionSeleccionada(direccionRegistro);
                        deshabilitarMapa(view);
                        break;
                    case 0:
                        habilitarBotonContinuar(view);
                        setDireccionSeleccionada(listaDirecciones.get(0));
                        deshabilitarMapa(view);
                        break;
                    case 1:
                        habilitarBotonContinuar(view);
                        setDireccionSeleccionada(listaDirecciones.get(1));
                        deshabilitarMapa(view);
                        break;
                    case 2:
                        habilitarBotonContinuar(view);
                        setDireccionSeleccionada(listaDirecciones.get(2));
                        deshabilitarMapa(view);
                        break;
                }
            }
        });

        return view;
    }

    private void deshabilitarBotonContinuar(View view){
        Button botonContinuar = (Button) view.findViewById(R.id.boton_continuar);
        botonContinuar.setEnabled(false);
    }

    private void habilitarBotonContinuar(View view){
        Button botonContinuar = (Button) view.findViewById(R.id.boton_continuar);
        botonContinuar.setEnabled(true);
    }

    private void setDireccionSeleccionada(Direccion dir){
        this.direccionSeleccionada = dir;
    }

    private void mostrarMapa(View view){
        map = (MapView) view.findViewById(R.id.map);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);

        MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), map);
        myLocationNewOverlay.enableMyLocation();
        map.getOverlays().add(myLocationNewOverlay);

        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        GeoPoint nodoPoint = new GeoPoint(-34.906454495770475, -57.94700914013303);
        Marker nodoMarker = new Marker(map);
        nodoMarker.setPosition(nodoPoint);
        nodoMarker.setTitle("Centro Cultural Macacha Güemes");
        nodoMarker.setSubDescription("Av. 1 e/ 45 y 46 N°690");
        nodoMarker.setSnippet("Atención al público de Jueves a Sábados de 14 a 18 hs.");
        nodoMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(nodoMarker);
        waypoints.add(nodoPoint);

        ExecutorService hiloDireccion = Executors.newSingleThreadExecutor();
        hiloDireccion.submit(() -> {
            NominatimPOIProvider poiProvider = new NominatimPOIProvider("OsmNavigator/1.0");
            String direccion = "Avenida " + SessionPreferences.getDireccionRegistro(getContext()).getStreet() + " " + SessionPreferences.getDireccionRegistro(getContext()).getNumber() + ", La Plata, Argentina";
            System.out.println(direccion);
            ArrayList<POI> pois = poiProvider.getPOICloseTo(new GeoPoint(-34.9214500, -57.9545300), direccion, 1, 0.5);
            if (pois != null) {
                for (POI poi : pois) {
                    System.out.println("Desc: " + poi.mDescription);
                    Marker poiMarker = new Marker(map);
                    poiMarker.setTitle("Tu dirección");
                    poiMarker.setSnippet(poi.mDescription);
                    poiMarker.setPosition(poi.mLocation);
                    poiMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(poiMarker);
                    waypoints.add(poi.mLocation);
                }
            }


            RoadManager roadManager = new OSRMRoadManager(getContext());
            Road road = roadManager.getRoad(waypoints);
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            map.getOverlays().add(roadOverlay);
            map.invalidate();
        });

        map.getController().setZoom(14.5);
        map.getController().setCenter(waypoints.get(waypoints.size()-1));

        map.setVisibility(View.VISIBLE);

    }

    private void deshabilitarMapa(View view){
        map = (MapView) view.findViewById(R.id.map);
        map.setVisibility(View.INVISIBLE);
    }

}