package com.rch.rider.InterfaceUtil;


import com.rch.rider.ObjectUtil.RequestObject;

public interface ConnectionCallback {

    void onSuccess(Object data, RequestObject requestObject);

    void onError(String data, RequestObject requestObject);


}
