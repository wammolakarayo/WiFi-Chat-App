package appship.lmlab.wifichatapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.zxing.WriterException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Utility {
    private static String TAG = "Utility";

    public static void setAvatar(ImageView imageView, String name,int fontSize){
        if(name == null){
            return;
        }
        String[] separated = name.split(" ");
        if(separated.length > 1){
            String f = "";
            String l = "";
            if(separated[0].length()<2){
                f = separated[0];
            } else{
                f = separated[0].substring(0,1);
            }

            if(separated[1].length()<2){
                l = separated[1];
            } else{
                l = separated[1].substring(0,1);
            }
            name = f+l;
        }else{
            if(separated[0].length()<2){
                name = separated[0];
            } else{
                name = separated[0].substring(0,2);;
            }
        }

        TextDrawable drawable = TextDrawable.builder().beginConfig()
                .useFont(Typeface.DEFAULT)
                .fontSize(fontSize) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(name, Color.RED);
        imageView.setImageDrawable(drawable);

    }

    public static String formatDate(String fromFormat, String toFormat, String dateTime) {
        SimpleDateFormat inFormat = new SimpleDateFormat(fromFormat, Locale.ENGLISH);
        SimpleDateFormat outFormat = new SimpleDateFormat(toFormat, Locale.ENGLISH);
        Date date = null;
        try {
            date = inFormat.parse(dateTime);
            dateTime = outFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("dateFormat_Error", "fromFormat: " + fromFormat + " toFormat: " +
                    toFormat + " " + "dateTime: " + dateTime + " " + Log.getStackTraceString(new Exception()));
        }

        return dateTime;
    }

    public static String getIp(Context context){
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context){
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }catch (Exception e){
            return "xxx-xxx-xxx";
        }
    }

    public static void QRCodeGenerator(ImageView imageView,String text){
        QRGEncoder qrgEncoder = new QRGEncoder(text, null, QRGContents.Type.TEXT, 200);
        qrgEncoder.setColorBlack(Color.WHITE);
        qrgEncoder.setColorWhite(Color.BLACK);
        // Getting QR-Code as Bitmap
        Bitmap bitmap = qrgEncoder.getBitmap();
        // Setting Bitmap to ImageView
        imageView.setImageBitmap(bitmap);

    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
