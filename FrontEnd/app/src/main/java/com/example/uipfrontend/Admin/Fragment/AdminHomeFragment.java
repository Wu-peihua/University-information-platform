package com.example.uipfrontend.Admin.Fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Activity.StudentModifyPasswordActivity;
import com.example.uipfrontend.Student.Activity.StudentMyReleaseActivity;
import com.example.uipfrontend.Student.Activity.StudentPersonalInfoActivity;

import java.util.Objects;


public class AdminHomeFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private Activity activity;

    private TextView name;  //用户昵称
    private TextView isVertify; //是否认证身份
    private ImageView imPortrait;  //头像
    private Uri portrait;  //头像uri

    private UserInfo userInfo;
    private String str_name ;
    private String uri_portrait;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_admin_home, null);
            activity = getActivity();

            init();
        }
        return rootView;
    }

    private void init(){
        //rootView.findViewById(R.id.rl_admin_home_release).setOnClickListener(this);
        rootView.findViewById(R.id.rl_admin_personalInfo).setOnClickListener(this);
        rootView.findViewById(R.id.rl_admin_home).setOnClickListener(this);
        rootView.findViewById(R.id.rl_admin_home_password).setOnClickListener(this);

        userInfo = (UserInfo) Objects.requireNonNull(getActivity()).getApplication();  //获取登录用户信息
        str_name = userInfo.getUserName();
        uri_portrait = userInfo.getPortrait();

        //初始化头像、昵称、手机号
        imPortrait = rootView.findViewById(R.id.iv_admin_home_portrait);
        portrait = getResourcesUri(R.drawable.ic_default_portrait);
        name = rootView.findViewById(R.id.tv_admin_home_name);
        isVertify = rootView.findViewById(R.id.tv_admin_home_isvertify);

        Glide.with(rootView.getContext()).load(userInfo.getPortrait()).into(imPortrait);
        name.setText(userInfo.getUserName());
        isVertify.setText("管理员");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_admin_personalInfo:
                Intent intent0 = new Intent(activity, StudentPersonalInfoActivity.class);
                intent0.putExtra("oldPortrait", uri_portrait);
                intent0.putExtra("oldNickname", str_name);
                startActivityForResult(intent0,0);
                break;
            case R.id.rl_admin_home_password:
                Intent intent3 = new Intent(activity, StudentModifyPasswordActivity.class);
                startActivity(intent3);
                break;
        }


    }

    private Uri getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        Uri drawableUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id));
        return drawableUri;
    }

    //修改用户信息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == 1) {
                String tmp = "";
                if (userInfo == null) {
                    userInfo = (UserInfo) Objects.requireNonNull(getActivity()).getApplication();
                }
                if (data.getStringExtra("newNickname") != null) {
                    tmp = data.getStringExtra("newNickname");
                    if (!tmp.equals(str_name)) {
                        str_name = tmp;
                        userInfo.setUserName(str_name);
                        name.setText(str_name);
                    }
                }
                if (data.getStringExtra("newPortrait") != null) {
                    tmp = data.getStringExtra("newPortrait");
                    if (!tmp.equals(uri_portrait)) {
                        uri_portrait = tmp;
                        userInfo.setPortrait(uri_portrait);
                        Glide.with(rootView.getContext()).load(uri_portrait)
                                .placeholder(R.drawable.portrait_default)
                                .error(R.drawable.portrait_default)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imPortrait);
                    }
                }
            }
        }
    }

}
