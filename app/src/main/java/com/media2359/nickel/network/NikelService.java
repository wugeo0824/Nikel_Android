package com.media2359.nickel.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Xijun on 10/3/16.
 */
public class NikelService {

    static final String API_URL = "https://api.foursquare.com/";

    private static NikelApi userlessManager;

    static NikelApi getUserlessApiManager() {
        if(userlessManager == null) {

            OkHttpClient httpClient = new OkHttpClient.Builder()
//                    .addInterceptor(new Interceptor() {
//                        @Override
//                        public Response intercept(Chain chain) throws IOException {
//                            Request request = chain.request();
//                            HttpUrl url = request.url().newBuilder().addQueryParameter("client_id", FOURSQUARE_CLIENT_ID)
//                                    .addQueryParameter("client_secret",FOURSQUARE_CLIENT_SECRET).addQueryParameter("v",currentDate).build();
//                            request = request.newBuilder().url(url).build();
//                            return chain.proceed(request);
//                        }
//                    })
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            userlessManager = retrofit.create(NikelApi.class);
        }

        return userlessManager;
    }

    interface NikelApi {

        //TODO: apis go here
    }
}

