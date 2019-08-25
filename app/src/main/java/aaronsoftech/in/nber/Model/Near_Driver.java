package aaronsoftech.in.nber.Model;

public class Near_Driver {
    String token_no;
    String driver_id;
    String driver_lat;
    String driver_lng;
    String driver_vehicle_no;
    String driver_vehicle_type;
    String driver_status;
    double driver_distance;

    public Near_Driver() {

    }

    public Near_Driver(String token_no, String driver_id, String driver_lat, String driver_lng, String driver_vehicle_no, String driver_vehicle_type, String driver_status, double driver_distance) {
        this.token_no = token_no;
        this.driver_id = driver_id;
        this.driver_lat = driver_lat;
        this.driver_lng = driver_lng;
        this.driver_vehicle_no = driver_vehicle_no;
        this.driver_vehicle_type = driver_vehicle_type;
        this.driver_status = driver_status;
        this.driver_distance = driver_distance;
    }

    public String getToken_no() {
        return token_no;
    }

    public void setToken_no(String token_no) {
        this.token_no = token_no;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_lat() {
        return driver_lat;
    }

    public void setDriver_lat(String driver_lat) {
        this.driver_lat = driver_lat;
    }

    public String getDriver_lng() {
        return driver_lng;
    }

    public void setDriver_lng(String driver_lng) {
        this.driver_lng = driver_lng;
    }

    public String getDriver_vehicle_no() {
        return driver_vehicle_no;
    }

    public void setDriver_vehicle_no(String driver_vehicle_no) {
        this.driver_vehicle_no = driver_vehicle_no;
    }

    public String getDriver_vehicle_type() {
        return driver_vehicle_type;
    }

    public void setDriver_vehicle_type(String driver_vehicle_type) {
        this.driver_vehicle_type = driver_vehicle_type;
    }

    public String getDriver_status() {
        return driver_status;
    }

    public void setDriver_status(String driver_status) {
        this.driver_status = driver_status;
    }

    public double getDriver_distance() {
        return driver_distance;
    }

    public void setDriver_distance(double driver_distance) {
        this.driver_distance = driver_distance;
    }
}
