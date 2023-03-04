package appship.lmlab.wifichatapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import appship.lmlab.wifichatapp.R;
import appship.lmlab.wifichatapp.model.User;
import appship.lmlab.wifichatapp.util.Constance;
import appship.lmlab.wifichatapp.util.DataBase;
import appship.lmlab.wifichatapp.view_model.ProfileViewModel;

public class ProfileFragment extends Fragment implements View.OnClickListener{


    private Context context;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ProfileViewModel profileViewModel = new ProfileViewModel(DataBase.getInstance(context).getMe(), context, view, Constance.ME, Constance.FROM_PROFILE) {
            @Override
            public void backProfile() {

            }
        };
        profileViewModel.init();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @Override
    public void onClick(View view) {

    }
}
