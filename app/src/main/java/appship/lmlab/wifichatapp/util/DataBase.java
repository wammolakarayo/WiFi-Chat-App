package appship.lmlab.wifichatapp.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import appship.lmlab.wifichatapp.model.ChatHead;
import appship.lmlab.wifichatapp.model.ChatMsg;
import appship.lmlab.wifichatapp.model.User;

public class DataBase extends SQLiteOpenHelper {

    private static  final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chat.db";

    private static volatile DataBase instance = null;

    public static DataBase getInstance(Context context) {
        if (instance == null) {
            synchronized (DataBase.class){
                if (instance == null) {
                    instance = new DataBase(context);
                }
            }
        }
        return instance;
    }

    private static final String ID = "id";
    private static final String TABLE_USER = "User";
    private static final String IP = "ip";
    private static final String NAME = "name";
    private static final String DEVICE_ID = "deviceId";
    private static final String PROFILE_PIC = "profilePic";
    private static final String STATUS = "status";

    private String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER
            + "(" + ID + " INTEGER PRIMARY KEY, "
            + IP +" TEXT DEFAULT '', "
            + NAME +" TEXT DEFAULT '', "
            + DEVICE_ID +" UNIQUE, "
            + PROFILE_PIC +" TEXT DEFAULT '', "
            + STATUS +" INTEGER DEFAULT 1 )";

    private static final String TABLE_CHAT = "Chat";
    private static final String MSG = "msg";
    private static final String SEND_RECEIVE_STATUS = "sendReceiveStatus";
    private static final String DATE_TIME = "dateTime";

    private String CREATE_TABLE_MSG = "CREATE TABLE IF NOT EXISTS " + TABLE_CHAT
            + "(" + ID + " INTEGER PRIMARY KEY, "
            + DEVICE_ID +" TEXT DEFAULT '', "
            + MSG +" TEXT DEFAULT '', "
            + SEND_RECEIVE_STATUS +" TEXT DEFAULT '', "
            + DATE_TIME +" TEXT DEFAULT '' )";


    private DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_MSG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IP, user.getIp());
        contentValues.put(NAME, user.getName());
        contentValues.put(PROFILE_PIC, user.getProfilePic());
        contentValues.put(STATUS, user.getStatus());
        contentValues.put(DEVICE_ID, user.getDeviceId());
        boolean a;
        if(user.getId() == -1){
            a = db.replace(TABLE_USER, null, contentValues)>0;
        }else{
            a = db.update(TABLE_USER,  contentValues, ID+" = '"+user.getId()+"'",null)>0;
        }
        db.close();

        return a;
    }

    public boolean addMsg(ChatMsg chatMsg){
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DEVICE_ID, chatMsg.getDeviceId());
        contentValues.put(MSG, chatMsg.getMsg());
        contentValues.put(SEND_RECEIVE_STATUS, chatMsg.getSendReceiveStatus());
        contentValues.put(DATE_TIME, dateFormat.format(date));

        String selectUser = "SELECT * FROM "+TABLE_USER+" WHERE "+ DEVICE_ID +" = '"+chatMsg.getDeviceId()+"'";
        Cursor cursor = db.rawQuery(selectUser, null);
        if(cursor.getCount()==0){
            ContentValues contentValuesUser = new ContentValues();
            contentValuesUser.put(IP, chatMsg.getIp());
            contentValuesUser.put(NAME, chatMsg.getDeviceId());
            contentValuesUser.put(STATUS, Constance.OTHER);
            contentValuesUser.put(DEVICE_ID, chatMsg.getDeviceId());
            db.replace(TABLE_USER, null, contentValuesUser);
        }else{
            ContentValues contentValuesUserIpUpdate = new ContentValues();
            contentValuesUserIpUpdate.put(IP, chatMsg.getIp());
            db.update(TABLE_USER,  contentValuesUserIpUpdate, DEVICE_ID+" = '"+chatMsg.getDeviceId()+"'",null);

        }
        cursor.close();
        boolean a = db.replace(TABLE_CHAT, null, contentValues)>0;
        db.close();

        return a;
    }

    public User getUserByPhoneNumber(int id){
        String query = "SELECT * FROM "+ TABLE_USER+" WHERE "+STATUS+" = "+ID+" = "+ id;

        return getContactObjSimple(query);
    }

    public User getMe(){
        String query = "SELECT * FROM "+ TABLE_USER+" WHERE "+STATUS+" = "+Constance.ME;

        return getContactObjSimple(query);
    }

    public List<User> getContactList(){
        String query = "SELECT * FROM "+ TABLE_USER + " WHERE "+STATUS+" = "+Constance.OTHER;

        return getContactListSimple(query);
    }

    public List<User> getContactListSimple(String query){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<User> userList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do{
                userList.add(getContactObjSimple(cursor));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public User getContactObjSimple(String query){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user = new User();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user = getContactObjSimple(cursor);
        }
        cursor.close();
        db.close();
        return user;
    }

    @SuppressLint("Range")
    public User getContactObjSimple(Cursor cursor){

        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        user.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
        user.setIp(cursor.getString(cursor.getColumnIndex(IP)));
        user.setProfilePic(cursor.getString(cursor.getColumnIndex(PROFILE_PIC)));
        user.setDeviceId(cursor.getString(cursor.getColumnIndex(DEVICE_ID)));

        return user;
    }


    @SuppressLint("Range")
    public List<ChatMsg> getChatMsg(String deviceId){
        String query = "SELECT * FROM "+ TABLE_CHAT + " WHERE "+ DEVICE_ID +" = '"+deviceId+"' LIMIT 50";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<ChatMsg> chatMsgs = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do{
                ChatMsg chatMsg = new ChatMsg();
                chatMsg.setMsg(cursor.getString(cursor.getColumnIndex(MSG)));
                chatMsg.setDateTime(cursor.getString(cursor.getColumnIndex(DATE_TIME)));
                chatMsg.setSendReceiveStatus(cursor.getInt(cursor.getColumnIndex(SEND_RECEIVE_STATUS)));
                chatMsg.setDeviceId(cursor.getString(cursor.getColumnIndex(DEVICE_ID)));
                chatMsgs.add(chatMsg);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return chatMsgs;
    }

    @SuppressLint("Range")
    public List<ChatHead> getChatHead(){
        List<ChatHead> chatHeadList = new ArrayList<>();
        String query = "SELECT User.*,Chat.msg,Chat.dateTime FROM User LEFT JOIN Chat ON User.deviceId = Chat.deviceId WHERE status = 2 GROUP BY User.deviceId ORDER BY dateTime DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do{
                ChatHead chatHead = new ChatHead();
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                user.setIp(cursor.getString(cursor.getColumnIndex(IP)));
                user.setProfilePic(cursor.getString(cursor.getColumnIndex(PROFILE_PIC)));
                user.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                user.setDeviceId(cursor.getString(cursor.getColumnIndex(DEVICE_ID)));

                chatHead.setUser(user);
                chatHead.setLastMsg(cursor.getString(cursor.getColumnIndex(MSG)));
                chatHead.setTime(cursor.getString(cursor.getColumnIndex(DATE_TIME)));
                chatHeadList.add(chatHead);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return chatHeadList;
    }

}
