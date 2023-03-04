package appship.lmlab.wifichatapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import appship.lmlab.wifichatapp.R;
import appship.lmlab.wifichatapp.adaptor.ContactAdaptor;
import appship.lmlab.wifichatapp.model.User;
import appship.lmlab.wifichatapp.util.Constance;
import appship.lmlab.wifichatapp.util.DataBase;
import appship.lmlab.wifichatapp.util.Utility;
import appship.lmlab.wifichatapp.view_model.ProfileViewModel;

public class ContactFragment extends Fragment implements View.OnClickListener{

    private RecyclerView contact_list;
    private FloatingActionButton fab_add;
    private View profile;
    private Context context;
    private ImageView dp_image;
    private TextView title;
    private LinearLayout action_bar_wrapper;

    public ContactFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        findView(view);
        showContactList();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void showContactList(){
        ContactAdaptor contactAdaptor = new ContactAdaptor(DataBase.getInstance(context).getContactList(), context) {
            @Override
            public void editUser(User user) {
                contact_list.setVisibility(View.GONE);
                profile.setVisibility(View.VISIBLE);
                fab_add.setVisibility(View.GONE);
                action_bar_wrapper.setVisibility(View.GONE);
                title.setText(user.getName());
                Utility.setAvatar(dp_image,user.getName(),50);
                ProfileViewModel profileViewModel = new ProfileViewModel(user, context, profile, Constance.OTHER, Constance.FROM_CONTACT_EDIT) {
                    @Override
                    public void backProfile() {
                        showContactList();
                    }
                };
                profileViewModel.init();

            }
        };
        title.setText("  Contact");
        contact_list.setLayoutManager(new LinearLayoutManager(context));
        contact_list.setAdapter(contactAdaptor);
        profile.setVisibility(View.GONE);
        dp_image.setVisibility(View.GONE);
        contact_list.setVisibility(View.VISIBLE);
        fab_add.setVisibility(View.VISIBLE);
        action_bar_wrapper.setVisibility(View.VISIBLE);
    }

    private void findView(View view){
        contact_list = view.findViewById(R.id.contact_list);
        profile = view.findViewById(R.id.profile);
        fab_add = view.findViewById(R.id.add_fab);
        title = view.findViewById(R.id.title);
        dp_image = view.findViewById(R.id.dp_image);
        action_bar_wrapper = view.findViewById(R.id.action_bar_wrapper);
        fab_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == fab_add){
            contact_list.setVisibility(View.GONE);
            profile.setVisibility(View.VISIBLE);
            fab_add.setVisibility(View.GONE);
            ProfileViewModel profileViewModel = new ProfileViewModel(new User(), context, profile, Constance.OTHER, Constance.FROM_CONTACT_ADD) {
                @Override
                public void backProfile() {
                    showContactList();
                }
            };
            profileViewModel.init();
        }
    }
}
