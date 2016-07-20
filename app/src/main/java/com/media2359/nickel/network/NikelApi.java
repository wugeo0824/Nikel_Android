package com.media2359.nickel.network;

import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.network.responses.BaseResponse;
import com.media2359.nickel.network.responses.LoginResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

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
    Call<LoginResponse> loginWithOtp(@Field("mobilePhone") String mobile, @Field("otp") String otp);

    @FormUrlEncoded
    @POST(API_VERSION + AUTH_PREFIX + "/loginConsumer")
    Call<LoginResponse> login(@Field("mobilePhone") String mobile, @Field("password") String password);

    @FormUrlEncoded
    @POST(API_VERSION + AUTH_PREFIX + "/resetPasswordConsumer")
    Call<BaseResponse> resetPassword(@Field("mobilePhone") String mobile, @Field("otp") String otp, @Field("password") String password);

    @FormUrlEncoded
    @POST(API_VERSION + AUTH_PREFIX + "/forgotPasswordConsumer")
    Call<BaseResponse> forgotPassword(@Field("mobilePhone") String mobile);

    @GET(API_VERSION + "/user/me")
    Call<MyProfile> getMyProfile();

    @Multipart
    @PUT(API_VERSION + "/user/me")
    Call<BaseResponse> updateProfile(@Part("name") RequestBody name, @Part("address") RequestBody address,
                                     @Part("city") RequestBody city, @Part("postalCode") RequestBody postalCode,
                                     @Part("documentType") RequestBody documentType, @Part("documentId") RequestBody documentId,
                                     @Part("dateOfBirth") RequestBody dateOfBirth, @Part MultipartBody.Part document1,
                                     @Part MultipartBody.Part document2, @Part("language") RequestBody language);

//    @FormUrlEncoded
//    @PUT(API_VERSION + "/user/me")
//    Call<BaseResponse> updateProfileForm(@Field("name") String name, @Field("address") String address,
//                                     @Field("city") String city, @Field("postalCode") String postalCode,
//                                     @Field("documentType") String documentType, @Field("documentId") String documentId,
//                                     @Field("dateOfBirth") String dateOfBirth, @Field("document1") String document1,
//                                     @Field("document2") String document2, @Field("language") String language);
}
