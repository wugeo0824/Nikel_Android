package com.media2359.nickel.network;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.media2359.nickel.event.OnForgotPasswordEvent;
import com.media2359.nickel.event.OnLoginEvent;
import com.media2359.nickel.event.OnLoginWithOtpEvent;
import com.media2359.nickel.event.OnProfileChangedEvent;
import com.media2359.nickel.event.OnProfileLoadedEvent;
import com.media2359.nickel.event.OnRegisterConsumerEvent;
import com.media2359.nickel.event.OnResetPasswordEvent;
import com.media2359.nickel.event.OnTransfersLoadedEvent;
import com.media2359.nickel.managers.UserSessionManager;
import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.network.responses.BaseResponse;
import com.media2359.nickel.network.responses.LoginResponse;
import com.media2359.nickel.network.responses.TransfersResponse;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Xijun on 12/7/16.
 */
public class RequestHandler {

    private static final String TAG = "RequestHandler";

    public static Call registerConsumer(String mobile, String password) {

        Call<BaseResponse> call = NikelService.getApiManager().registerConsumer(mobile, password);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    OnRegisterConsumerEvent event = new OnRegisterConsumerEvent(true, response.body().getMessage());
                    EventBus.getDefault().post(event);
                } else {
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
                if (response.isSuccessful()) {
                    UserSessionManager.getInstance().setToken(response.body().getToken());
                    OnLoginWithOtpEvent event = new OnLoginWithOtpEvent(true, response.message());
                    EventBus.getDefault().post(event);
                } else {
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

    public static Call login(String mobile, String password) {
        Call<LoginResponse> call = NikelService.getApiManager().login(mobile, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    UserSessionManager.getInstance().setToken(response.body().getToken());
                    OnLoginEvent event = new OnLoginEvent(true, response.message());
                    EventBus.getDefault().post(event);
                } else {
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
                if (response.isSuccessful()) {
                    OnForgotPasswordEvent event = new OnForgotPasswordEvent(true, response.message());
                    EventBus.getDefault().post(event);
                } else {
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
                if (response.isSuccessful()) {
                    OnResetPasswordEvent event = new OnResetPasswordEvent(true, response.message());
                    EventBus.getDefault().post(event);
                } else {
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

    public static Call updateProfile(MyProfile profile, MultipartBody.Part frontBody, MultipartBody.Part backBody) {

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), profile.getFullName());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), profile.getStreetAddress());
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), profile.getCity());
        RequestBody postalCode = RequestBody.create(MediaType.parse("text/plain"), profile.getPostalCode());
        RequestBody documentType = RequestBody.create(MediaType.parse("text/plain"), profile.getDocumentType());
        RequestBody documentID = RequestBody.create(MediaType.parse("text/plain"), profile.getDocumentID());
        RequestBody dateOfBirth = RequestBody.create(MediaType.parse("text/plain"), profile.getDateOfBirth());

        RequestBody language = RequestBody.create(MediaType.parse("text/plain"), "en");


        Call<BaseResponse> call = NikelService.getApiManager().updateProfile(name, address, city, postalCode, documentType,
                documentID, dateOfBirth, frontBody, backBody, language);
        try {
            final Response<BaseResponse> response = call.execute();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (response.isSuccessful()) {
                        EventBus.getDefault().post(new OnProfileChangedEvent(true, response.body().getMessage()));
                    } else {
                        EventBus.getDefault().post(new OnProfileChangedEvent(false, convert400Response(response)));
                    }
                }
            });

        } catch (final IOException e) {
            e.printStackTrace();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new OnProfileChangedEvent(false, e.getLocalizedMessage()));
                }
            });

        }

        return call;
    }

    public static Call getProfile() {
        Call<MyProfile> call = NikelService.getApiManager().getMyProfile();
        call.enqueue(new Callback<MyProfile>() {
            @Override
            public void onResponse(Call<MyProfile> call, Response<MyProfile> response) {
                if (response.isSuccessful()) {
                    EventBus.getDefault().post(new OnProfileLoadedEvent(true, response.message(), response.body()));
                } else {
                    EventBus.getDefault().post(new OnProfileLoadedEvent(false, convert400Response(response), null));
                }
            }

            @Override
            public void onFailure(Call<MyProfile> call, Throwable t) {
                EventBus.getDefault().post(new OnProfileLoadedEvent(false, t.getLocalizedMessage(), null));
            }
        });
        return call;
    }

    public static Call getTransfers(int page) {
        Call<TransfersResponse> call = NikelService.getApiManager().getTransfers(page);
        call.enqueue(new Callback<TransfersResponse>() {
            @Override
            public void onResponse(Call<TransfersResponse> call, Response<TransfersResponse> response) {
                if (response.isSuccessful()) {
                    EventBus.getDefault().post(new OnTransfersLoadedEvent(true, response.message(), response.body().getTransfers()));
                } else {
                    EventBus.getDefault().post(new OnTransfersLoadedEvent(false, convert400Response(response), null));
                }
            }

            @Override
            public void onFailure(Call<TransfersResponse> call, Throwable t) {
                EventBus.getDefault().post(new OnTransfersLoadedEvent(false, t.getLocalizedMessage(), null));
            }
        });
        return call;
    }


    public static String convert400Response(Response response) {
        String error;
        try {
            error = response.errorBody().string();
            BaseResponse baseResponse = new Gson().fromJson(error, BaseResponse.class);
            return baseResponse.getError();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
