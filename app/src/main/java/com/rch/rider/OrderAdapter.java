package com.rch.rider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rch.rider.InterfaceUtil.OrderCallback;
import com.rch.rider.ObjectUtil.DataObject;
import com.rch.rider.ObjectUtil.EmptyObject;
import com.rch.rider.ObjectUtil.ProgressObject;
import com.rch.rider.Utility.Utility;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.util.ArrayList;


/**
 * Created by hp on 5/5/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter {
    private boolean isProfile;
    private int NO_DATA_VIEW = 1;
    private int ORDER_HISTORY_VIEW = 2;
    private int PROGRESS_VIEW = 3;
    private Context context;
    private ArrayList<Object> dataArray = new ArrayList<>();
    private OrderCallback orderCallback;


    public OrderAdapter(Context context, ArrayList<Object> dataArray, OrderCallback orderCallback) {
        this.context = context;
        this.dataArray = dataArray;
        this.orderCallback = orderCallback;
    }


    @Override
    public int getItemViewType(int position) {

        if (dataArray.get(position) instanceof EmptyObject) {
            return NO_DATA_VIEW;
        } else if (dataArray.get(position) instanceof DataObject) {
            return ORDER_HISTORY_VIEW;
        } else if (dataArray.get(position) instanceof ProgressObject) {
            return PROGRESS_VIEW;
        }

        return NO_DATA_VIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == NO_DATA_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_item_layout, parent, false);
            viewHolder = new EmptyHolder(view);

        } else if (viewType == ORDER_HISTORY_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
            viewHolder = new OrderHolder(view);

        } else if (viewType == PROGRESS_VIEW) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item_layout, parent, false);
            viewHolder = new ProgressHolder(view);

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ProgressHolder) {

            //LookUpObject lookUpObject = (LookUpObject) dataArray.get(position);
            ProgressHolder lookUpHolder = (ProgressHolder) holder;


        } else if (holder instanceof EmptyHolder) {

            EmptyHolder emptyHolder = (EmptyHolder) holder;
            EmptyObject emptyObject = (EmptyObject) dataArray.get(position);

            emptyHolder.imageIcon.setImageResource(emptyObject.getPlaceHolderIcon());
            emptyHolder.txtTitle.setText(emptyObject.getTitle());
            emptyHolder.txtDescription.setText(emptyObject.getDescription());


        } else if (holder instanceof OrderHolder) {


            final OrderHolder orderHolder = (OrderHolder) holder;
            DataObject dataObject = (DataObject) dataArray.get(position);

            orderHolder.txtName.setText(String.format("Order Id - #%s", dataObject.getOrder_id()));
            orderHolder.txtDate.setText(dataObject.getOrder_delivery_date());
            orderHolder.txtTime.setText(dataObject.getOrder_delivery_time());
            orderHolder.txtCharges.setText(dataObject.getOrder_price().replace("INR", "₹"));
            orderHolder.txtStatus.setText(dataObject.getOrder_status().toUpperCase());

            if (dataObject.getOrder_status().equalsIgnoreCase(Utility.getStringFromRes(context, R.string.project_status_ready))) {
                orderHolder.txtStatus.setTextColor(Utility.getColourFromRes(context, R.color.order_ready));
            } else if (dataObject.getOrder_status().equalsIgnoreCase(Utility.getStringFromRes(context, R.string.project_status_received))) {
                orderHolder.txtStatus.setTextColor(Utility.getColourFromRes(context, R.color.colorPrimaryDark));
            } else if (dataObject.getOrder_status().equalsIgnoreCase(Utility.getStringFromRes(context, R.string.project_status_completed))) {
                orderHolder.txtStatus.setTextColor(Utility.getColourFromRes(context, R.color.order_on_the_way));
                orderHolder.txtStatus.setText(Utility.getStringFromRes(context, R.string.on_the_way));
            } else if (dataObject.getOrder_status().equalsIgnoreCase(Utility.getStringFromRes(context, R.string.project_status_delivered))) {
                orderHolder.txtStatus.setTextColor(Utility.getColourFromRes(context, R.color.order_delivered));
            }

            orderHolder.layoutOrder.setTag(position);
            orderHolder.layoutOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) orderHolder.layoutOrder.getTag();
                    if (orderCallback != null) {
                        orderCallback.onOrderClickListener(pos);
                    }
                }
            });


            /*GlideApp.with(context).load(Constant.ServerInformation.PICTURE_URL + dataObject.getObject_logo())
                    .into(orderHolder.imageLogo);*/


        }


    }

    @Override
    public int getItemCount() {
        return dataArray.size();

    }

    protected class EmptyHolder extends RecyclerView.ViewHolder {
        private ImageView imageIcon;
        private TextView txtTitle;
        private TextView txtDescription;

        public EmptyHolder(View view) {
            super(view);

            txtTitle = view.findViewById(R.id.txt_title);
            imageIcon = view.findViewById(R.id.image_icon);
            txtDescription = view.findViewById(R.id.txt_description);
        }
    }

    protected class OrderHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutOrder;
        private TextView txtName;
        private TextView txtDate;
        private TextView txtTime;
        private TextView txtRating;
        private TextView txtDeliveryTime;
        private TextView txtCharges;
        private TextView txtStatus;
        private ImageView imageFavourite;

        public OrderHolder(View view) {
            super(view);

            layoutOrder = (LinearLayout) view.findViewById(R.id.layout_order);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
            txtTime = (TextView) view.findViewById(R.id.txt_time);
            txtRating = (TextView) view.findViewById(R.id.txt_rating);
            txtDeliveryTime = (TextView) view.findViewById(R.id.txt_delivery_time);
            txtCharges = (TextView) view.findViewById(R.id.txt_charges);
            txtStatus = (TextView) view.findViewById(R.id.txt_status);
            imageFavourite = (ImageView) view.findViewById(R.id.image_favourite);

        }
    }

    protected class ProgressHolder extends RecyclerView.ViewHolder {
        private GeometricProgressView progressView;

        public ProgressHolder(View view) {
            super(view);
            progressView = view.findViewById(R.id.progressView);
        }

    }

}
