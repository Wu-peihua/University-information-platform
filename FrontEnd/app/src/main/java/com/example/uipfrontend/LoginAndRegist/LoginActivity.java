package com.example.uipfrontend.LoginAndRegist;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uipfrontend.Admin.AdminHomeActivity;
import com.example.uipfrontend.CommonUser.CommonUserActivity;
import com.example.uipfrontend.Entity.Institute;
import com.example.uipfrontend.Entity.ResponseUserInfo;
import com.example.uipfrontend.Entity.University;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.Entity.UserRecord;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.StudentActivity;
import com.example.uipfrontend.Utils.RSAUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, ViewTreeObserver.OnGlobalLayoutListener, TextWatcher {

    private ImageButton mIbNavigationBack;
    private EditText mEtLoginUsername;
    private EditText mEtLoginPwd;
    private LinearLayout mLlLoginUsername;
    private ImageView mIvLoginUsernameDel;
    private Button mBtLoginSubmit;
    private LinearLayout mLlLoginPwd;
    private ImageView mIvLoginPwdDel;
    private ImageView mIvLoginLogo;
    private LinearLayout mLayBackBar;
    private TextView mTvLoginForgetPwd;
    private Button mBtLoginRegister;

    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int MenuDataOk = 0;

    //顶部筛选菜单选项
    private String[] levelOneUniversityMenu;
    private String[][] levelTwoInstituteMenu;

    String publicKey;

    int userType = 0; //默认用户类型为学生

    //全局Toast
    private Toast mToast;

    private int mLogoHeight;
    private int mLogoWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        getMenusData();
    }

    //初始化视图
    private void initView() {

        //导航栏+返回按钮
        mLayBackBar = findViewById(R.id.ly_retrieve_bar);
        mIbNavigationBack = findViewById(R.id.ib_navigation_back);

        //logo
        mIvLoginLogo = findViewById(R.id.iv_login_logo);

        //username
        mLlLoginUsername = findViewById(R.id.ll_login_username);
        mEtLoginUsername = findViewById(R.id.et_login_username);
        mIvLoginUsernameDel = findViewById(R.id.iv_login_username_del);

        //passwd
        mLlLoginPwd = findViewById(R.id.ll_login_pwd);
        mEtLoginPwd = findViewById(R.id.et_login_pwd);
        mIvLoginPwdDel = findViewById(R.id.iv_login_pwd_del);

        //提交、注册
        mBtLoginSubmit = findViewById(R.id.bt_login_submit);
        mBtLoginRegister = findViewById(R.id.bt_login_register);

        //忘记密码
        mTvLoginForgetPwd = findViewById(R.id.tv_login_forget_pwd);
        mTvLoginForgetPwd.setOnClickListener(this);

        //注册点击事件
//        mIbNavigationBack.setOnClickListener(this);
        mIbNavigationBack.setVisibility(View.INVISIBLE);
        mEtLoginUsername.setOnClickListener(this);
        mIvLoginUsernameDel.setOnClickListener(this);
        mBtLoginSubmit.setOnClickListener(this);
        mBtLoginRegister.setOnClickListener(this);
        mEtLoginPwd.setOnClickListener(this);
        mIvLoginPwdDel.setOnClickListener(this);


        //注册其它事件
        mLayBackBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mEtLoginUsername.setOnFocusChangeListener(this);
        mEtLoginUsername.addTextChangedListener(this);
        mEtLoginPwd.setOnFocusChangeListener(this);
        mEtLoginPwd.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                //返回
                finish();
                break;
            case R.id.et_login_username:
                mEtLoginPwd.clearFocus();
                mEtLoginUsername.setFocusableInTouchMode(true);
                mEtLoginUsername.requestFocus();
                break;
            case R.id.et_login_pwd:
                mEtLoginUsername.clearFocus();
                mEtLoginPwd.setFocusableInTouchMode(true);
                mEtLoginPwd.requestFocus();
                break;
            case R.id.iv_login_username_del:
                //清空用户名
                mEtLoginUsername.setText(null);
                break;
            case R.id.iv_login_pwd_del:
                //清空密码
                mEtLoginPwd.setText(null);
                break;
            case R.id.bt_login_submit:
                //登录
                getPublicKeyAndLogin(getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.getPublicKey),
                        getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.loginUrl)
                        ,mEtLoginUsername.getText().toString(),mEtLoginPwd.getText().toString());
                break;
            case R.id.bt_login_register:
                //注册
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.tv_login_forget_pwd:
                //忘记密码
                startActivity(new Intent(LoginActivity.this, ForgetPwActivity.class));
                break;
            case R.id.ll_login_layer:

                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    //用户名密码输入事件
    @Override
    public void afterTextChanged(Editable s) {
        String username = mEtLoginUsername.getText().toString().trim();
        String pwd = mEtLoginPwd.getText().toString().trim();

        //是否显示清除按钮
        if (username.length() > 0) {
            mIvLoginUsernameDel.setVisibility(View.VISIBLE);
        } else {
            mIvLoginUsernameDel.setVisibility(View.INVISIBLE);
        }
        if (pwd.length() > 0) {
            mIvLoginPwdDel.setVisibility(View.VISIBLE);
        } else {
            mIvLoginPwdDel.setVisibility(View.INVISIBLE);
        }

        //登录按钮是否可用
        if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(username)) {
            mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
            mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));
        } else {
            mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
            mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
        }
    }


    //用户名密码焦点改变
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();

        if (id == R.id.et_login_username) {
            if (hasFocus) {
                mLlLoginUsername.setActivated(true);
                mLlLoginPwd.setActivated(false);
            }
        } else {
            if (hasFocus) {
                mLlLoginPwd.setActivated(true);
                mLlLoginUsername.setActivated(false);
            }
        }
    }

    //显示或隐藏logo
    @Override
    public void onGlobalLayout() {
        final ImageView ivLogo = this.mIvLoginLogo;
        Rect KeypadRect = new Rect();

        mLayBackBar.getWindowVisibleDisplayFrame(KeypadRect);

        int screenHeight = mLayBackBar.getRootView().getHeight();
        int keypadHeight = screenHeight - KeypadRect.bottom;

        //隐藏logo
        if (keypadHeight > 300 && ivLogo.getTag() == null) {
            final int height = ivLogo.getHeight();
            final int width = ivLogo.getWidth();
            this.mLogoHeight = height;
            this.mLogoWidth = width;

            ivLogo.setTag(true);

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = ivLogo.getLayoutParams();
                    layoutParams.height = (int) (height * animatedValue);
                    layoutParams.width = (int) (width * animatedValue);
                    ivLogo.requestLayout();
                    ivLogo.setAlpha(animatedValue);
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();
        }
        //显示logo
        else if (keypadHeight < 300 && ivLogo.getTag() != null) {
            final int height = mLogoHeight;
            final int width = mLogoWidth;

            ivLogo.setTag(null);

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = ivLogo.getLayoutParams();
                    layoutParams.height = (int) (height * animatedValue);
                    layoutParams.width = (int) (width * animatedValue);
                    ivLogo.requestLayout();
                    ivLogo.setAlpha(animatedValue);
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();
        }
    }


    private void getPublicKeyAndLogin(String requestUrl1,String requestUrl2, String userName,String password){

        ZLoadingDialog dialog = new ZLoadingDialog(LoginActivity.this);
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(getResources().getColor(R.color.blue))//颜色
                .setHintText("加载中...")
                .show();

        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            public void handleMessage(@NotNull Message msg) {
                switch (msg.what){
                    case SUCCESS:
                        loginRequest(requestUrl2,userName,password);
                        dialog.dismiss();
                        break;
                    case FAIL:
                        System.out.println("获取公钥失败！");
                        dialog.dismiss();
                        break;
                }
            }
        };

        new Thread(()->{

            Request request = new Request.Builder()
                    .url(requestUrl1)
                    .get()
                    .build();
            Message msg = new Message();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("获取: ", e.getMessage());
                    msg.what = FAIL;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseStr = response.body().string();
                    Log.i("RESPONSE",responseStr);

                    JsonObject jsonObject = new JsonParser().parse(responseStr).getAsJsonObject();
                    JsonElement element = jsonObject.get("publicKey");

                    publicKey = new Gson().fromJson(element,String.class);
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }
            });
        }).start();


    }


    //登录请求
    private void loginRequest(String requestUrl2,String userName,String password) {
        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            public void handleMessage(@NotNull Message msg) {
                switch (msg.what){
                    case SUCCESS:
                        //根据用户不同的类型跳转到不同的页面
                        Intent i = new Intent(LoginActivity.this , CommonUserActivity.class);
                        if(userType == 0){
                            i = new Intent(LoginActivity.this , CommonUserActivity.class);
                        }else if(userType == 1){
                            i = new Intent(LoginActivity.this, StudentActivity.class);
                        }else{
                            i = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        }
                        startActivity(i);
                        break;
                    case FAIL:
                        Toast.makeText(LoginActivity.this,"登录失败！",Toast.LENGTH_SHORT).show();
                        mBtLoginSubmit.setBackground(getResources().getDrawable(R.drawable.bg_login_submit));
                        mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                        mBtLoginSubmit.setEnabled(true);
                        break;

                }
            }
        };

        new Thread(()->{
            //设置请求体并设置contentType
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("userName", userName);

            //使用publicKey加密
            String postPassword;
            RSAPublicKey key = null;

            try {
                key = RSAUtil.getPublicKey(publicKey);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
            //公钥加密后的结果，同一数据每次加密结果不同
            postPassword = RSAUtil.publicEncrypt(password,key);

            builder.add("pwd",postPassword);

            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(requestUrl2)
                    .post(requestBody)
                    .build();
            Message msg = new Message();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("获取: ", e.getMessage());
                    msg.what = FAIL;
                    handler.sendMessage(msg);
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseStr = response.body().string();
                    Log.i("RESPONSE",responseStr);

                    JsonObject jsonObject = new JsonParser().parse(responseStr).getAsJsonObject();
                    JsonElement element = jsonObject.get("success");

                    boolean success = new Gson().fromJson(element,boolean.class);
                    if(success){
                        JsonElement userInfo = jsonObject.get("userInfo");
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        ResponseUserInfo userInfo1 = gson.fromJson(userInfo,ResponseUserInfo.class);
                        System.out.println(userInfo1);
                        msg.what = SUCCESS;
                        //将用户信息存放到getApplication中
                        UserInfo user = (UserInfo) getApplication();
                        user.setUserId(userInfo1.getUserId());
                        user.setUserName(userInfo1.getUserName());
                        user.setPortrait(userInfo1.getPortrait());
                        // user.setCreated(userInfo1.getCreated()); // TODO date问题
                        user.setInstituteId(userInfo1.getInstituteId());
                        user.setPw(userInfo1.getPw());
                        user.setUniversityId(userInfo1.getUniversityId());
                        user.setStuCard(userInfo1.getStuCard());
                        user.setStuNumber(userInfo1.getStuNumber());
                        user.setUserType(userInfo1.getUserType());
                        if(user.getUserType() == 0){
                            userType = 0;
                        }else if(user.getUserType() == 1){
                            userType = 1;
                        }else{
                            userType = 2;
                        }
                        getUserRecord(user);

                    }else{
                        JsonElement element1 = jsonObject.get("msg");
                        String msgStr = new Gson().fromJson(element1,String.class);
                        System.out.println("msg:"+msgStr);
                        msg.what = FAIL;
                    }

                    handler.sendMessage(msg);
                }
            });
        }).start();
    }



    private void getMenusData(){
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == MenuDataOk) {//初始化下拉筛选
                }
                super.handleMessage(msg);
            }
        };


        //发送http请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request requestUniversity = new Request.Builder().url(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryUniversity)).build();
                Request requestInstitute = new Request.Builder().url(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryInstitute)).build();

                try {
                    //请求大学目录
                    Response responseUniversity = client.newCall(requestUniversity).execute();//发送请求
                    Response responseInstitute = client.newCall(requestInstitute).execute();
                    String resultUniversity = Objects.requireNonNull(responseUniversity.body()).string();
                    String resultInstitute = Objects.requireNonNull(responseInstitute.body()).string();


                    //解析大学json字符串数组
                    JsonObject jsonObjectUniversity = new JsonParser().parse(resultUniversity).getAsJsonObject();
                    JsonArray jsonArrayUniversity = jsonObjectUniversity.getAsJsonArray("universityList");
                    //解析专业json字符串数组
                    JsonObject jsonObjectInstitute = new JsonParser().parse(resultInstitute).getAsJsonObject();
                    JsonArray jsonArrayInstitute = jsonObjectInstitute.getAsJsonArray("instituteList");

                    //初始化菜单栏
                    levelOneUniversityMenu = new String[jsonArrayUniversity.size()];
                    levelTwoInstituteMenu = new String[jsonArrayUniversity.size()][jsonArrayInstitute.size()];

                    //设置sharepreference保存大学菜单和专业菜单
                    SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
                    @SuppressLint("CommitPrefEdits")
                    SharedPreferences.Editor et = sp.edit();

                    List<String> universityList = new ArrayList<>();
                    List<String> instituteList = new ArrayList<>();


                    //循环遍历数组
                    int index = 0;
                    for (JsonElement jsonElement : jsonArrayUniversity) {
                        University university = new Gson().fromJson(jsonElement, new TypeToken<University>() {
                        }.getType());
                        levelOneUniversityMenu[index] = university.getUniversityName();
                        universityList.add(levelOneUniversityMenu[index]);
                        ++index;
                    }

                    for (JsonElement jsonElement : jsonArrayInstitute) {
                        Institute institute = new Gson().fromJson(jsonElement, new TypeToken<Institute>() {
                        }.getType());
                        instituteList.add(institute.getInstituteName());
                    }

                    StringBuilder universityStrBuilder = new StringBuilder();
                    StringBuilder instituteStrBuilder = new StringBuilder();
                    boolean first = true;

                    //将universityList转为String
                    for(String string :universityList) {
                        if(first) {
                            first=false;
                        }else{
                            universityStrBuilder.append(",");
                        }
                        universityStrBuilder.append(string);
                    }

                    first = true;
                    //将universityList转为String
                    for(String string :instituteList) {
                        if(first) {
                            first=false;
                        }else{
                            instituteStrBuilder.append(",");
                        }
                        instituteStrBuilder.append(string);
                    }



                    //将大学数组和专业数组添加到shareprefernece
                    et.putString("university", universityStrBuilder.toString());
                    et.putString("institute",instituteStrBuilder.toString());
                    et.commit();

                    Message msg = new Message();
                    msg.what = MenuDataOk;
                    handler.sendMessage(msg);

                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void getUserRecord(UserInfo user) {
        new Thread(()->{

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.getUserRecord)
                            + "/?userId=" + user.getUserId())
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取用户记录: ", Objects.requireNonNull(e.getMessage()));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("获取用户记录: ", resStr);

                    JsonObject jsonObject = new JsonParser().parse(resStr).getAsJsonObject();
                    JsonArray jsonArray1 = jsonObject.getAsJsonArray("likeRecord");
                    JsonArray jsonArray2 = jsonObject.getAsJsonArray("reportRecord");

                    Map<String, Long> map1 = new HashMap<>();
                    Map<String, Long> map2 = new HashMap<>();
                    Gson gson = new Gson();
                    for (JsonElement element : jsonArray1) {
                        UserRecord record = gson.fromJson(element, new TypeToken<UserRecord>() {}.getType());
                        String key = "";
                        switch (record.getType()) {
                            case 1: key = "post"; break;
                            case 2: key = "comment"; break;
                            case 3: key = "reply"; break;
                            case 5: key ="course_comment";break;
                            case 6: key="resource"; break;
                        }
                        key += record.getToId();
                        map1.put(key, record.getInfoId());
                    }
                    for (JsonElement element : jsonArray2) {
                        UserRecord record = gson.fromJson(element, new TypeToken<UserRecord>() {}.getType());
                        String key = "";
                        switch (record.getType()) {
                            case 1: key = "post"; break;
                            case 2: key = "comment"; break;
                            case 3: key = "reply"; break;
                            case 5: key ="course_comment";break;
                            case 6: key="resource"; break;
                        }
                        key += record.getToId();
                        map2.put(key, record.getInfoId());
                    }

                    user.setLikeRecord(map1);
                    user.setReportRecord(map2);
                }
            });
        }).start();
    }

}
