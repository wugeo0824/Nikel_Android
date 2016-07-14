package com.media2359.nickel.network;

import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.network.responses.BaseResponse;
import com.media2359.nickel.network.responses.LoginResponse;
import com.media2359.nickel.network.responses.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Xijun on 12/7/16.
 */
public interface NikelApi {

    String API_VERSION = "/api";
    String AUTH_PREFIX = "/auth";

    @FormUrlEncoded
    @POST(API_VERSION + AUTH_PREFIX + "/registerConsumer")
    Call<BaseResponse> registerConsumer(@Field("mobilePhone") String mobile, @Field("password") String password);

    @FormUrlEncoded
    @POST(API_VERSION + AUTH_PREFIX + "/loginConsumerOtp")
    Call<LoginResponse> loginWithOtp(@Field("mobilePhone") String mobile, @Field("otp")  String otp);

    @FormUrlEncoded
    @POST(API_VERSION + AUTH_PREFIX + "/loginConsumer")
    Call<LoginResponse> login(@Field("mobilePhone") String mobile, @Field("password") String password);

    @FormUrlEncoded
    @POST(API_VERSION + AUTH_PREFIX + "/resetPasswordConsumer")
    Call<BaseResponse> resetPassword(@Field("mobilePhone") String mobile, @Field("otp")  String otp, @Field("password") String password);

    @FormUrlEncoded
    @POST(API_VERSION + AUTH_PREFIX + "/forgotPasswordConsumer")
    Call<BaseResponse> forgotPassword(@Field("mobilePhone") String mobile);

    @GET(API_VERSION + "/user/me")
    Call<ProfileResponse> getMyProfile();

    @PUT(API_VERSION + "/user/me")
    Call<BaseResponse> updateProfile(@Body MyProfile profile);
}
