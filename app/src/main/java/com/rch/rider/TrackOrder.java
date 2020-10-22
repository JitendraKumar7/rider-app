package com.rch.rider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.liefery.android.vertical_stepper_view.VerticalStepperView;
import com.rch.rider.ConstantUtil.Constant;
import com.rch.rider.ManagementUtil.Management;
import com.rch.rider.ObjectUtil.DataObject;
import com.rch.rider.ObjectUtil.PrefObject;
import com.rch.rider.ObjectUtil.StepperObject;
import com.rch.rider.Utility.Utility;

import java.util.ArrayList;

public class TrackOrder extends AppCompatActivity {
    private VerticalStepperView listStepper;
    private MainStepperAdapter mainStepperAdapter;
    private ArrayList<Object> objectArrayList = new ArrayList<>();
    private TextView txtEta;
    private TextView txtAmount;
    private TextView txtOrderId;
    private TextView txtDate;
    private TextView txtDelivery;
    private Management management;
    private DataObject dataObject;
    private ImageView imagePaymentType;
    private TextView txtPaymentType;
    private LinearLayout listViewOrderDetail;
    private PrefObject prefObject;
    private AppCompatRatingBar riderRating;
    private TextView txtDeliveryCharges;


    public View getView(Object objects) {

        View view = LayoutInflater.from(this).inflate(R.layout.order_detail_item_layout, null);

        LinearLayout layoutProduct = view.findViewById(R.id.layout_product);
        TextView txtProduct = view.findViewById(R.id.txt_product);
        TextView txtQuantity = view.findViewById(R.id.txt_quantity);
        TextView txtPrice = view.findViewById(R.id.txt_price);

        txtProduct.setText(((DataObject) objects).getOrder_product_name());
        txtQuantity.setText("(x" + ((DataObject) objects).getOrder_product_quantity() + ")");
        txtPrice.setText(((DataObject) objects).getOrder_product_price().replace("INR", "₹"));

        layoutProduct.setPadding(0, 12, 0, 12);

        return view;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);


        dataObject = getIntent().getParcelableExtra(Constant.IntentKey.ORDER_DETAIL);

        management = new Management(this);
        prefObject = management.getPreferences(new PrefObject()
                .setRetrieveUserCredential(true));

        txtDate = (TextView) findViewById(R.id.txt_date);
        txtOrderId = (TextView) findViewById(R.id.txt_order_id);
        txtAmount = (TextView) findViewById(R.id.txt_amount);
        txtEta = (TextView) findViewById(R.id.txt_eta);
        txtPaymentType = findViewById(R.id.txt_payment_type);
        imagePaymentType = findViewById(R.id.image_payment_type);
        txtDeliveryCharges = findViewById(R.id.txt_delivery_charge);



        txtDate.setText(String.format("%s , %s", dataObject.getOrder_delivery_date(), dataObject.getOrder_delivery_time()));
        txtAmount.setText(String.format("%s : %s", Utility.getStringFromRes(this, R.string.total), dataObject.getOrder_price().replace("INR", "₹")));
        txtEta.setText(String.format("%s : %s", Utility.getStringFromRes(this, R.string.eta), dataObject.getDelivery_time()));
        txtOrderId.setText(String.format("%s # %s", Utility.getStringFromRes(this, R.string.order_id), dataObject.getOrder_id()));
        txtPaymentType.setText(dataObject.getPaymentType());

        ///View listHeaderView = getLayoutInflater().inflate(R.layout.product_, null, false);

        listViewOrderDetail = findViewById(R.id.list_view_order_detail);

        ArrayList<Object> arrayList = new ArrayList<Object>(dataObject.getProduct_order_detail_list());
        for (Object object : arrayList) {

            listViewOrderDetail.addView(getView(object));
        }

        txtDeliveryCharges.setText(dataObject.getDelivery_fee());

        if (dataObject.getPaymentType().equalsIgnoreCase(Utility.getStringFromRes(this, R.string.cod))) {
            imagePaymentType.setImageResource(R.drawable.ic_cod);
        } else {
            imagePaymentType.setImageResource(R.drawable.ic_credit_card);
        }

        objectArrayList.add(new StepperObject()
                .setTitle(Utility.getStringFromRes(this, R.string.order_placed))
                .setDescription(Utility.getStringFromRes(this, R.string.order_place_tagline))
                .setIcon(R.drawable.ic_order));

        objectArrayList.add(new StepperObject()
                .setTitle(Utility.getStringFromRes(this, R.string.process_order))
                .setDescription(Utility.getStringFromRes(this, R.string.process_order_tagline))
                .setIcon(R.drawable.ic_order_process));

        objectArrayList.add(new StepperObject()
                .setTitle(Utility.getStringFromRes(this, R.string.on_the_way))
                .setDescription(Utility.getStringFromRes(this, R.string.on_the_Way_tagline))
                .setIcon(R.drawable.ic_bike));

        objectArrayList.add(new StepperObject()
                .setTitle(Utility.getStringFromRes(this, R.string.received_successfully))
                .setDescription(Utility.getStringFromRes(this, R.string.success_tagline))
                .setIcon(R.drawable.ic_success));

        listStepper =  findViewById(R.id.list_stepper);
        mainStepperAdapter = new MainStepperAdapter(this, objectArrayList);
        listStepper.setStepperAdapter(mainStepperAdapter);

        if (dataObject.getOrder_status().equalsIgnoreCase(Utility.getStringFromRes(this, R.string.project_status_ready))) {

            if (mainStepperAdapter.hasNext())
                mainStepperAdapter.next();

        } else if (dataObject.getOrder_status().equalsIgnoreCase(Utility.getStringFromRes(this, R.string.project_status_completed))) {

            if (mainStepperAdapter.hasNext()) {
                mainStepperAdapter.next();
                mainStepperAdapter.next();
            }

        }

    }

}



