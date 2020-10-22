package com.rch.rider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rch.rider.ConstantUtil.Constant;
import com.rch.rider.InterfaceUtil.ConnectionCallback;
import com.rch.rider.ManagementUtil.Management;
import com.rch.rider.ObjectUtil.DataObject;
import com.rch.rider.ObjectUtil.PrefObject;
import com.rch.rider.ObjectUtil.RequestObject;
import com.rch.rider.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText editEmail;
    private EditText editPassword;

    private TextView txtLogin;
    private Management management;

    @Override
    public void onClick(View v) {
        if (v == txtLogin) {
            if (Utility.isEmptyString(editEmail.getText().toString())) {
                return;
            }
            if (Utility.isEmptyString(editPassword.getText().toString())) {
                return;
            }

            showLoginBottomSheet(this);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Utility.changeAppTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);

        txtLogin = findViewById(R.id.txt_login);


        management = new Management(this);
        PrefObject userData = management.getPreferences(new PrefObject()
                .setRetrieveUserRemember(true)
                .setRetrieveUserCredential(true));

        editEmail.setText(userData.getUserEmail());
        editPassword.setText(userData.getUserPassword());

        txtLogin.setOnClickListener(this);
    }

    private void showLoginBottomSheet(final Context context) {
        final View view = getLayoutInflater().inflate(R.layout.process_order_sheet_layout, null);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("functionality", "rider_login");
            jsonObject.accumulate("email", editEmail.getText().toString());
            jsonObject.accumulate("password", editPassword.getText().toString());

            management.sendRequestToServer(new RequestObject()
                    .setJson(jsonObject.toString())
                    .setConnection(Constant.CONNECTION.LOGIN)
                    .setConnectionType(Constant.CONNECTION_TYPE.UI)
                    .setConnectionCallback(new ConnectionCallback() {
                        @Override
                        public void onSuccess(Object data, RequestObject requestObject) {
                            bottomSheetDialog.dismiss();

                            if (data != null && requestObject != null) {

                                DataObject dataObject = (DataObject) data;


                                management.savePreferences(new PrefObject()
                                        .setSaveUserRemember(true)
                                        .setUserRemember(true));


                                management.savePreferences(new PrefObject()
                                        .setSaveLogin(true)
                                        .setLogin(true));

                                management.savePreferences(new PrefObject()
                                        .setSaveUserCredential(true)
                                        .setLoginType(dataObject.getLogin_type())
                                        .setUserId(dataObject.getUser_id())
                                        .setFirstName(dataObject.getUser_fName())
                                        .setLastName(dataObject.getUser_lName())
                                        .setUserPhone(dataObject.getPhone())
                                        .setUserPassword(dataObject.getUser_password())
                                        .setUserEmail(dataObject.getUser_email())
                                        .setPictureUrl(dataObject.getUser_picture()));

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }

                        }

                        @Override
                        public void onError(String data, RequestObject requestObject) {

                            bottomSheetDialog.dismiss();
                            Utility.Toaster(context, data, Toast.LENGTH_SHORT);

                        }
                    }));
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    //http://wowwishes.in/rch/food_delivery_home.php
    //{
    //	"functionality": "rider_current_order",
    //	"rider_id": "1"
    //}


    //{
    //  "functionality": push_order_completed_status
    //completed
    //
    //{
    //  "functionality":push_order_delivered_status
    //delivered

}
