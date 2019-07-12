package aaronsoftech.in.unber.Service;

import java.util.Map;

import aaronsoftech.in.unber.POJO.Response_All_Vehicle;
import aaronsoftech.in.unber.POJO.Response_Booking;
import aaronsoftech.in.unber.POJO.Response_Booking_List;
import aaronsoftech.in.unber.POJO.Response_Driver_vehicle;
import aaronsoftech.in.unber.POJO.Response_Login;
import aaronsoftech.in.unber.POJO.Response_Vehicle_type;
import aaronsoftech.in.unber.POJO.Response_register;
import aaronsoftech.in.unber.POJO.Response_vehicle;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
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
    @POST("change_vehicle_status")
    public Call<Response_register> update_change_vehicle_status(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("social_login")
    public Call<Response_Login> getSocial_Login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("login_mobile")
    public Call<Response_Login> getContect_Login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("booked_ride")
    public Call<Response_register> getBooked_ride(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("login_with_id")
    public Call<Response_Login> getLogin_with_id(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("user_profile")
    public Call<Response_Login> getUpdate_Profile(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("change_booking_status")
    public Call<Response_register> get_booking_status_change(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("get_driver_ride_book")
    public Call<Response_Booking_List> get_Driver_Booking(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("get_driver_vehicle")
    public Call<Response_Driver_vehicle> get_Driver_Vehicle(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("get_all_vehicle_type")
    public Call<Response_Vehicle_type> get_All_vehicle_type(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("get_select_vehicle")
    public Call<Response_All_Vehicle> get_All_select_vehicle(@FieldMap Map<String, String> map);

    @Multipart
    @POST("vehicle_registration")
    Call<Response_vehicle> vehicle_register(
            @Part("token_no") RequestBody token_no,
            @Part("driver_id") RequestBody driver_id,
            @Part("vehicle_type_id") RequestBody vehicle_type_id,
            @Part("vehicle_number") RequestBody number,
            @Part("status") RequestBody status,
            @Part MultipartBody.Part permit_file,
            @Part MultipartBody.Part vehicle_file,
            @Part MultipartBody.Part driver_rc_file,
            @Part MultipartBody.Part aadhar_other_doc,
            @Part MultipartBody.Part aadhar_insurense
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
            @Part MultipartBody.Part police_verification_file,
            @Part MultipartBody.Part aadhar_file
    );

}
