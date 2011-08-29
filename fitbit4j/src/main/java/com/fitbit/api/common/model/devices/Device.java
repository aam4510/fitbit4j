package com.fitbit.api.common.model.devices;

import com.fitbit.api.FitbitAPIException;
import com.fitbit.api.client.http.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Device {
    /**
     * Device id
     */
    private final long id;
    private final DeviceType type;
    /**
     * The battery level of the Fitbit device, one of Low, Medium, High, and Full. The level is "Low" when no
     * information is available.
     */
	private final String battery;

    private String lastSyncTime;

    public Device(long id, DeviceType type, String battery, String lastSyncTime) {
        this.id = id;
        this.type = type;
        this.battery = battery;
        this.lastSyncTime = lastSyncTime;
    }

    public Device(JSONObject json) throws JSONException {
        id = json.getLong("id");
        type =  DeviceType.valueOf(json.getString("type"));
        battery = json.getString("battery");
        lastSyncTime = json.getString("lastSyncTime");
    }

    public static List<Device> constructDeviceList(Response res) throws FitbitAPIException {
        try {
            return jsonArrayToDeviceList(res.asJSONArray());
        } catch (JSONException e) {
            throw new FitbitAPIException(e.getMessage() + ':' + res.asString(), e);
        }
    }

    private static List<Device> jsonArrayToDeviceList(JSONArray array) throws JSONException {
        List<Device> deviceList = new ArrayList<Device>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject device = array.getJSONObject(i);
            deviceList.add(new Device(device));
        }
        return deviceList;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type.name();
    }

    public DeviceType type() {
        return type;
    }

    public String getBattery() {
        return battery;
    }

    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public String getLastSyncTime() {
        return lastSyncTime;
    }

    @SuppressWarnings({"RedundantIfStatement"})
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;

        Device device = (Device) o;

        if (id != device.id) return false;
        if (battery != null ? !battery.equals(device.battery) : device.battery != null) return false;
        if (lastSyncTime != null ? !lastSyncTime.equals(device.lastSyncTime) : device.lastSyncTime != null)
            return false;
        if (type != device.type) return false;

        return true;
    }

    @SuppressWarnings({"NumericCastThatLosesPrecision", "UnnecessaryParentheses"})
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (battery != null ? battery.hashCode() : 0);
        result = 31 * result + (lastSyncTime != null ? lastSyncTime.hashCode() : 0);
        return result;
    }

}
