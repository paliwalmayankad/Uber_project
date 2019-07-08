package aaronsoftech.in.unber.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chouhan on 07/02/2019.
 */

public class Response_All_Vehicle {
    @SerializedName("api_status")
    @Expose
    private String api_status="";

    @SerializedName("api_message")
    @Expose
    private String api_message="";

    @SerializedName("data")
    public List<Data_Vehicle_List> data = null;

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

    public List<Data_Vehicle_List> getData() {
        return data;
    }

    public void setData(List<Data_Vehicle_List> data) {
        this.data = data;
    }

    public class Data_Vehicle_List {
        @SerializedName("id")
        @Expose
        private String id="";

        @SerializedName("driver_id")
        @Expose
        private String driver_id="";

        @SerializedName("vehicle_type_id")
        @Expose
        private String vehicle_type_id="";

        @SerializedName("vehicle_number")
        @Expose
        private String vehicle_number="";

        @SerializedName("vehicle_photo")
        @Expose
        private String vehicle_photo="";

        @SerializedName("vehicle_price")
        @Expose
        private String vehicle_price="";

        @SerializedName("token_no")
        @Expose
        private String token_no="";


        public String getToken_no() {
            return token_no;
        }

        public void setToken_no(String token_no) {
            this.token_no = token_no;
        }

        public String getVehicle_price() {
            return vehicle_price;
        }

        public void setVehicle_price(String vehicle_price) {
            this.vehicle_price = vehicle_price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getVehicle_type_id() {
            return vehicle_type_id;
        }

        public void setVehicle_type_id(String vehicle_type_id) {
            this.vehicle_type_id = vehicle_type_id;
        }

        public String getVehicle_number() {
            return vehicle_number;
        }

        public void setVehicle_number(String vehicle_number) {
            this.vehicle_number = vehicle_number;
        }

        public String getVehicle_photo() {
            return vehicle_photo;
        }

        public void setVehicle_photo(String vehicle_photo) {
            this.vehicle_photo = vehicle_photo;
        }
    }
}
