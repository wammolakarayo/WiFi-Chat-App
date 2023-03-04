package appship.lmlab.wifichatapp.observer;


public class MsgObserver {
    private static MsgObserver INSTANCE;
    private MsgCallBack msgCallBack;

    public static MsgObserver getCustomerOrderObserver() {
        if (INSTANCE == null) {
            synchronized (MsgObserver.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MsgObserver();
                }
            }
        }
        return INSTANCE;
    }

    public void setMsgObserverCallback(MsgCallBack customerOrderCallBack) {
        this.msgCallBack = customerOrderCallBack;
    }

    public void updateMsgOrder() {
        if(msgCallBack != null){
            msgCallBack.onCustomerOrderUpdate();
        }
    }
}
