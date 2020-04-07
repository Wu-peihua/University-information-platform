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

public class AddResActivity extends AppCompatActivity {

    private OptionsPickerView pvNoLinkOptions;
    private MaterialEditText met_type;
    private MaterialEditText met_head;
    private MaterialEditText met_desc;
    private MaterialEditText met_address;
    private CheckBox checkBox;

    private int type1;
    private int type2;

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
        met_head = findViewById(R.id.met_cu_addRes_head);
        met_desc = findViewById(R.id.met_cu_addRes_desc);
        met_address = findViewById(R.id.met_cu_addRes_address);
        checkBox = findViewById(R.id.cb_cu_addRes);
    }

    //不联动的多级选项
    @SuppressLint("ClickableViewAccessibility")
    private void initNoLinkOptionsPicker() {
        String[] option1 = {"全部", "哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学", "艺术学"};
        String[] option2 = {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"};

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
                .setSelectOptions(0, 0)
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
        met_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //利用正则表达式检测地址是否合法
                if (s.toString().length() != 0 && !met_address.validate(regex, "请输入正确的地址"))
                    met_address.setPrimaryColor(Color.parseColor("#E74C31"));
                else
                    met_address.setPrimaryColor(getResources().getColor(R.color.blue));
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
                } else if (TextUtils.isEmpty(met_head.getText().toString().trim())) {
                    met_head.requestFocus();
                    Toast.makeText(this, "请输入资源的标题！", Toast.LENGTH_SHORT).show();
                } else if (!met_address.isValid(regex)) {
                    met_address.requestFocus();
                    Toast.makeText(this, "请输入正确的地址！", Toast.LENGTH_SHORT).show();
                } else {
                    DialogInterface.OnClickListener dialog_OL = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                //确定
                                case -1:
                                    Intent intent = new Intent();
                                    String portraitUri = "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg";
                                    String username = "张咩阿";
                                    String head = met_head.getText().toString().trim();
                                    String desc = met_desc.getText().toString().trim();
                                    String address = met_address.getText().toString();
                                    //时间
                                    String strTime;
                                    long longTime = System.currentTimeMillis();
                                    Date date = new Date(longTime);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
                                    strTime = format.format(date);

                                    if (checkBox.isChecked())
                                        intent.putExtra("resInfo", new ResInfo(type1, type2, "", "匿名者",
                                                head, desc, address, strTime, 0));
                                    else
                                        intent.putExtra("resInfo", new ResInfo(type1, type2, portraitUri, username,
                                                head, desc, address, strTime, 0));

                                    setResult(1, intent);
                                    Toast.makeText(AddResActivity.this, "该资源已发布", Toast.LENGTH_SHORT).show();

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
                            .setMessage("是否确认发布该资源？")
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
}
