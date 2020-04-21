package com.example.uipfrontend.CommonUser.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uipfrontend.R;
import com.rengwuxian.materialedittext.MaterialEditText;

public class CommonUserModifyPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private String password = "123456";

    private MaterialEditText met[] = new MaterialEditText[3];
    private ImageView iv[] = new ImageView[3];
    private boolean selected[] = {false, false, false};

    //密码的正则表达式
    private String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)" +
            "(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu_modify_password);

        initToolBar();
        initView();
        testPassword();
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
            case R.id.iv_cu_oldPassword:
                target = 0;
                break;
            case R.id.iv_cu_newPassword:
                target = 1;
                break;
            case R.id.iv_cu_confirmPassword:
                target = 2;
                break;
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
                if (!password.equals(met[0].getText().toString())) {
                    met[0].setError("原密码不正确");
                    met[0].requestFocus();
                } else if (!met[1].isValid(regex) || met[1].getText().toString() == null) {
                    met[1].setError("新密码不合法");
                    met[1].requestFocus();
                } else if (!met[1].getText().toString().equals(met[2].getText().toString())) {
                    met[2].setError("两次密码不一致");
                    met[2].requestFocus();
                } else {
                    Toast.makeText(CommonUserModifyPasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(met[0].getWindowToken(), 0);
                    finish();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }
}