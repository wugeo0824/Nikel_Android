package com.media2359.nickel.network;

import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.network.responses.BanksResponse;
import com.media2359.nickel.network.responses.BaseResponse;
import com.media2359.nickel.network.responses.ComputeResponse;
import com.media2359.nickel.network.responses.LoginResponse;
import com.media2359.nickel.network.responses.RecipientListResponse;
import com.media2359.nickel.network.responses.TransfersResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
    Call<LoginResponse> loginWithOtp(@Field("mobilePhone") String mobile, @Field("otp") String otp);

    @FormUrlEncoded
    @POST(API_VERSION + AUTH_PREFIX + "/loginConsumer")
    Call<LoginResponse> login(@Field("mobilePhone") String mobile, @Field("password") String password);

    @FormUrlEncoded
    @POST(API_VERSION + AUTH_PREFIX + "/resetPasswordConsumer")
    Call<BaseResponse> resetPassword(@Field("mobilePhone") String mobile, @Field("otp") String otp, @Field("newPassword") String password);

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

    @GET(API_VERSION + "/recipients")
    Call<List<Recipient>> getRecipients();

    @POST(API_VERSION + "/recipients")
    Call<Recipient> addRecipient(@Body Recipient recipient);

    @PUT(API_VERSION + "/recipients/{id}")
    Call<Recipient> updateRecipient(@Path("id") String id, @Body Recipient recipient);

    @DELETE(API_VERSION + "/recipients/{id}")
    Call<BaseResponse> deleteRecipient(@Path("id") String id);

    @GET(API_VERSION + "/transfers")
    Call<TransfersResponse> getTransfers(@Query("page") int page);

    @FormUrlEncoded
    @POST(API_VERSION + "/transfers/compute")
    Call<ComputeResponse> computeTransfer(@Field("amtSent") int amtSent, @Field("currencySent") String currencySent, @Field("currencyRecv") String currencyRecv);

    @FormUrlEncoded
    @POST(API_VERSION + "/transfers")
    Call<NickelTransfer> createTransfer(@Field("amtSent") int amtSent, @Field("currencySent") String currencySent, @Field("currencyRecv") String currencyRecv, @Field("recipient") String recipientId);

    @Multipart
    @PUT(API_VERSION + "/transfers/{id}/uploadReceipt")
    Call<NickelTransfer> uploadReceipt(@Path("id") int transferId, @Part MultipartBody.Part receipt);

    @GET(API_VERSION + "/banks")
    Call<BanksResponse> getBanks();

}
