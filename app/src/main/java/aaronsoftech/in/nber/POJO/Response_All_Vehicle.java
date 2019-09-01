package aaronsoftech.in.nber.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chouhan on 07/02/2019.
 */

public class Response_All_Vehicle {
    @SerializedName("ResponseCode")
    @Expose
    private String ResponseCode="";

    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage="";

    @SerializedName("Result")
    public List<Data_Vehicle_List> Result = null;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public List<Data_Vehicle_List> getResult() {
        return Result;
    }

    public void setResult(List<Data_Vehicle_List> result) {
        Result = result;
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

        @SerializedName("lat")
        @Expose
        private String lat="";

        @SerializedName("lng")
        @Expose
        private String lng="";

        @SerializedName("status")
        @Expose
        private String status="";

        @SerializedName("distance")
        @Expose
        private String distance="";

                 /*  "id": 31,
                "driver_id": 5,
                "vehicle_type_id": "6",
                "vehicle_number": "I 20TH UDP",
                "vehicle_rc": "uploads/0/2019-08/184f9f389f0f7f3a0910a8ab7e860ea4.jpg",
                "vehicle_other_doc": "uploads/0/2019-08/4cb458fae1ba68595b99135a3dda9882.jpg",
                "vehicle_insurance_id": "uploads/0/2019-08/bc6bb83c0a69254970de2f7461957b62.jpg",
                "permit": "uploads/0/2019-08/befcb2c9909aac301d05e6e53fda86da.jpg",
                "status": "Deactive",
                "timestamp": "2019-08-20 22:18:07",
                "vehicle_photo": "uploads/0/2019-08/d902149e92bac2834206baab1474092e.jpg",
                "token_no": "fXgzu07W2K8:APA91bG4vdFl1XxZZwnrVKUWNyJ8QqbhUr9T8XHYEHcNhcWbaPZ8SRD0zsQU3yIiFcuBdOeTLqWtHEngN_loGo_QJFQV4Dg1dfurGj6HiCrsnw4aJQSsHm65tk-K-XFz4YehbOv4ds4m",
                "lat": "0",
                "lng": "0",
                "distance": 551.4361347655323015715111978352069854736328125*/

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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

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
