package appship.lmlab.wifichatapp.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import appship.lmlab.wifichatapp.R;
import appship.lmlab.wifichatapp.model.ChatMsg;
import appship.lmlab.wifichatapp.util.Constance;
import appship.lmlab.wifichatapp.util.Utility;

public class ChatMessageAdaptor extends RecyclerView.Adapter<ChatMessageAdaptor.ViewHolder> {

    private List<ChatMsg> chatMsgList;
    private Context context;

    public ChatMessageAdaptor(List<ChatMsg> chatMsgList, Context context) {
        this.chatMsgList = chatMsgList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatMessageAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.chat_received_msg_list_item, parent, false);
        switch (viewType) {
            case Constance.RECEIVE:
                contactView = inflater.inflate(R.layout.chat_received_msg_list_item, parent, false);
                break;

            case Constance.SEND:
                contactView = inflater.inflate(R.layout.chat_sent_msg_list_item, parent, false);
                break;
        }
        return new ChatMessageAdaptor.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageAdaptor.ViewHolder holder, int position) {
        ChatMsg chatMsg = chatMsgList.get(position);
        if(chatMsg != null){
            holder.msg.setText(chatMsg.getMsg());
            holder.time.setText(Utility.formatDate("yyyy-MM-dd HH:mm:ss","hh:mm a",chatMsg.getDateTime()));
        }

    }

    @Override
    public int getItemCount() {
        return chatMsgList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return chatMsgList.get(position).getSendReceiveStatus() == Constance.SEND ? Constance.SEND : Constance.RECEIVE;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
            time = itemView.findViewById(R.id.time);
        }
    }

    public void setChatMsgList(List<ChatMsg> chatMsgList) {
        this.chatMsgList = chatMsgList;
    }
}
