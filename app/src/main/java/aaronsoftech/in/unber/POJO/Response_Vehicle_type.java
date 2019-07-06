package aaronsoftech.in.unber.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chouhan on 07/01/2019.
 */

public class Response_Vehicle_type {
    @SerializedName("api_status")
    @Expose
    private String api_status="";

    @SerializedName("api_message")
    @Expose
    private String api_message="";

    @SerializedName("data")
    public List<Data_List> data = null;

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

    public List<Data_List> getData() {
        return data;
    }

    public void setData(List<Data_List> data) {
        this.data = data;
    }

    public class Data_List {
        @SerializedName("id")
        @Expose
        private String id="";
        @SerializedName("vehicle_type")
        @Expose
        private String vehicle_type="";
        @SerializedName("vehicle_icon")
        @Expose
        private String vehicle_icon="";
        @SerializedName("status")
        @Expose
        private String status="";
        @SerializedName("seating_capacity")
        @Expose
        private String seating_capacity="";

        @SerializedName("km_price")
        @Expose
        private String km_price="";

        public String getKm_price() {
            return km_price;
        }

        public void setKm_price(String km_price) {
            this.km_price = km_price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVehicle_type() {
            return vehicle_type;
        }

        public void setVehicle_type(String vehicle_type) {
            this.vehicle_type = vehicle_type;
        }

        public String getVehicle_icon() {
            return vehicle_icon;
        }

        public void setVehicle_icon(String vehicle_icon) {
            this.vehicle_icon = vehicle_icon;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSeating_capacity() {
            return seating_capacity;
        }

        public void setSeating_capacity(String seating_capacity) {
            this.seating_capacity = seating_capacity;
        }
    }
}
