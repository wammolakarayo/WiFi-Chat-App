package appship.lmlab.wifichatapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import appship.lmlab.wifichatapp.R;
import appship.lmlab.wifichatapp.adaptor.ChatHeadAdaptor;
import appship.lmlab.wifichatapp.adaptor.ChatMessageAdaptor;
import appship.lmlab.wifichatapp.model.ChatMsg;
import appship.lmlab.wifichatapp.model.User;
import appship.lmlab.wifichatapp.observer.MsgCallBack;
import appship.lmlab.wifichatapp.observer.MsgObserver;
import appship.lmlab.wifichatapp.util.Constance;
import appship.lmlab.wifichatapp.util.DataBase;
import appship.lmlab.wifichatapp.util.SendMessage;
import appship.lmlab.wifichatapp.util.Utility;

public class ChatHeadFragment extends Fragment implements View.OnClickListener{

    private RecyclerView chat_list;
    private Context context;
    private ImageView dp_image,back,send_button,sync_icon;
    private TextView title;
    private LinearLayout footer_section;
    private EditText message_text;
    private ChatMessageAdaptor contactAdaptor;
    private User selectedUser = null;

    public ChatHeadFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_head_fragment, container, false);
        findViews(view);
        showChatHeadList();
        saveReceivedMsg();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void showChatHeadList(){
        ChatHeadAdaptor chatHeadAdaptor = new ChatHeadAdaptor(DataBase.getInstance(context).getChatHead(), context) {
            @Override
            public void openChat(User user) {
                showChatList(user);
            }
        };
        title.setText("  Chat");
        chat_list.setLayoutManager(new LinearLayoutManager(context));
        chat_list.setAdapter(chatHeadAdaptor);
        dp_image.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        footer_section.setVisibility(View.GONE);

    }

    private void showChatList(User user){
        this.selectedUser = user;
        contactAdaptor = new ChatMessageAdaptor(DataBase.getInstance(context).getChatMsg(user.getDeviceId()),context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);
        chat_list.setLayoutManager(linearLayoutManager);
        chat_list.setAdapter(contactAdaptor);
        dp_image.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        footer_section.setVisibility(View.VISIBLE);
        title.setText(user.getName());
        Utility.setAvatar(dp_image,user.getName(),50);

    }

    private void sendMsg(){
        if(selectedUser != null && message_text.getText().toString().length()>0){
            send_button.setEnabled(false);
            sync_icon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_1));
            sync_icon.setVisibility(View.VISIBLE);
            ChatMsg chatMsg = new ChatMsg();
            chatMsg.setMsg(message_text.getText().toString());
            chatMsg.setDeviceId(Utility.getAndroidId(context));
            chatMsg.setIp(Utility.getIp(context));
            Gson gson = new Gson();
            String json = gson.toJson(chatMsg);
            SendMessage sendMessage = new SendMessage(selectedUser.getIp(),json) {
                @Override
                public void isSuccess(boolean success) {
                    send_button.setEnabled(true);
                    sync_icon.clearAnimation();
                    sync_icon.setVisibility(View.INVISIBLE);
                    if(success){
                        ChatMsg chatMsg = new ChatMsg();
                        chatMsg.setSendReceiveStatus(Constance.SEND);
                        chatMsg.setMsg(message_text.getText().toString());
                        chatMsg.setDeviceId(selectedUser.getDeviceId());
                        DataBase.getInstance(context).addMsg(chatMsg);
                        message_text.setText("");
                        List<ChatMsg> chatMsgList = DataBase.getInstance(context).getChatMsg(selectedUser.getDeviceId());
                        contactAdaptor.setChatMsgList(chatMsgList);
                        contactAdaptor.notifyDataSetChanged();
                        chat_list.scrollToPosition(chatMsgList.size() - 1);

                    }

                }
            };
            sendMessage.send();
        }
    }

    private void saveReceivedMsg(){
        MsgObserver.getCustomerOrderObserver().setMsgObserverCallback(new MsgCallBack() {
            @Override
            public void onCustomerOrderUpdate() {
                if(contactAdaptor != null && selectedUser != null){
                    List<ChatMsg> chatMsgList = DataBase.getInstance(context).getChatMsg(selectedUser.getDeviceId());
                    contactAdaptor.setChatMsgList(chatMsgList);
                    contactAdaptor.notifyDataSetChanged();
                    chat_list.scrollToPosition(chatMsgList.size() - 1);

                }

            }
        });
    }

    private void findViews(View view){
        footer_section = view.findViewById(R.id.footer_section);
        chat_list = view.findViewById(R.id.chat_list);
        dp_image = view.findViewById(R.id.dp_image);
        title = view.findViewById(R.id.title);
        sync_icon = view.findViewById(R.id.sync_icon);
        sync_icon.setVisibility(View.INVISIBLE);
        message_text = view.findViewById(R.id.message_text);
        send_button = view.findViewById(R.id.send_button);
        send_button.setOnClickListener(this);
        back = view.findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == back){
            showChatHeadList();
        }else if(view == send_button){
            sendMsg();
        }
    }
}
