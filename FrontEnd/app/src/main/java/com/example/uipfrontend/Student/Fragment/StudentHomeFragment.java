package com.example.uipfrontend.Student.Fragment;

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
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Activity.StudentModifyPasswordActivity;
import com.example.uipfrontend.Student.Activity.StudentMyReleaseActivity;
import com.example.uipfrontend.Student.Activity.StudentPersonalInfoActivity;


public class StudentHomeFragment extends Fragment  implements View.OnClickListener{

    private View rootView;
    private Activity activity;

    private TextView name;  //用户昵称
    private TextView isVertify; //是否认证身份
    private ImageView imPortrait;  //头像
    private Uri portrait;  //头像uri

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_student_home, null);
            activity = getActivity();

            init();
        }
        return rootView;
    }

    private void init(){
        rootView.findViewById(R.id.rl_student_personalInfo).setOnClickListener(this);
        rootView.findViewById(R.id.rl_student_home_release).setOnClickListener(this);
        rootView.findViewById(R.id.rl_studentModifyPassword).setOnClickListener(this);
        rootView.findViewById(R.id.rl_studentInfo).setOnClickListener(this);


        //初始化头像、昵称、手机号
        imPortrait = rootView.findViewById(R.id.iv_student_home_portrait);
        portrait = getResourcesUri(R.drawable.ic_default_portrait);
        name = rootView.findViewById(R.id.tv_student_home_name);
        isVertify = rootView.findViewById(R.id.tv_student_home_isvertify);

        Glide.with(rootView.getContext()).load(portrait).into(imPortrait);
        name.setText("用户名");
        isVertify.setText("是");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_student_personalInfo:
                Intent intent0 = new Intent(activity, StudentPersonalInfoActivity.class);
                startActivity(intent0);
                break;
            case R.id.rl_student_home_vertify:

                break;

            case R.id.rl_student_home_release:
                Intent intent2 = new Intent(activity, StudentMyReleaseActivity.class);
                startActivity(intent2);
                break;

            case R.id.rl_studentModifyPassword:
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
}