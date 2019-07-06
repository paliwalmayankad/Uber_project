package aaronsoftech.in.unber.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chouhan on 06/20/2019.
 */

public class APIClient {
    public static final String BASE_URL = "http://thenber.com/backend/public/api/";
    private static Retrofit retrofit = null;
    private static APIInterface methods = null;

    public static APIInterface getWebServiceMethod()
    {
        try
        {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor()
            {
                @Override
                public Response intercept(Chain chain) throws IOException
                {
                    Request request = chain.request().newBuilder()
                            .build();
                    return chain.proceed(request);
                }
            }).connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            methods = retrofit.create(APIInterface.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return methods;
    }

}
