package aaronsoftech.in.unber.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response_Booking_List {
    @SerializedName("api_status")
    @Expose
    private String api_status="";
    @SerializedName("api_message")
    @Expose
    private String api_message="";

    @SerializedName("data")
    public List<User_List> data = null;

    public class User_List
    {

        @SerializedName("id")
        @Expose
        private String id="";

        @SerializedName("vehicle_image")
        @Expose
        private String vehicle_image="";

        @SerializedName("user_id")
        @Expose
        private String user_id="";

        @SerializedName("vehicle_id")
        @Expose
        private String vehicle_id="";

        @SerializedName("booked_date_time")
        @Expose
        private String booked_date_time="";

        @SerializedName("from_lat")
        @Expose
        private String from_lat="";

        @SerializedName("from_lng")
        @Expose
        private String from_lng="";
        @SerializedName("from_address")
        @Expose
        private String from_address="";

        @SerializedName("to_address")
        @Expose
        private String to_address="";

        @SerializedName("to_lat")
        @Expose
        private String to_lat="";

        @SerializedName("stoppage_date_time")
        @Expose
        private String stoppage_date_time="";

        @SerializedName("payment_status")
        @Expose
        private String payment_status="";

        @SerializedName("amount")
        @Expose
        private String amount="";

        @SerializedName("pickup")
        @Expose
        private String pickup="";

        @SerializedName("status")
        @Expose
        private String status="";

        @SerializedName("driver_id")
        @Expose
        private String driver_id="";

        @SerializedName("ucontact")
        @Expose
        private String ucontact="";

        @SerializedName("uimage")
        @Expose
        private String uimage="";

        @SerializedName("uname")
        @Expose
        private String uname="";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getUcontact() {
            return ucontact;
        }

        public void setUcontact(String ucontact) {
            this.ucontact = ucontact;
        }

        public String getUimage() {
            return uimage;
        }

        public void setUimage(String uimage) {
            this.uimage = uimage;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getVehicle_image() {
            return vehicle_image;
        }

        public void setVehicle_image(String vehicle_image) {
            this.vehicle_image = vehicle_image;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

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

        public String getFrom_address() {
            return from_address;
        }

        public void setFrom_address(String from_address) {
            this.from_address = from_address;
        }

        public String getTo_address() {
            return to_address;
        }

        public void setTo_address(String to_address) {
            this.to_address = to_address;
        }

        public String getTo_lat() {
            return to_lat;
        }

        public void setTo_lat(String to_lat) {
            this.to_lat = to_lat;
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

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
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

    public List<User_List> getData() {
        return data;
    }

    public void setData(List<User_List> data) {
        this.data = data;
    }
}
