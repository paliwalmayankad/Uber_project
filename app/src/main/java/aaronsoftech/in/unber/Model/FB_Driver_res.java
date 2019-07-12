package aaronsoftech.in.unber.Model;

/**
 * Created by Chouhan on 07/06/2019.
 */

public class FB_Driver_res {
    private String driver_ID;
    private String name;
    private String photo;
    private String contact_number;
    private String lat;
    private String lng;
    private String address;
    private String city;
    private String country;
    private String email;
    private String state;
    private String veh_type_id;
    private String veh_no;
    private String veh_img;
    private String amount;

    public FB_Driver_res(String driver_ID, String name, String photo, String contact_number, String lat, String lng, String address, String city, String country, String email, String state, String veh_type_id, String veh_no, String veh_img, String amount) {
        this.driver_ID = driver_ID;
        this.name = name;
        this.photo = photo;
        this.contact_number = contact_number;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.city = city;
        this.country = country;
        this.email = email;
        this.state = state;
        this.veh_type_id = veh_type_id;
        this.veh_no = veh_no;
        this.veh_img = veh_img;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVeh_type_id() {
        return veh_type_id;
    }

    public void setVeh_type_id(String veh_type_id) {
        this.veh_type_id = veh_type_id;
    }

    public String getVeh_no() {
        return veh_no;
    }

    public void setVeh_no(String veh_no) {
        this.veh_no = veh_no;
    }

    public String getVeh_img() {
        return veh_img;
    }

    public void setVeh_img(String veh_img) {
        this.veh_img = veh_img;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDriver_ID() {
        return driver_ID;
    }

    public void setDriver_ID(String driver_ID) {
        this.driver_ID = driver_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
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

}
