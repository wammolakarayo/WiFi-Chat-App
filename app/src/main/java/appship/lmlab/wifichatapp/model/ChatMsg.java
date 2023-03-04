package appship.lmlab.wifichatapp.model;

import appship.lmlab.wifichatapp.util.Constance;

public class ChatMsg {

    private String deviceId = "";
    private String msg = "";
    private int sendReceiveStatus = Constance.RECEIVE;
    private String dateTime = "";
    private String ip = "";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSendReceiveStatus() {
        return sendReceiveStatus;
    }

    public void setSendReceiveStatus(int sendReceiveStatus) {
        this.sendReceiveStatus = sendReceiveStatus;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
