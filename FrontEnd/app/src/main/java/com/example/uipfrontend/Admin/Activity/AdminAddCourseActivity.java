package com.example.uipfrontend.Admin.Activity;

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
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Adapter.GridImageAdapter;
import com.example.uipfrontend.Student.FullyGridLayoutManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

public class AdminAddCourseActivity extends AppCompatActivity {

    //上传图片控件
    RecyclerView recyclerView;
    private GridImageAdapter adapter;

//    //弹出对话框 选择照片选择方式
//    NiftyDialogBuilder dialogBuilderSelect;

    private List<LocalMedia> selectList = new ArrayList<>();   //照片存储列表
    //记录用户选择，拍照或从相册选择
    private boolean mode;
    //最大照片数
    private int maxSelectNum = 1;
    //主题风格
    private int themeId;
    //状态栏背景色
    private int statusBarColorPrimaryDark;
    //向上箭头、向下箭头
    private int upResId,downResId;
    private int chooseMode = PictureMimeType.ofAll();

    private OptionsPickerView pvNoLinkOptions;
    private MaterialEditText school;

    private int schoolOption;
    private int instituteOption;

    //发布课程名
    private MaterialEditText title;
    //课程教师
    private MaterialEditText teacher;
    //课程简介
    private FJEditTextCount description;

    private String[] option1 ;
    private String[] option2 ;

    private Course course;

    //上传图片的本地路径
    List<String> selectString = new ArrayList<>();
    //服务器返回的图片url
    String url = "";  //url字符串，逗号分隔，存入数据库中

    private UserInfo userInfo; //记录当前登陆用户的信息

    private boolean isModify = false;

    private static final String s1 = "addCourse";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_course);

        course = new Course();

        userInfo = (UserInfo)getApplication();  //获取登录用户信息

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        initToolBar();
        initRecyclerView();
        initView();
        initData();

        initNoLinkOptionsPicker();
    }
    public void initToolBar() {

        setTitle("");
        Toolbar toolbar = findViewById(R.id.toolbar_admin_recruit_release);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.rv_admin_picture);

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
                        PictureSelector.create(AdminAddCourseActivity.this).externalPictureVideo(media.getPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
                        PictureSelector.create(AdminAddCourseActivity.this).externalPictureAudio(media.getPath());
                        break;
                    default:
                        // 预览图片 可自定长按保存路径
                        PictureSelector.create(AdminAddCourseActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
                        break;
                }
            }
        });


    }
    public void initView(){
        school = findViewById(R.id.et_admin_school);
        title = findViewById(R.id.et_admin_title);
        teacher = findViewById(R.id.et_admin_teacher);
        description = findViewById(R.id.et_admin_description);
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
                    hideSystemKeyboard(AdminAddCourseActivity.this, school);
                    Toast.makeText(this, "请选择发布面向的学校及学科！", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(title.getText().toString().trim())) {
                    title.requestFocus();
                    Toast.makeText(this, "请输入课程标题！", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(teacher.getText().toString().trim())) {
                    teacher.requestFocus();
                    Toast.makeText(this, "请输入教师姓名！", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(description.getText().trim())) {
                    description.requestFocus();
                    Toast.makeText(this, "请输入课程信息的描述！", Toast.LENGTH_SHORT).show();
                }
//                else if(TextUtils.isEmpty(course.getCoursePicture().trim())){
//                    Toast.makeText(this, "请选择课程照片！", Toast.LENGTH_SHORT).show();
//                    course.setCoursePicture("https://image.slidesharecdn.com/itpresentation-160222172239/95/computer-networks-presentation-2-638.jpg?cb=1456162084\n");
//                }
                else {
                    DialogInterface.OnClickListener dialog_OL = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                //确定
                                case -1:
                                    Intent intent = new Intent();

                                    course.setUserId(userInfo.getUserId());
                                    course.setTeacher(teacher.getText().toString());
                                    course.setDescription(description.getText());
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    course.setInfoDate(simpleDateFormat.format(new Date()));
                                    course.setSchoolId(schoolOption+1);
                                    course.setAcademyId(instituteOption+1);
                                    course.setCourseName(title.getText().toString());
                                    //course.setCoursePicture(userInfo.getPortrait());
                                    course.setScore(0f);

                                    if(selectString.size() != 0){
                                        uploadImage();
                                    }else{
                                        if(!isModify){
                                            insertOrModifyCourse(getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.InsertCourse));
                                        }else{
                                            insertOrModifyCourse(getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.UpdateCourse));
                                        }
                                    }

                                    Toast.makeText(AdminAddCourseActivity.this, "课程发布成功", Toast.LENGTH_SHORT).show();

                                    mySendBroadCast(s1);
                                    AdminAddCourseActivity.this.finish();
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
                    AlertDialog dialog = new AlertDialog.Builder(AdminAddCourseActivity.this)
                            .setTitle("提示")
                            .setMessage("是否确认发布该课程？")
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
                hideSystemKeyboard(AdminAddCourseActivity.this, v);
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
            PictureSelector.create(AdminAddCourseActivity.this)
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
            PictureSelector.create(AdminAddCourseActivity.this)
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
            new AlertView.Builder().setContext(AdminAddCourseActivity.this)
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

        //通过sharepreference获取大学选项和学院选项
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        //第二个参数为缺省值，如果不存在该key，返回缺省值
        String strUniversity = sp.getString("university",null);
        String strInstitute = sp.getString("institute",null);

        List<String> universityList = new ArrayList<>();
        List<String> instituteList = new ArrayList<>();
        //将String转为List
        String str1[] = strUniversity.split(",");
        universityList = Arrays.asList(str1);
        String str[] = strInstitute.split(",");
        instituteList = Arrays.asList(str);

        option1 = universityList.toArray(new String[0]);
        option2 = instituteList.toArray(new String[0]);



        Intent intent = getIntent();
        Course newCourse = (Course) intent.getSerializableExtra("course");
        if (newCourse != null) {

            school.setText(option1[newCourse.getSchoolId()-1] + "-" + option2[newCourse.getAcademyId()-1]);
            title.setText(newCourse.getName());
            teacher.setText(newCourse.getTeacher());
            description.setText(newCourse.getDescription());
            course.setCourseID(newCourse.getCourseID());
            isModify = true;
        }

    }
    public void insertOrModifyCourse(String requestUrl){

        ZLoadingDialog dialog = new ZLoadingDialog(AdminAddCourseActivity.this);
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(getResources().getColor(R.color.blue))//颜色
                .setHintText("加载中...")
                .show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //建立client
                final OkHttpClient[] client = {new OkHttpClient()};
                //将传送实体类转为string类型的键值对
                Gson gson = new Gson();
                String json = gson.toJson(course);

                //设置请求体并设置contentType
                RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"),json);
                //请求
                Request request=new Request.Builder()
                        .url(requestUrl)
                        .post(requestBody)
                        .build();
                //新建call联结client和request
                Call call= client[0].newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //请求失败的处理
                        Log.i("RESPONSE:","fail"+e.getMessage());
                        dialog.dismiss();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("RESPONSE:",response.body().string());
                        dialog.dismiss();
                    }

                });
            }
        }).start();


    }

    //先上传图片，成功后返回url再上传表单信息
    public void uploadImage(){

        ZLoadingDialog dialog = new ZLoadingDialog(AdminAddCourseActivity.this);
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(getResources().getColor(R.color.blue))//颜色
                .setHintText("加载中...")
                .show();

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

                        System.out.println("url:"+url);

                        course.setCoursePicture(url);

                        if(!isModify){
                            insertOrModifyCourse(getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.InsertCourse));
                        }else{
                            insertOrModifyCourse(getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.UpdateCourse));
                        }
                    }


                });
            }
        }).start();

    }


    /**
     * 描述：删除评论后发送广播(给AdminCommentFragment)
     */
    private void mySendBroadCast(String s) {
        Intent intent = new Intent();
        intent.putExtra("pos", 1);
        intent.setAction(s);
        sendBroadcast(intent);
    }
}
