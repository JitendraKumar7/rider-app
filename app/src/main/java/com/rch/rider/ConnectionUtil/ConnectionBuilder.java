package com.rch.rider.ConnectionUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.fonts.Font;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rch.rider.ConstantUtil.Constant;
import com.rch.rider.JsonUtil.AppOfferUtil.AppOfferJson;
import com.rch.rider.JsonUtil.CardUtil.CardJson;
import com.rch.rider.JsonUtil.CategoryUtil.CategoryJson;
import com.rch.rider.JsonUtil.CouponUtil.CouponJson;
import com.rch.rider.JsonUtil.DeliveryDetailUtil.DeliveryDetailJson;
import com.rch.rider.JsonUtil.HomeUtil.HomeJson;
import com.rch.rider.JsonUtil.OrderHistoryUtil.OrderHistoryJson;
import com.rch.rider.JsonUtil.OrderUtil.OrderJson;
import com.rch.rider.JsonUtil.ProductUtil.ProductJson;
import com.rch.rider.JsonUtil.ReviewUtil.ReviewJson;
import com.rch.rider.JsonUtil.UserUtil.UserJson;
import com.rch.rider.ObjectUtil.DataObject;
import com.rch.rider.ObjectUtil.RequestObject;
import com.rch.rider.R;
import com.rch.rider.Utility.Utility;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import needle.Needle;
import needle.UiRelatedProgressTask;


public class ConnectionBuilder {
    private String TAG = ConnectionBuilder.class.getName();
    ACProgressFlower acProgressFlower = null;
    ProgressDialog progressDialog;

    public ConnectionBuilder(final RequestObject requestObject) {

        if (!Utility.checkConnection(requestObject.getContext())) {

            if (requestObject.getInternetCallback() != null) {
                requestObject.getInternetCallback().onConnectivityFailed();
                return;
            }

            Utility.Toaster(requestObject.getContext(), Utility.getStringFromRes(requestObject.getContext(), R.string.internet_not_available), Toast.LENGTH_SHORT);
            return;
        }

        Utility.extraData(TAG, "Json = " + requestObject.toString());

        if (requestObject.getConnectionType() == Constant.CONNECTION_TYPE.UI) {

            if (requestObject.getConnection() == Constant.CONNECTION.SOCIAL_LOGIN
                    || requestObject.getConnection() == Constant.CONNECTION.PRIVACY_POLICY
                    || requestObject.getConnection() == Constant.CONNECTION.FORGOT
                    || requestObject.getConnection() == Constant.CONNECTION.UPDATE) {


                if (Utility.isEmptyString(requestObject.getLoadingText()))
                    acProgressFlower = getACProgressFlower(requestObject.getContext(),
                            Utility.getStringFromRes(requestObject.getContext(), R.string.progress));
                else
                    acProgressFlower = getACProgressFlower(requestObject.getContext(),
                            requestObject.getLoadingText());

                    acProgressFlower.show();

            }

            Needle.onBackgroundThread().execute(new UiRelatedProgressTask<String, Integer>() {

                @Override
                protected void onProgressUpdate(Integer integer) {

                }

                @Override
                protected String doWork() {

                    if (Constant.REQUEST.valueOf(requestObject.getRequestType()) == Constant.REQUEST.GET) {
                        return Connection.makeRequest(requestObject.getServerUrl(), requestObject.getRequestType());
                    } else if (Constant.REQUEST.valueOf(requestObject.getRequestType()) == Constant.REQUEST.POST) {
                        return Connection.makeRequest(requestObject.getServerUrl(), requestObject.getJson(), requestObject.getRequestType());
                    } else
                        return Connection.makeRequest(requestObject.getServerUrl(), requestObject.getRequestType());

                }

                @Override
                protected void thenDoUiRelatedWork(String data) {

                    Utility.extraData(TAG, "Response = " + data);

                    if (Utility.isEmptyString(data)) {
                        return;
                    }

                    if (data.equals(Constant.ImportantMessages.CONNECTION_ERROR)) {
                        return;
                    }

                    Gson gson = new Gson();
                    Object object = null;
                    String responseCode = null;
                    String responseMessage = null;
                    DataObject dataObject = null;

                    if (requestObject.getConnection() == Constant.CONNECTION.CONFIGURATION) {

                        object = gson.fromJson(data, AppOfferJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.HOME) {

                        object = gson.fromJson(data, HomeJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.NEAREST) {

                        object = gson.fromJson(data, HomeJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.ALL_CATEGORIES) {

                        object = gson.fromJson(data, AppOfferJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.SEARCH) {

                        object = gson.fromJson(data, HomeJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.RESTAURANT_DETAIL) {

                        object = gson.fromJson(data, CategoryJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.PRODUCT_MENU) {

                        object = gson.fromJson(data, ProductJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.REDEEM_COUPON) {

                        object = gson.fromJson(data, CouponJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.PAYMENT_CARDS
                            || requestObject.getConnection() == Constant.CONNECTION.ADD_CARD) {

                        object = gson.fromJson(data, CardJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.ALL_FAVOURITES) {

                        object = gson.fromJson(data, HomeJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.ALL_COMMENT) {

                        object = gson.fromJson(data, ReviewJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.SIGN_UP) {

                        object = gson.fromJson(data, UserJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.LOGIN) {

                        object = gson.fromJson(data, UserJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.SOCIAL_LOGIN) {

                        object = gson.fromJson(data, UserJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.ORDER_HISTORY) {

                        object = gson.fromJson(data, OrderHistoryJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.SPECIFIC_RESTAURANT) {

                        object = gson.fromJson(data, HomeJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.CHECK_OUT) {

                        object = gson.fromJson(data, OrderJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.DELIVERY_CHARGES) {

                        object = gson.fromJson(data, DeliveryDetailJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.FORGOT) {

                        object = gson.fromJson(data, UserJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    } else if (requestObject.getConnection() == Constant.CONNECTION.UPDATE) {

                        object = gson.fromJson(data, UserJson.class);
                        dataObject = DataObject.getDataObject(requestObject, object);

                    }


                    responseCode = dataObject.getCode();
                    responseMessage = dataObject.getMessage();


                    if (acProgressFlower != null && acProgressFlower.isShowing())
                        acProgressFlower.dismiss();

                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();

                    if (requestObject.getConnectionCallback() != null) {

                        if (responseCode.equals(Constant.ErrorCodes.success_code)) {
                            requestObject.getConnectionCallback().onSuccess(dataObject, requestObject);
                        } else if (responseCode.equals(Constant.ErrorCodes.error_code)) {

                            if (requestObject.getConnection() == Constant.CONNECTION.MENU_DETAIL) {
                                requestObject.getConnectionCallback().onSuccess(dataObject, requestObject);
                            } else {
                                requestObject.getConnectionCallback().onError(responseMessage, requestObject);
                            }

                        } else {
                            requestObject.getConnectionCallback().onError(responseMessage, requestObject);
                        }

                    }

                }


            });

        }

    }


    public static class CreateConnection {
        private RequestObject requestObject;

        public CreateConnection setRequestObject(RequestObject requestObject) {
            this.requestObject = requestObject;
            return this;
        }

        public ConnectionBuilder buildConnection() {
            return new ConnectionBuilder(requestObject);
        }

    }

    /**
     * <p>It is used to retrieve Progress Dialog object</p>
     *
     * @param context
     * @param progress
     * @return
     */
    private ACProgressFlower getACProgressFlower(Context context, String progress) {

        ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE).text(progress)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;


    }


    /**
     * <p>It is used to show Downloading progress dialog</p>
     *
     * @param context
     * @return
     */
    private ProgressDialog getDownloadProgressDialog(Context context, String loadingText) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        // Set your progress dialog Title
        mProgressDialog.setTitle(loadingText);
        // Set your progress dialog Message
        mProgressDialog.setMessage(loadingText);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //mProgressDialog.setMax(100);
        return mProgressDialog;
    }


}
