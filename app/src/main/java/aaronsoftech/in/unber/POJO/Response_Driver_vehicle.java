package aaronsoftech.in.unber.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response_Driver_vehicle {
    @SerializedName("api_status")
    @Expose
    private String api_status="";
    @SerializedName("api_message")
    @Expose
    private String api_message="";

    @SerializedName("data")
    public List<Vehicle_Info> data = null;


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

    public List<Vehicle_Info> getData() {
        return data;
    }

    public void setData(List<Vehicle_Info> data) {
        this.data = data;
    }

    public class Vehicle_Info
    {
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

        @SerializedName("vehicle_rc")
        @Expose
        private String vehicle_rc="";

        @SerializedName("vehicle_other_doc")
        @Expose
        private String vehicle_other_doc="";

        @SerializedName("vehicle_insurance_id")
        @Expose
        private String vehicle_insurance_id="";

        @SerializedName("permit")
        @Expose
        private String permit="";

        @SerializedName("status")
        @Expose
        private String status="";

        @SerializedName("timestamp")
        @Expose
        private String timestamp="";

        @SerializedName("vehicle_photo")
        @Expose
        private String vehicle_photo="";

        @SerializedName("token_no")
        @Expose
        private String token_no="";

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

        public String getVehicle_rc() {
            return vehicle_rc;
        }

        public void setVehicle_rc(String vehicle_rc) {
            this.vehicle_rc = vehicle_rc;
        }

        public String getVehicle_other_doc() {
            return vehicle_other_doc;
        }

        public void setVehicle_other_doc(String vehicle_other_doc) {
            this.vehicle_other_doc = vehicle_other_doc;
        }

        public String getVehicle_insurance_id() {
            return vehicle_insurance_id;
        }

        public void setVehicle_insurance_id(String vehicle_insurance_id) {
            this.vehicle_insurance_id = vehicle_insurance_id;
        }

        public String getPermit() {
            return permit;
        }

        public void setPermit(String permit) {
            this.permit = permit;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getVehicle_photo() {
            return vehicle_photo;
        }

        public void setVehicle_photo(String vehicle_photo) {
            this.vehicle_photo = vehicle_photo;
        }

        public String getToken_no() {
            return token_no;
        }

        public void setToken_no(String token_no) {
            this.token_no = token_no;
        }
    }

}
