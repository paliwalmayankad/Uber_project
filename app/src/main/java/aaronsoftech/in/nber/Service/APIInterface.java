package aaronsoftech.in.nber.Service;

import java.util.Map;

import aaronsoftech.in.nber.POJO.Response_Login;
import aaronsoftech.in.nber.POJO.Response_register;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Chouhan on 06/20/2019.
 */

public interface APIInterface {

    @FormUrlEncoded
    @POST("user_register")
    public Call<Response_register> getRegister(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("social_login")
    public Call<Response_Login> getSocial_Login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("login_with_id")
    public Call<Response_Login> getLogin_with_id(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("user_profile")
    public Call<Response_Login> getUpdate_Profile(@FieldMap Map<String, String> map);

}
