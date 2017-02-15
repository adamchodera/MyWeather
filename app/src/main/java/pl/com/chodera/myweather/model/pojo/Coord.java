package pl.com.chodera.myweather.model.pojo;

import com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
