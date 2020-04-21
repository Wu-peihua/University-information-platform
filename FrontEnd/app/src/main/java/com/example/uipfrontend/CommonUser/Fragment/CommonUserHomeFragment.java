package com.example.uipfrontend.CommonUser.Fragment;

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
import com.example.uipfrontend.CommonUser.Activity.CommonUserModifyPasswordActivity;
import com.example.uipfrontend.CommonUser.Activity.CommonUserMyReleaseActivity;
import com.example.uipfrontend.CommonUser.Activity.CommonUserPersonalInfoActivity;
import com.example.uipfrontend.CommonUser.Activity.StudentVerifyActivity;
import com.example.uipfrontend.R;

public class CommonUserHomeFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Activity activity;

    private TextView tv_name;
    private String str_name = "立即登录";
    private TextView tv_isVertify;
    private ImageView iv_portrait;
    private Uri uri_portrait;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_cu_home, null);
            activity = getActivity();

            init();
        }
        return rootView;
    }

    private void init() {
        rootView.findViewById(R.id.rl_cu_home_personalInfo).setOnClickListener(this);
        rootView.findViewById(R.id.rl_cu_home_vertify).setOnClickListener(this);
        rootView.findViewById(R.id.rl_cu_home_release).setOnClickListener(this);
        rootView.findViewById(R.id.rl_cu_home_password).setOnClickListener(this);

        tv_name = rootView.findViewById(R.id.tv_cu_home_name);
        tv_isVertify = rootView.findViewById(R.id.tv_cu_home_isVertify);
        iv_portrait = rootView.findViewById(R.id.iv_cu_home_portrait);
        uri_portrait = getResourcesUri(R.drawable.ic_default_portrait);

        str_name = "张咩阿";
        tv_name.setText(str_name);
        tv_isVertify.setText("否");
        Glide.with(rootView.getContext()).load(uri_portrait)
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_portrait);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_cu_home_personalInfo:
                Intent intent0 = new Intent(activity, CommonUserPersonalInfoActivity.class);
                intent0.putExtra("oldPortrait", uri_portrait.toString());
                intent0.putExtra("oldNickname", str_name);
                startActivityForResult(intent0, 0);
                break;
            case R.id.rl_cu_home_vertify:
                startActivity(new Intent(activity, StudentVerifyActivity.class));
                break;
            case R.id.rl_cu_home_release:
                startActivity(new Intent(activity, CommonUserMyReleaseActivity.class));
                break;
            case R.id.rl_cu_home_password:
                startActivity(new Intent(activity, CommonUserModifyPasswordActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == 1) {
                if (data.getStringExtra("newNickname") != null) {
                    str_name = data.getStringExtra("newNickname");
                    tv_name.setText(str_name);
                }
                if (data.getStringExtra("newPortrait") != null) {
                    uri_portrait = Uri.parse(data.getStringExtra("newPortrait"));
                    Glide.with(rootView.getContext()).load(uri_portrait)
                            .placeholder(R.drawable.portrait_default)
                            .error(R.drawable.portrait_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(iv_portrait);
                }
            }
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
}
