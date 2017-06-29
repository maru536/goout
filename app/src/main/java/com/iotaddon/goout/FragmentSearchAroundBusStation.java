package com.iotaddon.goout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSearchAroundBusStation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSearchAroundBusStation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearchAroundBusStation extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private OnFragmentInteractionListener mListener;
    private DataManager dataManager = DataManager.getInstance();
    private HttpResponseDataUpdateListener httpResponseDataUpdateListener;
    private ArrayList<DataBusStation> arrayList;
    private View view;

    private LatLng USER_POSITION;

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private Marker selectedMarker = null;
    private HashMap<Marker, DataBusStation> hashMap;

    private View marker_root_view;
    private TextView txtMaker;

    public FragmentSearchAroundBusStation() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentSearchAroundBusStation newInstance() {
        FragmentSearchAroundBusStation fragment = new FragmentSearchAroundBusStation();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_search_around_bus_station, container, false);

        arrayList = new ArrayList<>();
        hashMap = new HashMap<>();

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_transportation_search_around_station_googlemap);

        marker_root_view = LayoutInflater.from(getContext()).inflate(R.layout.maker_bus_station_none, null);
        txtMaker = (TextView) marker_root_view.findViewById(R.id.maker_bus_statin_txt_name);


        httpResponseDataUpdateListener = new HttpResponseDataUpdateListener() {
            @Override
            public void doUpdate(String res) {
                try {
                    Log.e("transportation res", res);
                    JSONObject json = new JSONObject(res);
                    JSONObject jsonResponse = json.getJSONObject("response");
                    JSONObject jsonMsgBody = jsonResponse.getJSONObject("msgBody");
                    JSONArray jsonStationList = jsonMsgBody.getJSONArray("busStationAroundList");

                    for (int i = 0; i < jsonStationList.length(); i++) {
                        JSONObject jsonStation = jsonStationList.getJSONObject(i);
                        DataBusStation dataBusStation = new DataBusStation(jsonStation.getInt("districtCd"), jsonStation.getString("regionName"), jsonStation.getDouble("x"), jsonStation.getDouble("y"), jsonStation.getString("stationName"), jsonStation.getString("centerYn"), jsonStation.getString("mobileNo"), jsonStation.getInt("stationId"));
                        arrayList.add(dataBusStation);
                    }
                    Log.e("station size ", " = " + arrayList.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mapFragment.getMapAsync(FragmentSearchAroundBusStation.this);
            }
        };

        AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_TRANSPORTATION_BUS_AROUND_STATION, "");
        asyncTaskHttpCommunicator.setListener(httpResponseDataUpdateListener);
        asyncTaskHttpCommunicator.execute();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        for (int i = 0; i < arrayList.size(); i++) {
            addMarker(arrayList.get(i), false);
        }
        USER_POSITION = new LatLng(dataManager.getUserAddress().getLatitude(), dataManager.getUserAddress().getLongitude());

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(USER_POSITION, 15.5f));
        CircleOptions circle1KM = new CircleOptions().center(USER_POSITION) //원점
                .radius(500)      //반지름 단위 : m
                .strokeWidth(0f);  //선너비 0f : 선없음
        //.fillColor(Color.parseColor("#007ec650"));
        this.googleMap.addCircle(circle1KM);

        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);


        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        googleMap.animateCamera(center);
        changeSelectedMarker(marker);

        Intent intent = new Intent(getContext(), ActivityTransportationBusInfo.class);
        intent.putExtra("stationId",hashMap.get(marker).getStationId());
        startActivityForResult(intent, 1);

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                dataManager.setSelectedTransportation(dataManager.TYPE_TRANSPORTATION_BUS_SELECTED, true);
                AsyncTaskHttpSetConfigCommunicator asyncTaskHttpSetConfigCommunicator = new AsyncTaskHttpSetConfigCommunicator();
                asyncTaskHttpSetConfigCommunicator.execute();
                getActivity().setResult(getActivity().RESULT_OK);
                getActivity().finish();
            } else {
                dataManager.setSelectedTransportation(dataManager.TYPE_TRANSPORTATION_BUS_SELECTED, false);
                AsyncTaskHttpSetConfigCommunicator asyncTaskHttpSetConfigCommunicator = new AsyncTaskHttpSetConfigCommunicator();
                asyncTaskHttpSetConfigCommunicator.execute();
            }
        }
    }

    private void changeSelectedMarker(Marker marker) {
        // 선택했던 마커 되돌리기
        if (selectedMarker != null) {
            addMarker(selectedMarker, false);
            selectedMarker.remove();
        }

        // 선택한 마커 표시
        if (marker != null) {
            selectedMarker = addMarker(marker, true);
            marker.remove();
        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Marker addMarker(Marker marker, boolean isSelectedMarker) {
        DataBusStation temp = new DataBusStation(hashMap.get(marker));
        return addMarker(temp, isSelectedMarker);
    }

    private Marker addMarker(DataBusStation markerItem, boolean isSelectedMarker) {
        LatLng position = new LatLng(markerItem.getY(), markerItem.getX());

        txtMaker.setText(markerItem.getStationName());

        if (isSelectedMarker) {
            txtMaker.setBackgroundResource(R.drawable.border_white);
            txtMaker.setTextColor(Color.WHITE);
        } else {
            txtMaker.setBackgroundResource(R.drawable.border_blue);
            txtMaker.setTextColor(Color.BLACK);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(markerItem.getStationName());
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), marker_root_view)));

        Marker marker = googleMap.addMarker(markerOptions);
        hashMap.put(marker, markerItem);

        return marker;
    }

    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
