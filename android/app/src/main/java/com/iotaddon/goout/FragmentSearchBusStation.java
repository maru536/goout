package com.iotaddon.goout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSearchBusStation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSearchBusStation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearchBusStation extends Fragment {

    private OnFragmentInteractionListener mListener;

    private DataManager dataManager = DataManager.getInstance();
    private RecyclerView recyclerView;
    private FragmentSearchBusStation.ItemAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataBusStation> arrayList;
    private EditText editSearch;
    private HttpResponseDataUpdateListener weatherListener;

    public FragmentSearchBusStation() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentSearchBusStation newInstance() {
        FragmentSearchBusStation fragment = new FragmentSearchBusStation();
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
        View view = inflater.inflate(R.layout.fragment_fragment_search_bus_station, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_transportation_search_bus_recycler);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        adapter = new FragmentSearchBusStation.ItemAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);
        editSearch = (EditText) view.findViewById(R.id.fragment_transportation_search_bus_edit);
        editSearch.setHint("정류장 번호 숫자만 입력");

        weatherListener = new HttpResponseDataUpdateListener() {
            @Override
            public void doUpdate(String res) {
                try {
                    Log.e("transportation res", res);
                    JSONObject json = new JSONObject(res);
                    JSONObject jsonResponse = json.getJSONObject("response");
                    JSONObject jsonMsgBody = jsonResponse.getJSONObject("msgBody");
                    JSONObject jsonStation = jsonMsgBody.getJSONObject("busStationList");
                    DataBusStation dataBusStation = new DataBusStation(jsonStation.getInt("districtCd"), jsonStation.getString("regionName"), jsonStation.getDouble("x"), jsonStation.getDouble("y"), jsonStation.getString("stationName"), jsonStation.getString("centerYn"), jsonStation.getString("mobileNo"), jsonStation.getInt("stationId"));
                    arrayList.clear();
                    arrayList.add(dataBusStation);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = editSearch.getText().toString();
                if (text.length() == 5) {
                    AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_TRANSPORTATION_BUS_STATION, text);
                    asyncTaskHttpCommunicator.setListener(weatherListener);
                    asyncTaskHttpCommunicator.execute();
                }else{
                    arrayList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);

                    Intent intent = new Intent(getContext(), ActivityTransportationBusInfo.class);
                    intent.putExtra("stationId",arrayList.get(position).getStationId());
                    startActivityForResult(intent, 0);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) {
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

    class ItemAdapter extends RecyclerView.Adapter<FragmentSearchBusStation.ItemAdapter.ViewHolder> {


        private ArrayList<DataBusStation> items;
        private Context context;

        public ItemAdapter(ArrayList<DataBusStation> items, Context context) {
            this.context = context;
            this.items = items;
        }

        @Override
        public FragmentSearchBusStation.ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_station_contents, parent, false);
            FragmentSearchBusStation.ItemAdapter.ViewHolder holder = new FragmentSearchBusStation.ItemAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(FragmentSearchBusStation.ItemAdapter.ViewHolder holder, final int position) {
            DataBusStation item = items.get(position);
            holder.txtStation.setText(item.getStationName());
            holder.txtId.setText(item.getStationId()+"");
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtStation, txtId;
            LinearLayout container;

            public ViewHolder(View itemView) {
                super(itemView);
                txtStation = (TextView)itemView.findViewById(R.id.item_bus_station_contents_txt_name);
                txtId = (TextView)itemView.findViewById(R.id.item_bus_station_contents_txt_id);
                container = (LinearLayout)itemView.findViewById(R.id.item_bus_station_contents_linear_container);
            }
        }
    }
}
