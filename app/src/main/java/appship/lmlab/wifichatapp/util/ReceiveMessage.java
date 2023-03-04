package appship.lmlab.wifichatapp.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ReceiveMessage implements Runnable{
    @Override
    public void run() {
        try {

            ServerSocket serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(Constance.port));

            while (true){
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String json = dataInputStream.readUTF();
                saveMsg(json);



                /*handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(port == 49500) {
                            ArrayList<String> jsonList = database.getOrderLog();
                            for(String json : jsonList){
                                if(json != null){
                                    List<POSupload> order = gson.fromJson(json, token.getType());
                                    writeDataOnBd(order);
                                }
                            }

                        }else if(port == 50100){
                            writeDataOnBd(order,1);
                        }else if(port == 50505){
                            writeDataOnBd(email);
                        }else if(port == 50200){
                            writeDataOnBd(customers,"");
                        }

                    }
                });*/



            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public abstract void saveMsg(String json);
}
