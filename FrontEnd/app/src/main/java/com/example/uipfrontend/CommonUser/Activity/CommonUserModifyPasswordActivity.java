package com.example.uipfrontend.CommonUser.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.LoginAndRegist.LoginActivity;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Utils.RSAUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommonUserModifyPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int NETWORK_ERR = -2;
    private static final int SERVER_ERR = -1;
    private static final int SUCCESS = 1;
    
    private MaterialEditText[] met = new MaterialEditText[3];
    private ImageView[] iv = new ImageView[3];
    private boolean[] selected = {false, false, false};
    
    private UserInfo user;
    private String publicKey;

    //密码的正则表达式
    private String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)" +
            "(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$";
    private String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu_modify_password);
        user = (UserInfo) getApplication();
        initToolBar();
        initView();
        //testPassword();
    }

    public void initToolBar() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        setTitle("");
        Toolbar toolbar = findViewById(R.id.cu_toolbar_modifyPassword);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initView() {
        met[0] = findViewById(R.id.met_cu_oldPassword);
        met[1] = findViewById(R.id.met_cu_newPassword);
        met[2] = findViewById(R.id.met_cu_confirmPassword);
        iv[0] = findViewById(R.id.iv_cu_oldPassword);
        iv[1] = findViewById(R.id.iv_cu_newPassword);
        iv[2] = findViewById(R.id.iv_cu_confirmPassword);
        for (int i = 0; i < 3; i++)
            iv[i].setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int target = 0;
        switch (v.getId()) {
            case R.id.iv_cu_oldPassword: target = 0; break;
            case R.id.iv_cu_newPassword: target = 1; break;
            case R.id.iv_cu_confirmPassword: target = 2; break;
        }
        if (selected[target])
            //从密码可见模式变为密码不可见模式
            met[target].setTransformationMethod(PasswordTransformationMethod.getInstance());
        else
            met[target].setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        
        selected[target] = !selected[target];
        iv[target].setSelected(selected[target]);
        met[target].setSelection(met[target].getText().toString().length());
    }

    public void testPassword() {
        met[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && s.toString().length() != 0)
                    met[1].validate(regex, "新密码不合法");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.submit:
                if (user == null) user = (UserInfo) getApplication();
                
                String old = Objects.requireNonNull(met[0].getText()).toString().trim();
                String fresh = Objects.requireNonNull(met[1].getText()).toString().trim();
                String confirm = Objects.requireNonNull(met[2].getText()).toString().trim();
                
                if (old.equals("")) {
                    met[0].setError("请输入原密码");
                    met[0].requestFocus();
                } else if (fresh.equals("")) {
                    met[1].setError("请输入新密码");
                    met[1].requestFocus();
                } else if (confirm.equals("")) {
                    met[2].setError("请确认新密码");
                    met[2].requestFocus();
                } else if (!old.equals(user.getPw())) {
                    met[0].setError("原密码不正确");
                    met[0].requestFocus();
                } else if (!met[1].isValid(reg)) {
                    met[1].setError("请确保新密码为长度6-20的数字字母组合");
                    met[1].requestFocus();
                } else if (!fresh.equals(confirm)) {
                    met[2].setError("两次密码不一致");
                    met[2].requestFocus();
                } else {
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(met[0].getWindowToken(), 0);
                    modifyPassword(fresh);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void modifyPassword(String pw) {
        
        @SuppressLint("HandlerLeak") 
        Handler handler = new Handler(){
            public void handleMessage(@NotNull Message msg) {
                switch (msg.what){
                    case NETWORK_ERR:
                        System.out.println("网络错误 - 获取公钥失败！");
                        Log.i("获取公钥: ", "失败 - 网络错误");
                        Toast.makeText(CommonUserModifyPasswordActivity.this,
                                "网络出了点问题，请稍候再试", Toast.LENGTH_SHORT).show();
                        break;
                    case SERVER_ERR:
                        System.out.println("服务器错误 - 获取公钥失败！");
                        Log.i("获取公钥: ", "失败 - 网络错误");
                        Toast.makeText(CommonUserModifyPasswordActivity.this,
                                "出了点问题，请稍候再试", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        modify(pw);
                        break;
                }
            }
        };
        
        getPublicKey(handler);
    }
    
    private void getPublicKey(Handler handler) {
        
        new Thread(()->{

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.getPublicKey))
                    .get()
                    .build();
            Message msg = new Message();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取公钥: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    String responseStr = Objects.requireNonNull(response.body()).string();
                    Log.i("获取公钥: ", responseStr);

                    JsonObject jsonObject = new JsonParser().parse(responseStr).getAsJsonObject();
                    JsonElement element = jsonObject.get("publicKey");

                    publicKey = new Gson().fromJson(element,String.class);
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }
    
    private void modify(String pw) {
        
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            public void handleMessage(@NotNull Message msg) {
                switch (msg.what){
                    case NETWORK_ERR:
                        System.out.println("网络错误 - 修改密码失败！");
                        Log.i("修改密码: ", "失败 - 网络错误");
                        Toast.makeText(CommonUserModifyPasswordActivity.this,
                                "网络出了点问题，请稍候再试", Toast.LENGTH_SHORT).show();
                        break;
                    case SERVER_ERR:
                        System.out.println("服务器错误 - 修改密码失败！");
                        Log.i("修改密码: ", "失败 - 网络错误");
                        Toast.makeText(CommonUserModifyPasswordActivity.this,
                                "出了点问题，请稍候再试", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        Toast.makeText(CommonUserModifyPasswordActivity.this,
                                "修改密码成功，请重新登录", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CommonUserModifyPasswordActivity.this,
                                LoginActivity.class));
                        finish();
                        break;
                }
            }
        };
        
        new Thread(()->{

            Message msg = new Message();
            OkHttpClient okHttpClient = new OkHttpClient();
            
            RSAPublicKey key = null;
            try {
                key = RSAUtil.getPublicKey(publicKey);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
            //公钥加密后的结果，同一数据每次加密结果不同
            String postPassword = RSAUtil.publicEncrypt(pw, key);

            FormBody.Builder builder = new FormBody.Builder();
            builder.add("userId", String.valueOf(user.getUserId()));
            builder.add("newPw", postPassword);
            RequestBody requestBody = builder.build();
            
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) + 
                            getResources().getString(R.string.modifyPassword))
                    .post(requestBody)
                    .build();
            
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("修改密码: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseStr = Objects.requireNonNull(response.body()).string();
                    Log.i("修改密码: ", responseStr);

                    JsonObject jsonObject = new JsonParser().parse(responseStr).getAsJsonObject();
                    JsonElement element = jsonObject.get("result");
                    boolean result = new Gson().fromJson(element, boolean.class);
                    
                    if (result) msg.what = SUCCESS;
                    else msg.what = SERVER_ERR;
                    
                    handler.sendMessage(msg);
                }
            });
            
        }).start();
    }
}