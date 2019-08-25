package aaronsoftech.in.nber.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Notification {

    @SerializedName("api_status")
    @Expose
    private String api_status="";
    @SerializedName("api_message")
    @Expose
    private String api_message="";

    @SerializedName("data")
    public List<Notification.send_data> data = null;


    public class send_data {
        @SerializedName("user_id")
        @Expose
        private String user_id="";

        @SerializedName("driver_id")
        @Expose
        private String driver_id="";

        @SerializedName("notification_text")
        @Expose
        private String notification_text="";

        @SerializedName("timestamp")
        @Expose
        private String timestamp="";

        @SerializedName("sent_by")
        @Expose
        private String sent_by="";

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getNotification_text() {
            return notification_text;
        }

        public void setNotification_text(String notification_text) {
            this.notification_text = notification_text;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSent_by() {
            return sent_by;
        }

        public void setSent_by(String sent_by) {
            this.sent_by = sent_by;
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

    public List<send_data> getData() {
        return data;
    }

    public void setData(List<send_data> data) {
        this.data = data;
    }
}
