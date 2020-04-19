package com.example.uipfrontend.CommonUser.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.uipfrontend.Entity.ResInfo;
import com.example.uipfrontend.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AddResActivity extends AppCompatActivity implements View.OnTouchListener {

    private OptionsPickerView pvNoLinkOptions;
    private MaterialEditText  met_type;
    private MaterialEditText  met_title;
    private MaterialEditText  met_desc;
    private MaterialEditText  met_link;
    private CheckBox          checkBox;

    private int     type1;
    private int     type2;
    private String  portraitUri;
    private String  username;
    private String  title;
    private String  desc;
    private String  link;
    private String  time;
    private int     likeNum;
    private boolean isAnonymous;

    String[] option1 = {"全部", "哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学", "艺术学"};
    String[] option2 = {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"};
    //url的正则表达式
    private String regex = "^(?=^.{3,255}$)(http(s)?:\\/\\/)?(www\\.)?[a-zA-Z0-9][-a-zA-Z0-9]{0,62}" +
            "(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:\\d+)*(\\/\\w+\\.\\w+)*([\\?&]\\w+=\\w*)*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_res);

        initToolBar();
        initView();
        initNoLinkOptionsPicker();
        testAddress();
    }

    public void initToolBar() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        setTitle("");
        Toolbar toolbar = findViewById(R.id.toolbar_cu_addRes);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initView() {
        met_type = findViewById(R.id.met_cu_addRes_type);
        met_title = findViewById(R.id.met_cu_addRes_title);
        met_desc = findViewById(R.id.met_cu_addRes_desc);
        met_desc.setOnTouchListener(this);
        met_link = findViewById(R.id.met_cu_addRes_link);
        checkBox = findViewById(R.id.cb_cu_addRes);
        initData();
    }

    public void initData() {
        type1 = type2 = 0;
        portraitUri = "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg";
        username = "张咩阿";
        likeNum = 0;
        Intent intent = getIntent();
        ResInfo acceptData = (ResInfo) intent.getSerializableExtra("resInfo");
        if (acceptData != null) {
            type1 = acceptData.getType1();
            type2 = acceptData.getType2();
            portraitUri = acceptData.getPortraitUri();
            username = acceptData.getUsername();
            title = acceptData.getTitle();
            desc = acceptData.getDescription();
            link = acceptData.getLink();
            time = acceptData.getTime();
            likeNum = acceptData.getLikeNum();
            isAnonymous = acceptData.isAnonymous();

            met_type.setText(option1[type1] + "-" + option2[type2]);
            met_title.setText(title);
            met_desc.setText(desc);
            met_link.setText(link);
            checkBox.setChecked(isAnonymous);
        }
    }

    //不联动的多级选项
    @SuppressLint("ClickableViewAccessibility")
    private void initNoLinkOptionsPicker() {
        pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                type1 = options1;
                type2 = options2;
                String str = option1[options1] + "-" + option2[options2];
                met_type.setText(str);
            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                    }
                })
                .setSelectOptions(type1, type2)
                .isRestoreItem(false)
                .setSubmitColor(getResources().getColor(R.color.blue))
                .setCancelColor(getResources().getColor(R.color.blue))
                .setTextColorCenter(getResources().getColor(R.color.blue))
                .setItemVisibleCount(8)
                .setLineSpacingMultiplier((float) 2.3)
                .setOutSideCancelable(false)
                .build();

        //将数组类型转换成参数所需的Arraylist类型再传参
        pvNoLinkOptions.setNPicker(new ArrayList(Arrays.asList(option1)), new ArrayList(Arrays.asList(option2)), null);

        met_type.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                met_type.requestFocus();
                hideSystemKeyboard(AddResActivity.this, v);
                if (pvNoLinkOptions != null)
                    pvNoLinkOptions.show();
                return false;
            }
        });
    }

    public void testAddress() {
        met_link.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //利用正则表达式检测地址是否合法
                if (s.toString().length() != 0 && !met_link.validate(regex, "请输入正确的地址"))
                    met_link.setPrimaryColor(Color.parseColor("#E74C31"));
                else
                    met_link.setPrimaryColor(getResources().getColor(R.color.blue));
            }
        });
    }

    //隐藏输入法
    public static void hideSystemKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                this.finish();
                return true;
            case R.id.submit:
                if (TextUtils.isEmpty(met_type.getText().toString().trim())) {
                    met_type.requestFocus();
                    hideSystemKeyboard(AddResActivity.this, met_type);
                    Toast.makeText(this, "请选择资源的类型！", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(met_title.getText().toString().trim())) {
                    met_title.requestFocus();
                    Toast.makeText(this, "请输入资源的标题！", Toast.LENGTH_SHORT).show();
                } else if (!met_link.isValid(regex)) {
                    met_link.requestFocus();
                    Toast.makeText(this, "请输入正确的地址！", Toast.LENGTH_SHORT).show();
                } else {
                    DialogInterface.OnClickListener dialog_OL = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                //确定
                                case -1:
                                    Intent intent = new Intent();

                                    title = met_title.getText().toString().trim();
                                    desc = met_desc.getText().toString().trim();
                                    link = met_link.getText().toString();

                                    long longTime = System.currentTimeMillis();
                                    Date date = new Date(longTime);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
                                    time = format.format(date);

                                    isAnonymous = checkBox.isChecked();

                                    intent.putExtra("resInfo", new ResInfo(type1, type2, portraitUri, username,
                                            title, desc, link, time, likeNum, isAnonymous));

                                    setResult(1, intent);
                                    Toast.makeText(AddResActivity.this, "资源发布成功", Toast.LENGTH_SHORT).show();
                                    hideSystemKeyboard(AddResActivity.this, met_type);
                                    AddResActivity.this.finish();
                                    break;
                                //取消
                                case -2:
                                    dialog.dismiss();
                                    break;
                                //其他
                                case -3:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog dialog = new AlertDialog.Builder(AddResActivity.this)
                            .setTitle("提示")
                            .setMessage("是否确定发布该资源？")
                            .setPositiveButton("确定", dialog_OL)
                            .setNegativeButton("取消", dialog_OL)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理，否则将事件交由父视图处理
        if (view.getId() == R.id.met_cu_addRes_desc &&
                (met_desc.canScrollVertically(1) || met_desc.canScrollVertically(-1))) {
            //EditText处理滚动事件
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                //父视图处理滚动事件
                view.getParent().requestDisallowInterceptTouchEvent(false);
        }
        return false;
    }
}
