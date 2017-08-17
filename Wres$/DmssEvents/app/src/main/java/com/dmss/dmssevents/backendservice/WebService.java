package com.dmss.dmssevents.backendservice;

import android.content.Context;

import com.dmss.dmssevents.interfaces.WebServiceResponseCallBack;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jaya.krishna on 20-Mar-17.
 */
public class WebService {
    OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Context context;

    public WebService(Context context) {
        client = new OkHttpClient();
        client.newBuilder().connectTimeout(30000, TimeUnit.MILLISECONDS).readTimeout(30000, TimeUnit.MILLISECONDS).build();
        this.context = context;
    }

    public void getData(String url, final WebServiceResponseCallBack callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String exception;
                if(e.fillInStackTrace().toString().contains("TimeOutException")||e.fillInStackTrace().toString().contains("java")){
                    exception = "Network is slow,Please try again";
                }else{
                    exception = e.fillInStackTrace().toString();
                }
                callback.onServiceCallFail(exception);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    if (response != null) {
                        callback.onServiceCallSuccess(response.body().string());
                    } else {
                        if (response.message() != null) {
                            callback.onServiceCallFail(response.message());
                        } else {
                            callback.onServiceCallFail("No data found!");
                        }

                    }

                }
            }
        });
    }

    public void deleteData(String url, final WebServiceResponseCallBack callback) {
        final Request request = new Request.Builder().url(url).delete().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String exception;
                if(e.fillInStackTrace().toString().contains("TimeOutException")||e.fillInStackTrace().toString().contains("java")){
                    exception = "Network is slow,Please try again";
                }else{
                    exception = e.fillInStackTrace().toString();
                }
                callback.onServiceCallFail(exception);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    if (response != null) {
                        callback.onServiceCallSuccess(response.body().string());
                    } else {
                        if (response.message() != null) {
                            callback.onServiceCallFail(response.message());
                        } else {
                            callback.onServiceCallFail("No data found!");
                        }

                    }

                }
            }
        });
    }

    public void getDataWithHeaders(String url, String base64String, final WebServiceResponseCallBack callback) {
        final Request request = new Request.Builder().url(url).addHeader("content-type", "application/json")
                .addHeader("authorization", "Basic " + base64String).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String exception;
                if(e.fillInStackTrace().toString().contains("TimeOutException")||e.fillInStackTrace().toString().contains("java")){
                    exception = "Network is slow,Please try again";
                }else{
                    exception = e.fillInStackTrace().toString();
                }
                callback.onServiceCallFail(exception);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    if (response != null) {
                        callback.onServiceCallSuccess(response.body().string());
                    } else {
                        callback.onServiceCallFail("WebAPI Not Responding ");
                    }
                } else {
                    callback.onServiceCallFail(response.message());
                }
            }
        });
    }

    public void postData(String url, String json, final WebServiceResponseCallBack callback) {
        RequestBody reqBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(reqBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String exception;
                if(e.fillInStackTrace().toString().contains("TimeOutException")||e.fillInStackTrace().toString().contains("java")){
                    exception = "Network is slow,Please try again";
                }else{
                    exception = e.fillInStackTrace().toString();
                }
                callback.onServiceCallFail(exception);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200 || response.code() == 201) {
                    if (response != null) {
                        callback.onServiceCallSuccess(response.body().string());
                    } else {
                        callback.onServiceCallFail("WebAPI Not Responding ");
                    }
                } else {
                    callback.onServiceCallFail("Data unavailable");
                }
            }
        });
    }

}