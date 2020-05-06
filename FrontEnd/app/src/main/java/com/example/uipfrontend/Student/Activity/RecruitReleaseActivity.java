package com.example.uipfrontend.Student.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.uipfrontend.Entity.RecruitInfo;
import com.example.uipfrontend.Entity.University;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Adapter.GridImageAdapter;
import com.example.uipfrontend.Student.FullyGridLayoutManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import fj.edittextcount.lib.FJEditTextCount;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.uipfrontend.CommonUser.Activity.AddResActivity.hideSystemKeyboard;

public class RecruitReleaseActivity extends AppCompatActivity {

    //上传图片控件
    RecyclerView recyclerView;
    private GridImageAdapter adapter;

    private List<LocalMedia> selectList = new ArrayList<>();   //照片存储列表
    //记录用户选择，拍照或从相册选择
    private boolean mode;
    //最大照片数
    private int maxSelectNum = 3;
    //主题风格
    private int themeId;
    //状态栏背景色
    private int statusBarColorPrimaryDark;
    //向上箭头、向下箭头
    private int upResId,downResId;
    private int chooseMode = PictureMimeType.ofAll();

    private OptionsPickerView pvNoLinkOptions;
    //发布组队信息面向的学校
    private MaterialEditText school;
    private int schoolOption;
    private int instituteOption;
    //发布组队信息的标题
    private MaterialEditText title;
    //组队信息发布人联系方式
    private MaterialEditText contact;
    //组队信息描述
    private FJEditTextCount description;

    private String[] option1 ;
    private String[] option2 ;

    //用于发送的组队信息
    private RecruitInfo recruitInfo;

    //上传图片的本地路径
    List<String> selectString = new ArrayList<>();
    //服务器返回的图片url
    String url = "";  //url字符串，逗号分隔，存入数据库中





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_recruit_release);

        recruitInfo = new RecruitInfo();


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        initData();

        initToolBar();
        initRecyclerView();
        initView();

        initNoLinkOptionsPicker();




    }

    public void initToolBar() {

        setTitle("");
        Toolbar toolbar = findViewById(R.id.toolbar_student_recruit_release);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initRecyclerView(){
        recyclerView = findViewById(R.id.rv_recruit_picture);

        //主题风格id设置
        themeId = R.style.picture_default_style;
        //选择是否从相册选择或直接拍照
        statusBarColorPrimaryDark = R.color.blue;
        upResId = R.drawable.arrow_up;
        downResId = R.drawable.arrow_down;

        //照片上传功能
        //每行显示3张照片
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        //最大可上传照片数量
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((position, v) -> {
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:
                        // 预览视频
                        PictureSelector.create(RecruitReleaseActivity.this).externalPictureVideo(media.getPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
                        PictureSelector.create(RecruitReleaseActivity.this).externalPictureAudio(media.getPath());
                        break;
                    default:
                        // 预览图片 可自定长按保存路径
                        PictureSelector.create(RecruitReleaseActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
                        break;
                }
            }
        });


    }

    public void initView(){
        school = findViewById(R.id.et_recruit_school);
        title = findViewById(R.id.et_recruit_title);
        contact = findViewById(R.id.et_recruit_contact);
        description = findViewById(R.id.et_recruit_description);
    }

    //toolbar带有保存按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit_menu, menu);
        return true;
    }

    //toolbar的操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
             * 将actionBar的HomeButtonEnabled设为ture，
             *
             * 将会执行此case
             */
            case android.R.id.home:
                finish();
                break;
            case R.id.submit:
                if (TextUtils.isEmpty(school.getText().toString().trim())) {
                    school.requestFocus();
                    hideSystemKeyboard(RecruitReleaseActivity.this, school);
                    Toast.makeText(this, "请选择发布面向的学校及学科！", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(title.getText().toString().trim())) {
                    title.requestFocus();
                    Toast.makeText(this, "请输入组队信息的标题！", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(contact.getText().toString().trim())) {
                    contact.requestFocus();
                    Toast.makeText(this, "请输入您的联系方式！", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(description.getText().trim())) {
                    description.requestFocus();
                    Toast.makeText(this, "请输入组队信息的描述！", Toast.LENGTH_SHORT).show();
                }
                else {
                    DialogInterface.OnClickListener dialog_OL = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                //确定
                                case -1:
                                    Intent intent = new Intent();

//                                    setResult(1, intent);

                                    recruitInfo.setUserId(Long.valueOf(1)); //默认设置为1
                                    recruitInfo.setContact(contact.getText().toString());
                                    recruitInfo.setContent(description.getText());
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    recruitInfo.setInfoDate(simpleDateFormat.format(new Date()));
                                    recruitInfo.setUniversityId(schoolOption+1);
                                    recruitInfo.setInstituteId(instituteOption+1);
                                    recruitInfo.setTitle(title.getText().toString());
                                    recruitInfo.setPortrait("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587898699610&di=c7b2fc839b41a4eb285279b781112427&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F09%2F20190109072726_aNNZd.thumb.700_0.jpeg");

                                    uploadImage();
//                                    insertRecruitInfo();

                                    Toast.makeText(RecruitReleaseActivity.this, "组队信息发布成功", Toast.LENGTH_SHORT).show();

                                    RecruitReleaseActivity.this.finish();
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
                    AlertDialog dialog = new AlertDialog.Builder(RecruitReleaseActivity.this)
                            .setTitle("提示")
                            .setMessage("是否确认发布该组队信息？")
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


    //不联动的多级选项
    @SuppressLint("ClickableViewAccessibility")
    private void initNoLinkOptionsPicker() {


        pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                schoolOption = options1;
                instituteOption = options2;
                String str = option1[options1] + "-" + option2[options2];
                school.setText(str);
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
        pvNoLinkOptions.setNPicker(new ArrayList(Arrays.asList(option1)), new ArrayList(Arrays.asList(option2)),null);

        school.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                school.requestFocus();
                hideSystemKeyboard(RecruitReleaseActivity.this, v);
                if (pvNoLinkOptions != null)
                    pvNoLinkOptions.show();
                return false;
            }
        });
    }


    //选择图片
    public void selectPhotos(){
        if (mode) {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(RecruitReleaseActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .cameraFileName("")// 使用相机时保存至本地的文件名称,注意这个只在拍照时可以使用，选图时不要用
                    .selectionMode(PictureConfig.MULTIPLE)// PictureConfig.MULTIPLE : PictureConfig.SINGLE 多选 or 单选
                    .isSingleDirectReturn(false)// 单选模式下是否直接返回
                    .previewImage(true)// 是否可预览图片
                    .isCamera(true)// 是否显示拍照按钮
//                        .isChangeStatusBarFontColor(isChangeStatusBarFontColor)// 是否关闭白色状态栏字体颜色
                    .setStatusBarColorPrimaryDark(statusBarColorPrimaryDark)// 状态栏背景色
                    .setUpArrowDrawable(upResId)// 设置标题栏右侧箭头图标
                    .setDownArrowDrawable(downResId)// 设置标题栏右侧箭头图标
                    .isOpenStyleCheckNumMode(false)// 是否开启数字选择模式 类似QQ相册
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
//                        .enableCrop(cb_crop.isChecked())// 是否裁剪
//                        .compress(cb_compress.isChecked())// 是否压缩
                    .synOrAsy(false)//同步true或异步false 压缩 默认同步
                    //.compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
//                        .isGif(cb_isGif.isChecked())// 是否显示gif图片
//                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                        .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(true)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 废弃 改用cutOutQuality()
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled(true) // 裁剪是否可旋转图片
                    //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        } else {
            // 单独拍照
            PictureSelector.create(RecruitReleaseActivity.this)
                    .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                    .theme(themeId)// 主题样式设置 具体参考 values/styles
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    //.querySpecifiedFormatSuffix(PictureMimeType.ofPNG())// 查询指定后缀格式资源
                    .selectionMode(PictureConfig.MULTIPLE)// PictureConfig.MULTIPLE : PictureConfig.SINGLE  多选 or 单选
                    .previewImage(true)// 是否可预览图片
//                        .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
//                        .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
//                        .isChangeStatusBarFontColor(isChangeStatusBarFontColor)// 是否关闭白色状态栏字体颜色
                    .setStatusBarColorPrimaryDark(statusBarColorPrimaryDark)// 状态栏背景色
                    .isOpenStyleCheckNumMode(true)// 是否开启数字选择模式 类似QQ相册
                    .setUpArrowDrawable(upResId)// 设置标题栏右侧箭头图标
                    .setDownArrowDrawable(downResId)// 设置标题栏右侧箭头图标
//                        .enableCrop(cb_crop.isChecked())// 是否裁剪
//                        .compress(cb_compress.isChecked())// 是否压缩
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
//                        .isGif(cb_isGif.isChecked())// 是否显示gif图片
//                        .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
//                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                        .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(true)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()////显示多少秒以内的视频or音频也可适用
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    }

    //给上传图片添加点击事件
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            //弹出对话框 选择拍照或从相册选择
            new AlertView.Builder().setContext(RecruitReleaseActivity.this)
                    .setStyle(AlertView.Style.ActionSheet)
                    .setTitle("上传照片")
                    .setCancelText("取消")
                    .setDestructive("拍照", "从相册中选择")
                    .setOthers(null)
                    .setOnItemClickListener((object, position) -> {
                        switch (position) {
                            case 0:
                                mode = false;
                                selectPhotos();
                                break;
                            case 1:
                                mode = true;
                                selectPhotos();
                                break;
                        }
                    })
                    .build()
                    .show();

        }
    };

    //返回结果并显示
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);

                    for (LocalMedia media : selectList) {
                        selectString.add(media.getPath());
//                        Log.i(TAG, "压缩---->" + media.getCompressPath());
//                        Log.i(TAG, "原图---->" + media.getPath());
//                        Log.i(TAG, "裁剪---->" + media.getCutPath());
//                        Log.i(TAG, "Android Q 特有Path---->" + media.getAndroidQToPath());
                    }

                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    @SuppressLint("SetTextI18n")
    public void initData() {

        Intent intent = getIntent();
        RecruitInfo recruitInfo = (RecruitInfo) intent.getSerializableExtra("recruitInfo");
        if (recruitInfo != null) {

            school.setText(option1[recruitInfo.getUniversityId()] + "-" + option2[recruitInfo.getInstituteId()]);
            title.setText(recruitInfo.getTitle());
            contact.setText(recruitInfo.getContact());
            description.setText(recruitInfo.getContent());

        }

        //通过sharepreference获取大学选项和学院选项
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        //第二个参数为缺省值，如果不存在该key，返回缺省值
        Set<String> setUniversity = sp.getStringSet("university",null);
        Set<String> setInstitute = sp.getStringSet("institute",null);

        assert setUniversity != null;
        List<String> universityList = new ArrayList<>(setUniversity);
        assert setInstitute != null;
        List<String> instituteList = new ArrayList<>(setInstitute);

        option1 = universityList.toArray(new String[0]);
        option2 = instituteList.toArray(new String[0]);

        System.out.println("InstituteList:"+instituteList.toString());


    }


    public void insertRecruitInfo(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //建立client
                final OkHttpClient[] client = {new OkHttpClient()};
                //将传送实体类转为string类型的键值对
                Gson gson = new Gson();
                String json = gson.toJson(recruitInfo);

                //设置请求体并设置contentType
                RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"),json);
                //请求
                Request request=new Request.Builder()
                        .url(getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.insertRecruit))
                        .post(requestBody)
                        .build();
                //新建call联结client和request
                Call call= client[0].newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //请求失败的处理
                        Log.i("RESPONSE:","fail"+e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("RESPONSE:",response.body().string());
                    }

                });
            }
        }).start();


    }

    public void uploadImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //建立client
                final OkHttpClient[] client = {new OkHttpClient()};

                //创建MultipartBody.Builder，用于添加请求的数据
                MultipartBody.Builder builder = new MultipartBody.Builder();
                for (String tempName : selectString) { //对文件进行遍历
                    File file = new File(tempName); //生成文件
                    //根据文件的后缀名，获得文件类型
                    builder.addFormDataPart( //给Builder添加上传的文件
                            "image",  //请求的名字
                            file.getName(), //文件的文字，服务器端用来解析的
                            RequestBody.create(MediaType.parse("multipart/form-data"), file) //创建RequestBody，把上传的文件放入
                    );
                }

                Request.Builder requestBuilder = new Request.Builder();
                requestBuilder.url(getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.uploadImage))
                        .post(builder.build());


                //新建call联结client和request
                Call call= client[0].newCall(requestBuilder.build());
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //请求失败的处理
                        Log.i("RESPONSE:","fail"+e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody = response.body().string();
                        Log.i("RESPONSE:",responseBody);
                        //图片上传成功后再上传表单信息
                        //解析大学json字符串数组
                        JsonObject jsonObjectUrl = new JsonParser().parse(responseBody).getAsJsonObject();
                        JsonArray jsonArrayUrl = jsonObjectUrl.getAsJsonArray("urlList");

                        //循环遍历数组
                        for (JsonElement jsonElement : jsonArrayUrl) {
                            url += jsonElement.toString() + ",";
                        }
                        recruitInfo.setPictures(url);

                        insertRecruitInfo();
                    }

                });
            }
        }).start();

    }

}
