package appship.lmlab.wifichatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import appship.lmlab.wifichatapp.fragment.ChatHeadFragment;
import appship.lmlab.wifichatapp.fragment.ContactFragment;
import appship.lmlab.wifichatapp.fragment.ProfileFragment;
import appship.lmlab.wifichatapp.model.ChatMsg;
import appship.lmlab.wifichatapp.model.User;
import appship.lmlab.wifichatapp.observer.MsgObserver;
import appship.lmlab.wifichatapp.util.DataBase;
import appship.lmlab.wifichatapp.util.ReceiveMessage;
import appship.lmlab.wifichatapp.util.Utility;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Context context = MainActivity.this;
    private static final String TAG = "MainActivity";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    public static int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.chat);


        addMe();
        receiveMessages();


    }

    private void addMe(){
        User user = DataBase.getInstance(context).getMe();
        user.setIp(Utility.getIp(context));
        user.setDeviceId(Utility.getAndroidId(context));
        DataBase.getInstance(context).addUser(user);
    }


    ChatHeadFragment chatHeadFragment = new ChatHeadFragment();
    ContactFragment contactFragment = new ContactFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, chatHeadFragment).commit();
                return true;

            case R.id.contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, contactFragment).commit();
                return true;

            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
        }
        return false;
    }

    private void receiveMessages(){
        Thread myThread = new Thread(new ReceiveMessage() {
            @Override
            public void saveMsg(String json) {
                Log.d(TAG, "_saveMsg_ "+json);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        ChatMsg chatMsg = gson.fromJson(json, ChatMsg.class);
                        DataBase.getInstance(context).addMsg(chatMsg);
                        onUpdateMsg();
                    }
                });

            }
        });
        myThread.start();
    }

    private void onUpdateMsg(){
        synchronized (this){
            MsgObserver.getCustomerOrderObserver().updateMsgOrder();
        }
    }

    public void requestCameraPermission(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)  == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }
}