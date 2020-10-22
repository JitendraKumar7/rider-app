package com.rch.rider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rch.rider.ConstantUtil.Constant;
import com.rch.rider.InterfaceUtil.ConnectionCallback;
import com.rch.rider.InterfaceUtil.OrderCallback;
import com.rch.rider.ManagementUtil.Management;
import com.rch.rider.ObjectUtil.DataObject;
import com.rch.rider.ObjectUtil.EmptyObject;
import com.rch.rider.ObjectUtil.InternetObject;
import com.rch.rider.ObjectUtil.PrefObject;
import com.rch.rider.ObjectUtil.ProgressObject;
import com.rch.rider.ObjectUtil.RequestObject;
import com.rch.rider.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HistoryFragment extends Fragment implements ConnectionCallback, OrderCallback {
    private OrderAdapter orderAdapter;
    private ArrayList<Object> objectArrayList = new ArrayList<>();


    @Override
    public void onSuccess(Object data, RequestObject requestObject) {
        if (getActivity() != null && isAdded()) {
            if (requestObject.getConnection() == Constant.CONNECTION.ORDER_HISTORY) {

                objectArrayList.clear();
                DataObject dataObject = (DataObject) data;

                for (int i = 0; i < dataObject.getObjectArrayList().size(); i++) {
                    objectArrayList.add(dataObject.getObjectArrayList().get(i));
                }

                orderAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(String data, RequestObject requestObject) {
        Utility.Logger(TAG, "Setting = " + data);
        objectArrayList.clear();
        objectArrayList.add(new EmptyObject()
                .setTitle(Utility.getStringFromRes(getActivity(), R.string.no_order))
                .setDescription(Utility.getStringFromRes(getActivity(), R.string.no_order_tagline))
                .setPlaceHolderIcon(R.drawable.em_no_order));
        orderAdapter.notifyDataSetChanged();
    }


    @Override
    public void onOrderClickListener(int position) {
        DataObject dataObject = (DataObject) objectArrayList.get(position);

        Intent intent = new Intent(getActivity(), TrackOrder.class);
        intent.putExtra(Constant.IntentKey.ORDER_DETAIL, dataObject);
        startActivity(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_order, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        objectArrayList.add(new ProgressObject());
        Management management = new Management(getActivity());

        PrefObject prefObject = management.getPreferences(new PrefObject()
                .setRetrieveLogin(true)
                .setRetrieveUserCredential(true));


        //Initialize & Setup Layout Manager with Recycler View

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (objectArrayList.get(position) instanceof EmptyObject) {
                    return 1;
                } else if (objectArrayList.get(position) instanceof InternetObject) {
                    return 1;
                } else if (objectArrayList.get(position) instanceof ProgressObject) {
                    return 1;
                } else {
                    return 1;
                }
            }
        });

        RecyclerView recyclerViewOrder =  view.findViewById(R.id.recycler_view_categories);
        recyclerViewOrder.setLayoutManager(gridLayoutManager);

        //Initialize & Setup Adapter with Recycler View

        orderAdapter = new OrderAdapter(getActivity(), objectArrayList, this);
        recyclerViewOrder.setAdapter(orderAdapter);


        //Send request to Server for retrieving TrendingPhotos Wallpapers


        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("functionality", "rider_history_order");
            jsonObject.accumulate("rider_id", prefObject.getUserId());

            management.sendRequestToServer(new RequestObject()
                    .setJson(jsonObject.toString())
                    .setConnectionType(Constant.CONNECTION_TYPE.UI)
                    .setConnection(Constant.CONNECTION.ORDER_HISTORY)
                    .setConnectionCallback(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}

