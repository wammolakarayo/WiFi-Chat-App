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
import appship.lmlab.wifichatapp.model.User;
import appship.lmlab.wifichatapp.util.Utility;

public abstract class ContactAdaptor extends RecyclerView.Adapter<ContactAdaptor.ViewHolder> {

    private List<User> userList;
    private Context context;

    public ContactAdaptor(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.contact_list_item, parent, false);
        return new ContactAdaptor.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdaptor.ViewHolder holder, int position) {
        User user = userList.get(position);
        if(user != null){
            holder.name.setText(user.getName());
            holder.ip.setText(user.getIp());

            Utility.setAvatar(holder.dp_image,user.getName(),50);
            holder.wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editUser(userList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,ip;
        LinearLayout wrapper;
        ImageView dp_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            ip = itemView.findViewById(R.id.ip);
            wrapper = itemView.findViewById(R.id.wrapper);
            dp_image = itemView.findViewById(R.id.dp_image);
        }
    }

    public abstract void editUser(User user);
}
