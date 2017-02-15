package pl.com.chodera.myweather.model.pojo;

import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    private String all;

    public String getAll() {
        return all;
    }
}
