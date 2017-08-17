package com.dmss.dmssevents.interfaces;

/**
 * Created by jaya.krishna on 20-Mar-17.
 */
public interface WebServiceResponseCallBack {
    public void onServiceCallSuccess(String result);

    public void onServiceCallFail(String error);
}
