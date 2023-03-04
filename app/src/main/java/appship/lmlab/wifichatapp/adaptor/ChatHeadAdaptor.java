package appship.lmlab.wifichatapp.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import appship.lmlab.wifichatapp.R;
import appship.lmlab.wifichatapp.model.ChatHead;
import appship.lmlab.wifichatapp.model.User;
import appship.lmlab.wifichatapp.util.Utility;


public abstract class ChatHeadAdaptor extends RecyclerView.Adapter<ChatHeadAdaptor.ViewHolder> {

    private List<ChatHead> chatHeadList;
    private Context context;

    public ChatHeadAdaptor(List<ChatHead> chatHeadList, Context context) {
        this.chatHeadList = chatHeadList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatHeadAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.chathead_list_item, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHeadAdaptor.ViewHolder holder, int position) {
        ChatHead chatHead = chatHeadList.get(position);
        if(chatHead != null){
            holder.last_msg.setText(chatHead.getLastMsg());
            holder.name.setText(chatHead.getUser().getName());
            holder.time.setText(Utility.formatDate("yyyy-MM-dd HH:mm:ss","hh:mm a",chatHead.getTime()));
            Utility.setAvatar(holder.dp_image,chatHead.getUser().getName(),50);

            holder.wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openChat(chatHead.getUser());
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return chatHeadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView dp_image;
        TextView name;
        TextView last_msg;
        TextView time;
        LinearLayout wrapper;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dp_image = itemView. findViewById(R.id.dp_image);
            name = itemView. findViewById(R.id.name);
            last_msg = itemView. findViewById(R.id.last_msg);
            time = itemView. findViewById(R.id.time);
            wrapper = itemView. findViewById(R.id.wrapper);
        }
    }


    public abstract void openChat(User user);
}
