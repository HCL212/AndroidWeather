
package com.example.androidweather.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Flags extends RealmObject {

    @SerializedName("sources")
    @Expose
    private RealmList<String> sources = null;
    @SerializedName("nearest-station")
    @Expose
    private Double nearestStation;
    @SerializedName("units")
    @Expose
    private String units;

    public RealmList<String> getSources() {
        return sources;
    }

    public void setSources(RealmList<String> sources) {
        this.sources = sources;
    }

    public Double getNearestStation() {
        return nearestStation;
    }

    public void setNearestStation(Double nearestStation) {
        this.nearestStation = nearestStation;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

}
