package com.media2359.nickel.network;

import android.util.Log;

import com.google.gson.Gson;
import com.media2359.nickel.event.OnForgotPasswordEvent;
import com.media2359.nickel.event.OnLoginEvent;
import com.media2359.nickel.event.OnLoginWithOtpEvent;
import com.media2359.nickel.event.OnRegisterConsumerEvent;
import com.media2359.nickel.event.OnResetPasswordEvent;
import com.media2359.nickel.managers.UserSessionManager;
import com.media2359.nickel.network.responses.BaseResponse;
import com.media2359.nickel.network.responses.LoginResponse;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Xijun on 12/7/16.
 */
public class RequestHandler {

    public static Call registerConsumer(String mobile, String password) {

        Call<BaseResponse> call = NikelService.getApiManager().registerConsumer(mobile, password);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccess()){
                    OnRegisterConsumerEvent event = new OnRegisterConsumerEvent(true, response.body().getMessage());
                    EventBus.getDefault().post(event);
                }else {
                    OnRegisterConsumerEvent event = new OnRegisterConsumerEvent(false, convert400Response(response));
                    EventBus.getDefault().post(event);
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                OnRegisterConsumerEvent event = new OnRegisterConsumerEvent(false, t.getLocalizedMessage());
                EventBus.getDefault().post(event);
            }
        });
        return call;
    }

    public static Call loginWithOtp(String mobile, String otp) {
        Call<LoginResponse> call = NikelService.getApiManager().loginWithOtp(mobile, otp);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccess()){
                    UserSessionManager.getInstance().setToken(response.body().getToken());
                    OnLoginWithOtpEvent event = new OnLoginWithOtpEvent(true, response.message());
                    EventBus.getDefault().post(event);
                }else {
                    OnLoginWithOtpEvent event = new OnLoginWithOtpEvent(false, convert400Response(response));
                    EventBus.getDefault().post(event);
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                OnLoginWithOtpEvent event = new OnLoginWithOtpEvent(false, t.getLocalizedMessage());
                EventBus.getDefault().post(event);
            }
        });
        return call;
    }

    public static Call login (String mobile, String password) {
        Call<LoginResponse> call = NikelService.getApiManager().login(mobile, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccess()){
                    UserSessionManager.getInstance().setToken(response.body().getToken());
                    OnLoginEvent event = new OnLoginEvent(true, response.message());
                    EventBus.getDefault().post(event);
                }else {
                    OnLoginEvent event = new OnLoginEvent(false, convert400Response(response));
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                OnLoginEvent event = new OnLoginEvent(false, t.getLocalizedMessage());
                EventBus.getDefault().post(event);
            }
        });
        return call;
    }

    public static Call forgotPassword(String mobile) {
        Call<BaseResponse> call = NikelService.getApiManager().forgotPassword(mobile);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccess()){
                    OnForgotPasswordEvent event = new OnForgotPasswordEvent(true, response.message());
                    EventBus.getDefault().post(event);
                }else {
                    OnForgotPasswordEvent event = new OnForgotPasswordEvent(false, convert400Response(response));
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                OnForgotPasswordEvent event = new OnForgotPasswordEvent(false, t.getLocalizedMessage());
                EventBus.getDefault().post(event);
            }
        });
        return call;
    }

    public static Call resetPassword(String mobile, String password, String otp) {
        Call<BaseResponse> call = NikelService.getApiManager().resetPassword(mobile, otp, password);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccess()){
                    OnResetPasswordEvent event = new OnResetPasswordEvent(true, response.message());
                    EventBus.getDefault().post(event);
                }else {
                    OnResetPasswordEvent event = new OnResetPasswordEvent(false, convert400Response(response));
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                OnResetPasswordEvent event = new OnResetPasswordEvent(false, t.getLocalizedMessage());
                EventBus.getDefault().post(event);
            }
        });
        return call;
    }

    private static String convert400Response(Response response) {
        String error;
        try {
            error = response.errorBody().string();
            BaseResponse baseResponse = new Gson().fromJson(error, BaseResponse.class);
            return baseResponse.getError();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
