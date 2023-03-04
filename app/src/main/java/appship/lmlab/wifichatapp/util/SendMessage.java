package appship.lmlab.wifichatapp.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;

public abstract class SendMessage {
    private String ip;
    private String json;
    private int port = 53400;
    private static final String TAG ="Socket";


    public SendMessage(String ip, String json) {
        this.ip = ip;
        this.json = json;
    }

    public void send(){
        Task t = new Task();
        t.execute(ip,json);
        Log.d(TAG, "send: ");

    }

    class Task extends AsyncTask<String, Void, Boolean> {

        Socket s;
        String ip,json;
        DataOutputStream dataOutputStream;
        InetSocketAddress sockAdr;

        @Override
        protected Boolean doInBackground(String... strings) {
            ip = strings[0];
            json = strings[1];
            boolean isSuccess = false;
            try{

                sockAdr = new InetSocketAddress(ip, port);
                s = new Socket();
                int timeout = 5000;
                s.connect(sockAdr, timeout);
                dataOutputStream = new DataOutputStream(s.getOutputStream());
                dataOutputStream.writeUTF(json);
                dataOutputStream.close();
                s.close();

                isSuccess = true;
            }catch (NoRouteToHostException e){
                e.printStackTrace();
            }catch (Exception e){
               e.printStackTrace();
            }
            return isSuccess;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            isSuccess(s);
        }
    }

    public abstract void isSuccess(boolean success);

}
