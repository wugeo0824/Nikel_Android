package com.media2359.nickel.network;

import com.media2359.nickel.managers.UserSessionManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Xijun on 10/3/16.
 */
public class NikelService {

    static final String API_URL = "http://nickel-staging.ap-southeast-1.elasticbeanstalk.com";

    private static NikelApi apiManager;

    static NikelApi getApiManager() {
        if(apiManager == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            if (UserSessionManager.getInstance().getToken() != null){
                                request = request.newBuilder().addHeader("Authorization", UserSessionManager.getInstance().getToken()).build();
                            }
                            return chain.proceed(request);
                        }
                    })
                    .addInterceptor(interceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiManager = retrofit.create(NikelApi.class);
        }

        return apiManager;
    }

}

