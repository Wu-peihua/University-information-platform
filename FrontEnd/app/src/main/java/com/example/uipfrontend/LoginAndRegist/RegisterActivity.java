package com.example.uipfrontend.LoginAndRegist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uipfrontend.Entity.ResponseUserInfo;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Utils.RSAUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int DUPLICATE = 2;
    String publicKey;


    EditText userName;
    EditText password;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_step_one);

        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
        findViewById(R.id.bt_register_submit).setOnClickListener(this);
        userName = findViewById(R.id.et_register_username);
        password = findViewById(R.id.et_register_pwd_input);
        register = findViewById(R.id.bt_register_submit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.bt_register_submit:
                //前端判断用户名与密码表单
                if(userName.getText() == null || userName.getText().toString().equals("") || password.getText() == null || password.getText().toString().equals("")){
                    Toast.makeText(this,"用户名或密码为空！",Toast.LENGTH_SHORT).show();
                }else{
                    register.setBackground(getResources().getDrawable(R.drawable.bg_login_submit_lock));
                    register.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    register.setEnabled(false);
                    getPublicKeyAndRegist(getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.getPublicKey),
                            getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.registUrl),
                            userName.getText().toString(),password.getText().toString());
                }

                break;
        }
    }

    private void getPublicKeyAndRegist(String requestUrl1,String requestUrl2, String userName,String password){

        ZLoadingDialog dialog = new ZLoadingDialog(RegisterActivity.this);
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(getResources().getColor(R.color.blue))//颜色
                .setHintText("加载中...")
                .show();

        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            public void handleMessage(@NotNull Message msg) {
                switch (msg.what){
                    case SUCCESS:
                        regist(requestUrl2,userName,password);
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

    private void regist(String requestUrl, String userName,String password){
        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            public void handleMessage(@NotNull Message msg) {
                switch (msg.what){
                    case SUCCESS:
                        //跳转到登录页面
                        Intent i = new Intent(RegisterActivity.this , LoginActivity.class);
                        startActivity(i);
                        break;
                    case FAIL:
                        Toast.makeText(RegisterActivity.this,"注册失败！",Toast.LENGTH_SHORT).show();
                        register.setBackground(getResources().getDrawable(R.drawable.bg_login_submit));
                        register.setTextColor(getResources().getColor(R.color.white));
                        register.setEnabled(true);
                        break;
                    case DUPLICATE:
                        Toast.makeText(RegisterActivity.this,"用户名已存在！",Toast.LENGTH_SHORT).show();
                        register.setBackground(getResources().getDrawable(R.drawable.bg_login_submit));
                        register.setTextColor(getResources().getColor(R.color.white));
                        register.setEnabled(true);
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
                    .url(requestUrl)
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

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseStr = response.body().string();
                    Log.i("RESPONSE",responseStr);

                    JsonObject jsonObject = new JsonParser().parse(responseStr).getAsJsonObject();
                    JsonElement element = jsonObject.get("success");

                    boolean success = new Gson().fromJson(element,boolean.class);
                    if(success){
                        JsonElement userInfo = jsonObject.get("userInfo");
                        ResponseUserInfo userInfo1 = new Gson().fromJson(userInfo,ResponseUserInfo.class);
                        System.out.println(userInfo1);
                        msg.what = SUCCESS;

                    }else{
                        JsonElement element1 = jsonObject.get("msg");
                        String msgStr = new Gson().fromJson(element1,String.class);
                        System.out.println("msg:"+msgStr);
                        if(msgStr.equals("用户名已存在")){
                            msg.what = DUPLICATE;
                        }else{
                            msg.what = FAIL;
                        }
                    }

                    handler.sendMessage(msg);
                }
            });
        }).start();

    }
}