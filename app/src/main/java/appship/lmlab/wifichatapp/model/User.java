package appship.lmlab.wifichatapp.model;

import appship.lmlab.wifichatapp.util.Constance;
import appship.lmlab.wifichatapp.util.Utility;

public class User {
    private int id = -1;
    private String ip = "";
    private String name = Utility.getDeviceName();
    private String profilePic = "";
    private String deviceId = "";
    private int status = Constance.ME;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
