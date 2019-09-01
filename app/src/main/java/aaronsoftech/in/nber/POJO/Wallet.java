package aaronsoftech.in.nber.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Wallet {

    @SerializedName("api_status")
    @Expose
    private String api_status;

    @SerializedName("api_message")
    @Expose
    private String api_message;

    @SerializedName("data")
    public List<Wallet_List> data = null;

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

    public class Wallet_List
    {
        @SerializedName("driver_id")
        @Expose
        private String driver_id;

        @SerializedName("amt_type")
        @Expose
        private String amt_type;

        @SerializedName("amount")
        @Expose
        private String amount;

        @SerializedName("remark")
        @Expose
        private String remark;

        @SerializedName("timestamp")
        @Expose
        private String timestamp;


        @SerializedName("status")
        @Expose
        private String status;

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getAmt_type() {
            return amt_type;
        }

        public void setAmt_type(String amt_type) {
            this.amt_type = amt_type;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public List<Wallet_List> getData() {
        return data;
    }

    public void setData(List<Wallet_List> data) {
        this.data = data;
    }
}
