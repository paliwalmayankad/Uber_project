package aaronsoftech.in.unber.POJO;

public class Response_Booking {


    String vehicle_image;
    String user_id;
    String vehicle_id;
    String booked_date_time;
    String from_lat;
    String from_lng;
    String from_address;
    String to_address;
    String to_lat;
    String to_lng;
    String stoppage_date_time;
    String payment_status;
    String amount;
    String pickup;
    String status;
    String driver_id;


    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getBooked_date_time() {
        return booked_date_time;
    }

    public void setBooked_date_time(String booked_date_time) {
        this.booked_date_time = booked_date_time;
    }

    public String getStoppage_date_time() {
        return stoppage_date_time;
    }

    public void setStoppage_date_time(String stoppage_date_time) {
        this.stoppage_date_time = stoppage_date_time;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicle_image() {
        return vehicle_image;
    }

    public void setVehicle_image(String vehicle_image) {
        this.vehicle_image = vehicle_image;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getFrom_lat() {
        return from_lat;
    }

    public void setFrom_lat(String from_lat) {
        this.from_lat = from_lat;
    }

    public String getFrom_lng() {
        return from_lng;
    }

    public void setFrom_lng(String from_lng) {
        this.from_lng = from_lng;
    }

    public String getTo_lat() {
        return to_lat;
    }

    public void setTo_lat(String to_lat) {
        this.to_lat = to_lat;
    }

    public String getTo_lng() {
        return to_lng;
    }

    public void setTo_lng(String to_lng) {
        this.to_lng = to_lng;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
