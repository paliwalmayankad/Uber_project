package aaronsoftech.in.nber.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Chouhan on 07/01/2019.
 */

public class Response_vehicle {
    @SerializedName("api_status")
    @Expose
    private String api_status;

    @SerializedName("api_message")
    @Expose
    private String api_message;

    @SerializedName("id")
    @Expose
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
