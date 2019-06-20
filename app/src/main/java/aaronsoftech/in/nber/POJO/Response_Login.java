package aaronsoftech.in.nber.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chouhan on 06/20/2019.
 */

public class Response_Login {
    @SerializedName("api_status")
    @Expose
    private String api_status="";
    @SerializedName("api_message")
    @Expose
    private String api_message="";

    @SerializedName("data")
    public List<User_Info> data = null;

    public class User_Info {

        @SerializedName("id")
        public Integer id;

        @SerializedName("name")
        public String name= null;

        @SerializedName("gender")
        public String gender= null;

        @SerializedName("photo")
        public String photo= null;

        @SerializedName("email")
        public String email= null;

        @SerializedName("password")
        public String password= null;

        @SerializedName("id_cms_privileges")
        public String id_cms_privileges= null;

        @SerializedName("cms_privileges_name")
        public String cms_privileges_name= null;

        @SerializedName("cms_privileges_is_superadmin")
        public String cms_privileges_is_superadmin= null;

        @SerializedName("cms_privileges_theme_color")
        public String cms_privileges_theme_color= null;

        @SerializedName("status")
        public String status = null;

        @SerializedName("contact_number")
        public String contact_number = null;

        @SerializedName("address")
        public String address = null;

        @SerializedName("city")
        public String city = null;

        @SerializedName("state")
        public String state = null;

        @SerializedName("country")
        public String country = null;

        @SerializedName("lat")
        public String lat = null;
        @SerializedName("lng")
        public String lng = null;

        @SerializedName("zip_code")
        public String zip_code = null;

        @SerializedName("mac_id")
        public String mac_id = null;

        @SerializedName("social_type")
        public String social_type = null;

        @SerializedName("token_id")
        public String token_id = null;

        @SerializedName("passcode")
        public String passcode = null;

        @SerializedName("usr_status")
        public String usr_status = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getId_cms_privileges() {
            return id_cms_privileges;
        }

        public void setId_cms_privileges(String id_cms_privileges) {
            this.id_cms_privileges = id_cms_privileges;
        }

        public String getCms_privileges_name() {
            return cms_privileges_name;
        }

        public void setCms_privileges_name(String cms_privileges_name) {
            this.cms_privileges_name = cms_privileges_name;
        }

        public String getCms_privileges_is_superadmin() {
            return cms_privileges_is_superadmin;
        }

        public void setCms_privileges_is_superadmin(String cms_privileges_is_superadmin) {
            this.cms_privileges_is_superadmin = cms_privileges_is_superadmin;
        }

        public String getCms_privileges_theme_color() {
            return cms_privileges_theme_color;
        }

        public void setCms_privileges_theme_color(String cms_privileges_theme_color) {
            this.cms_privileges_theme_color = cms_privileges_theme_color;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getZip_code() {
            return zip_code;
        }

        public void setZip_code(String zip_code) {
            this.zip_code = zip_code;
        }

        public String getMac_id() {
            return mac_id;
        }

        public void setMac_id(String mac_id) {
            this.mac_id = mac_id;
        }

        public String getSocial_type() {
            return social_type;
        }

        public void setSocial_type(String social_type) {
            this.social_type = social_type;
        }

        public String getToken_id() {
            return token_id;
        }

        public void setToken_id(String token_id) {
            this.token_id = token_id;
        }

        public String getPasscode() {
            return passcode;
        }

        public void setPasscode(String passcode) {
            this.passcode = passcode;
        }

        public String getUsr_status() {
            return usr_status;
        }

        public void setUsr_status(String usr_status) {
            this.usr_status = usr_status;
        }
    }

    public String getApi_status() {
        return api_status;
    }

    public void setApi_status(String api_status) {
        this.api_status = api_status;
    }

    public String getApi_message() {
        return api_message;
    }

    public void setApi_message(String api_message) {
        this.api_message = api_message;
    }

    public List<User_Info> getData() {
        return data;
    }

    public void setData(List<User_Info> data) {
        this.data = data;
    }
}
