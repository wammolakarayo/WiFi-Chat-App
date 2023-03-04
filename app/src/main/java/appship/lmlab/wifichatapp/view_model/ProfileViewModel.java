package appship.lmlab.wifichatapp.view_model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import appship.lmlab.wifichatapp.R;
import appship.lmlab.wifichatapp.dialog.QRScan;
import appship.lmlab.wifichatapp.model.ChatMsg;
import appship.lmlab.wifichatapp.model.User;
import appship.lmlab.wifichatapp.util.Constance;
import appship.lmlab.wifichatapp.util.DataBase;
import appship.lmlab.wifichatapp.util.Utility;

public abstract class ProfileViewModel {
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private User user = new User();
    private View view;
    private EditText name;
    private EditText ip;
    private EditText device_id;
    private TextView profile_title;
    private ImageView edit_save,dp_image_large,profile_back,scan_qr,qr;
    private CardView qr_wrapper;

    private Context context;
    private boolean editEnable = false;
    private boolean ipEditEnable = false;
    private boolean phoneNumberEditEnable = false;
    private int meOrOther;
    private int openFrom;

    public ProfileViewModel(User user,  Context context, View view,int meOrOther,int openFrom) {
        this.user = user;
        this.view = view;
        this.context = context;
        this.meOrOther = meOrOther;
        this.openFrom = openFrom;
    }

    public void init(){
        findViews(view);
        setData();
        setButtonStatus(false);
        click();
    }

    private void setData(){
        String qrString = "";
        if(user != null){
            name.setText(user.getName());
            device_id.setText(user.getDeviceId());
            if(user.getStatus() == Constance.ME){
                ip.setText(Utility.getIp(context));
            }else{
                ip.setText(user.getIp());
            }
            Utility.setAvatar(dp_image_large,user.getName(),150);
            name.requestFocus();

        }

        if(openFrom == Constance.FROM_PROFILE){
            profile_back.setVisibility(View.GONE);
            profile_title.setText("   Profile");
            device_id.setText(Utility.getAndroidId(context));
            scan_qr.setVisibility(View.GONE);
            qr_wrapper.setVisibility(View.VISIBLE);
        }else{
            profile_back.setVisibility(View.VISIBLE);
            profile_title.setText("Profile");
            scan_qr.setVisibility(View.VISIBLE);
            qr_wrapper.setVisibility(View.GONE);
        }

        if(openFrom == Constance.FROM_CONTACT_ADD){
            editEnable = true;
            ip.setText("");
            name.setText("");
            device_id.setText("");
        }

        qrString = name.getText().toString()+">>>"+ip.getText().toString()+">>>"+device_id.getText().toString();
        Utility.QRCodeGenerator(qr,qrString);
    }




    private void setButtonStatus(boolean isInit){
        user.setStatus(meOrOther);
        if(editEnable){
            edit_save.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_done));
            name.setEnabled(true);
            if(user.getStatus() == Constance.OTHER){
                ip.setEnabled(true);
                device_id.setEnabled(true);
            }

        }else{
            saveData(isInit);
        }



    }

    private void saveData(boolean isInit){
        edit_save.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_edit));
        name.setEnabled(false);
        ip.setEnabled(false);
        device_id.setEnabled(false);
        if(isInit){
            if(name.getText().toString().length() < 3){
                Toast.makeText(context,"Name too short",Toast.LENGTH_LONG).show();
                return;
            }
            if(device_id.getText().toString().equals("")){
                Toast.makeText(context,"Phone number cannot be empty",Toast.LENGTH_LONG).show();
                return;
            }

            if(!Patterns.IP_ADDRESS.matcher(ip.getText().toString()).matches()){
                Toast.makeText(context,"Invalid IP address",Toast.LENGTH_LONG).show();
                return;
            }

            user.setName(name.getText().toString().replace("<<<","").trim());
            user.setIp(ip.getText().toString().trim());
            user.setDeviceId(device_id.getText().toString().replace("<<<","").trim());
            DataBase.getInstance(context).addUser(user);
        }
    }

    private void click(){
        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEnable = !editEnable;
                setButtonStatus(true);

            }
        });

        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backProfile();
            }
        });

        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    QRScan qrScan = new QRScan(context) {
                        @Override
                        public void getCode(String code) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String[] array = code.split(">>>");
                                    if(array.length>0){
                                        name.setText(array[0]);
                                        ip.setText(array[1]);
                                        device_id.setText(array[2]);
                                    }
                                    saveData(true);
                                }
                            });

                        }
                    };
                    qrScan.show();
                }else{
                    ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.CAMERA}, 1001);
                }

            }
        });

    }

    private void findViews(View view) {
        name = view.findViewById(R.id.name);
        ip = view.findViewById(R.id.ip);
        edit_save = view.findViewById(R.id.edit_save);
        dp_image_large = view.findViewById(R.id.dp_image_large);
        profile_back = view.findViewById(R.id.profile_back);
        profile_title = view.findViewById(R.id.profile_title);
        device_id = view.findViewById(R.id.device_id);
        scan_qr = view.findViewById(R.id.scan_qr);
        qr = view.findViewById(R.id.qr);
        qr_wrapper = view.findViewById(R.id.qr_wrapper);

    }

    public abstract void backProfile();
}
