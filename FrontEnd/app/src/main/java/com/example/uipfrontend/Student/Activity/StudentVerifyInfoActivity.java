package com.example.uipfrontend.Student.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uipfrontend.CommonUser.Activity.PostDetailActivity;
import com.example.uipfrontend.CommonUser.Activity.StudentVerifyActivity;
import com.example.uipfrontend.Entity.Certification;
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentVerifyInfoActivity extends AppCompatActivity {

    private  TextView state;//认证状态 未完成-0 完成-1
    private TextView school;     // 高校
    private TextView academy;    //学院
    private TextView stuNumber;  // 学号
    private TextView stuName;    // 姓名
    private NineGridView cer_pictures; // 学生证照片

    private List<String> universityList = new ArrayList<>();//大学列表
    private List<String> instituteList = new ArrayList<>();//学院列表
    private Certification certification; // 获取认证信息
    private UserInfo user;//全局用户

    private static final int NETWORK_ERR = -2;
    private static final int SERVER_ERR = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            finish();
        }
        setContentView(R.layout.activity_student_verifyinfo);
        init();
        //接受点击事件传参数
    }

    private void init(){
        initToolBar();
        initView();
        getMenusData();
        queryStudentVerifyData();
    }

    public void initView(){
        NineGridView.setImageLoader(new PicassoImageLoader());
        school = findViewById(R.id.tv_info_school);
        stuNumber = findViewById(R.id.tv_info_cardid);
        academy = findViewById(R.id.tv_info_academy);
        stuName = findViewById(R.id.tv_info_name);
        cer_pictures = findViewById(R.id.nineGrid_student_verify_picture);
        state = findViewById(R.id.tv_info_state);
    }
    // 自定义toolbar
    public void initToolBar() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        setTitle("");
        Toolbar toolbar = findViewById(R.id.cu_toolbar_studentVerify);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Picasso 加载
     */
    private static class PicassoImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Picasso.with(context).load(url)//
                    .placeholder(R.drawable.ic_default_image)//
                    .error(R.drawable.ic_default_image)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }


    public void queryStudentVerifyData() {
        user = (UserInfo) getApplication();
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("获取认证信息: ", "失败 - 网络错误");
                        Toast.makeText(StudentVerifyInfoActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                        break;
                    case SERVER_ERR:
                        Log.i("获取认证信息: ", "失败 - 服务器错误");
                        break;
                    case ZERO:
                        Log.i("获取认证信息: ", "未提交");
                        setVerifyInfo();
                        break;
                    case SUCCESS:
                        Log.i("获取认证信息: ", "成功");
                        setVerifyInfo();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread(()->{

            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.selectCertificationByUserId)
                            + "/?userId=" + user.getUserId())
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取认证信息: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("获取认证信息: ", resStr);

                    JsonObject jsonObject = new JsonParser().parse(resStr).getAsJsonObject();
                    JsonElement element = jsonObject.get("certification");

                    certification = new Gson().fromJson(element, Certification.class);

                    if (certification == null) {
                        msg.what = ZERO;
                    } else {
                        // TODO: 通过 - 提示重新登录，状态改为"已通过"；
                        //  不通过 - 弹框提示，点击关闭按钮时删除原来提交的认证信息，回到原始提交页面；
                        //  待审核 - (已经实现)
                        msg.what = SUCCESS;
                    }
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }

    private void getMenusData(){
        SharedPreferences sp = Objects.requireNonNull(this).getSharedPreferences("data",MODE_PRIVATE);
        //第二个参数为缺省值，如果不存在该key，返回缺省值
        String strUniversity = sp.getString("university",null);
        String strInstitute = sp.getString("institute",null);

        String str1[] = strUniversity != null ? strUniversity.split(",") : new String[0];
        universityList = Arrays.asList(str1);
        String str[] = strInstitute != null ? strInstitute.split(",") : new String[0];
        instituteList = Arrays.asList(str);

    }

    private void setVerifyInfo(){
        //数据库无student_certification记录或者 审核还没完成
        if(certification==null||certification.getStuCer()==0) {
            state.setText("审核未完成");
        }

        else{
            int universityOption = certification.getUniversityId();
            int instituteOption = certification.getInstitudeId();
            String universitystr = universityList.get(universityOption-1);
            String institutestr  = instituteList.get(instituteOption-1);

            school.setText(universitystr);
            academy.setText(institutestr);
            stuNumber.setText(certification.getStuNumber());
            stuName.setText(certification.getStuName());
            state.setText("已完成认证");

            String url = certification.getStuCard();
            List<ImageInfo> imageList = new ArrayList<>();
            if(url!=null){
                String[] selectString = url.split(",");
                for (String picUrl : selectString) {
                    picUrl = picUrl.replace("localhost", getResources().getString(R.string.myIP));
                    picUrl = picUrl.replace("\"", "");
                    ImageInfo image = new ImageInfo();
                    image.setThumbnailUrl(picUrl);
                    image.setBigImageUrl(picUrl);
                    imageList.add(image);
                }

            }else{
            }
            cer_pictures.setAdapter(new NineGridViewClickAdapter(this, imageList));

        }
    }

}
