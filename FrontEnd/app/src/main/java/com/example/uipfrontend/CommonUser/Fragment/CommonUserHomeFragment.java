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
import com.example.uipfrontend.CommonUser.Activity.CommonUserMyReleaseActivity;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Activity.StudentMyReleaseActivity;

public class CommonUserHomeFragment extends Fragment implements View.OnClickListener {

    private View     rootView;
    private Activity activity;

    private TextView  tv_name;
    private TextView  tv_isVertify;
    private ImageView im_portrait;
    private Uri       portrait;

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
        rootView.findViewById(R.id.rl_cu_home_release).setOnClickListener(this);


        tv_name = rootView.findViewById(R.id.tv_cu_home_name);
        tv_isVertify = rootView.findViewById(R.id.tv_cu_home_isVertify);
        im_portrait = rootView.findViewById(R.id.iv_cu_home_portrait);
        portrait = getResourcesUri(R.drawable.ic_default_portrait);

        tv_name.setText("用户名");
        tv_isVertify.setText("否");
        Glide.with(rootView.getContext()).load(portrait).into(im_portrait);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_cu_home_personalInfo:
                break;
            case R.id.rl_cu_home_vertify:
                break;
            case R.id.rl_cu_home_release:
                startActivity(new Intent(activity, CommonUserMyReleaseActivity.class));
                break;
            case R.id.rl_cu_home_password:
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
}
