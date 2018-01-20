package app.nobroker.dataModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashrafiqubal on 20/01/18.
 */

public class PayloadMainApi {
    int status = 0;
    int statusCode = 0;
    String message = "";
//    String otherParams = "";
    List<Data> data = new ArrayList<>();

    public PayloadMainApi() {

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /*public String getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(String otherParams) {
        this.otherParams = otherParams;
    }*/

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
