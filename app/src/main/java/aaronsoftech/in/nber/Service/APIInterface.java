package aaronsoftech.in.nber.Service;

import java.util.Map;

import aaronsoftech.in.nber.POJO.Response_Login;
import aaronsoftech.in.nber.POJO.Response_register;
import aaronsoftech.in.nber.POJO.Response_vehicle;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    @POST("login")
    public Call<Response_Login> getContect_Login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("login_with_id")
    public Call<Response_Login> getLogin_with_id(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("user_profile")
    public Call<Response_Login> getUpdate_Profile(@FieldMap Map<String, String> map);


    @Multipart
    @POST("vehicle_reg")
    Call<Response_vehicle> vehicle_register(
            @Part("driver_id") RequestBody driver_id,
            @Part("vehicle_type_id") RequestBody vehicle_type_id,
            @Part("vehicle_number") RequestBody number,
            @Part MultipartBody.Part permit_file,
            @Part MultipartBody.Part vehicle_file,
            @Part MultipartBody.Part driver_rc_file,
            @Part MultipartBody.Part aadhar_other_doc,
            @Part MultipartBody.Part aadhar_insurense
            /*@Part MultipartBody.Part police_verification_file,
            @Part MultipartBody.Part file_permit_a,
            @Part MultipartBody.Part file_permit_b,
            @Part MultipartBody.Part file_registration*/
    );

    @Multipart
    @POST("driver_reg")
    Call<Response_register> driver_register(
            @Part("user_id") RequestBody user_id,
            @Part("verified_status") RequestBody verified_status,
            @Part("dl_number") RequestBody dl_number,
            @Part("aadhar_number") RequestBody aadhar_number,
            @Part("pan_number") RequestBody pan_number,
            @Part("police_verification_status") RequestBody police_verification_status,
            @Part("driver_insured_status") RequestBody driver_insured_status,
            @Part("status") RequestBody status,
            @Part MultipartBody.Part dl_file,
            @Part MultipartBody.Part pan_file,
            @Part MultipartBody.Part driver_insured_file,
            @Part MultipartBody.Part aadhar_file
            /*@Part MultipartBody.Part police_verification_file,
            @Part MultipartBody.Part file_permit_a,
            @Part MultipartBody.Part file_permit_b,
            @Part MultipartBody.Part file_registration*/
    );

}
