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
    private String speed;

    public FB_Driver_res(String driver_ID, String name, String photo, String contact_number, String lat, String lng, String speed) {
        this.driver_ID = driver_ID;
        this.name = name;
        this.photo = photo;
        this.contact_number = contact_number;
        this.lat = lat;
        this.lng = lng;
        this.speed = speed;
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

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }
}
