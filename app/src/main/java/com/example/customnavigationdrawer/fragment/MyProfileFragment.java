package com.example.customnavigationdrawer.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.customnavigationdrawer.MainActivity;
import com.example.customnavigationdrawer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MyProfileFragment extends Fragment {

    private View mView;
    private ImageView imgAvatar;
    private EditText edtFullname, edtEmail;
    private Button btnUpdateProfile, btnUpdateEmail;
    private MainActivity mMainActivity;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        initUi();
        setUserInformation();
        initListener();
        return mView;
    }

    private void initUi(){
        progressDialog = new ProgressDialog(getActivity());
        mMainActivity = (MainActivity) getActivity();
        imgAvatar = mView.findViewById(R.id.img_avatar);
        edtFullname = mView.findViewById(R.id.edt_full_name);
        edtEmail = mView.findViewById(R.id.edt_email_frag);
        btnUpdateProfile = mView.findViewById(R.id.btn_update_profile);
        btnUpdateEmail = mView.findViewById(R.id.btn_update_email);

    }

    public void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        edtFullname.setText(user.getDisplayName());
        edtEmail.setText(user.getEmail());
        //set ảnh để sau part 10
    }

    private void initListener(){
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateProfile();
            }
        });
        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateEmail();
            }
        });
    }

    private void onClickUpdateProfile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        String strFullName = edtFullname.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName).setPhotoUri(Uri.parse("https://i.pinimg.com/564x/cc/16/0c/cc160c19dbd165c43046c176223f10fe.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Update profile success", Toast.LENGTH_SHORT).show();
                            mMainActivity.showUserInformation();
                        }else{
                            Toast.makeText(getActivity(), "Fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onClickUpdateEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String strNewEmail = edtEmail.getText().toString().trim();
        progressDialog.show();
        if(user == null){
            return;
        }
        user.updateEmail(strNewEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User email address updated.", Toast.LENGTH_SHORT).show();
                            mMainActivity.showUserInformation();
                        }else{
                            Toast.makeText(getActivity(), "Fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
