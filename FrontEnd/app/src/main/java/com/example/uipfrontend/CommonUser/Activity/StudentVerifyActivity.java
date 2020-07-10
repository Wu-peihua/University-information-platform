package com.example.uipfrontend.CommonUser.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.uipfrontend.Entity.Certification;
import com.example.uipfrontend.Entity.Student;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.LoginAndRegist.LoginActivity;
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
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
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

public class StudentVerifyActivity extends AppCompatActivity {

    private static final int NETWORK_ERR = -2;
    private static final int SERVER_ERR = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;

    private final int maxSelectNum = 3; // 最大照片数
    private final String SELECT_FROM_PHOTO_ALBUM = "SELECT_FROM_PHOTO_ALBUM";
    private final String TAKING_PHOTO            = "TAKING_PHOTO";
    
    private boolean isFirstTime = true; // true: 插入, false: 更新
    private boolean editable = true;    // 编辑框是否可编辑

    private UserInfo user;

    private Certification certification; // 提交的认证信息

    private OptionsPickerView pvNoLinkOptions;
    private String[] option1;     // 高校名列表
    private String[] option2;     // 学院名列表
    private int universityOption; // 选择的高校的序号
    private int instituteOption;  // 选择的学院的序号
    
    private MaterialEditText school;     // 高校+学院
    private MaterialEditText stuNumber;  // 学号
    private MaterialEditText stuName;    // 姓名
    private MaterialEditText stuCard;    // 学生证
    private MaterialEditText state;      // 状态
    private String stateText = "";

    private List<LocalMedia> selectList; // 证明图片
    private List<String> selectString;   // 证明图片的本地地址
    private String url;                  // 证明图片的网络地址

    private RecyclerView recyclerView;
    private GridImageAdapter adapter;

    private int themeId; // 主题风格
    private int statusBarColorPrimaryDark; // 状态栏背景色
    private int upResId, downResId; // 向上箭头、向下箭头
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu_student_verify);
        
        school = findViewById(R.id.met_cu_verify_school);
        stuNumber = findViewById(R.id.met_cu_verify_sno);
        stuName = findViewById(R.id.met_cu_verify_name);
        stuCard = findViewById(R.id.met_cu_verify_card);
        state = findViewById(R.id.met_cu_verify_state);

        getData();
        initNoLinkOptionsPicker();
        initRecyclerView();
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
    
    /**
     * 描述：从本地获取高校列表和对应的学院列表，
     * 再查找用户是否提交过认证申请，
     * 如果有过提交，则使用默认toolbar，
     * 隐藏添加照片，显示提交状态
     * 并将所有编辑框设置为不可编辑
     */
    void getData() {
        user = (UserInfo) getApplication();

        // 通过share_preference获取大学选项和学院选项
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);

        // 第二个参数为缺省值，如果不存在该key，返回缺省值
        String strUniversity = sp.getString("university",null);
        String strInstitute = sp.getString("institute",null);

        option1 = strUniversity != null ? strUniversity.split(",") : new String[0];
        option2 = strInstitute != null ? strInstitute.split(",") : new String[0];

        ZLoadingDialog dialog = new ZLoadingDialog(this);
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE) //设置类型
                .setLoadingColor(getResources().getColor(R.color.blue)) //颜色
                .setHintText("加载中...")
                .setCanceledOnTouchOutside(false)
                .show();
        
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("获取未认证: ", "失败 - 网络错误");
                        Toast.makeText(StudentVerifyActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                        initToolBar();
                        break;
                    case SERVER_ERR:
                        Log.i("获取未认证: ", "失败 - 服务器错误");
                        Toast.makeText(StudentVerifyActivity.this, "请稍后再试", Toast.LENGTH_LONG).show();
                        initToolBar();
                        break;
                    case ZERO:
                        Log.i("获取未认证: ", "未提交");
                        initToolBar();
                        break;
                    case SUCCESS:
                        Log.i("获取未认证: ", "成功");
                        
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(StudentVerifyActivity.this, SweetAlertDialog.NORMAL_TYPE);
                        String content = "";
                        switch (certification.getStuCer()) {
                            case 0: editable = false;
                                content = "请等待管理员审核";
                                stateText = "已提交，请等待管理员审核";
                                break;
                            case 1: isFirstTime = false;
                                content = "提交的信息不充分, 请修改后重新提交";
                                break;
                            case 2: editable = false;
                                content = "已通过审核, 是否跳转到登录页面？";
                                stateText = "已通过，点击跳转到登录页面";
                                state.setOnClickListener(view -> {
                                    Intent intent = new Intent(StudentVerifyActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                });
                                sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
                                    Intent intent = new Intent(StudentVerifyActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    sweetAlertDialog1.dismiss();
                                }).setCancelText("取消").showCancelButton(true);
                                break;
                        }
                        sweetAlertDialog.setTitleText("提示")
                                .setConfirmText("确定")
                                .setContentText(content)
                                .show();

                        if (editable) initToolBar();
                        setData();
                        break;
                }
                dialog.dismiss();
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
                    Log.i("获取未认证: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("获取未认证: ", resStr);

                    JsonObject jsonObject = new JsonParser().parse(resStr).getAsJsonObject();
                    JsonElement element = jsonObject.get("certification");
                    
                    certification = new Gson().fromJson(element, Certification.class);
                    
                    if (certification == null) {
                        msg.what = ZERO;
                    } else { 
                        msg.what = SUCCESS;
                    }
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }
    
    /**
     * 描述：用户已提交认证申请，显示用户提交的信息
     */
    void setData() {
        universityOption = certification.getUniversityId() - 1;
        instituteOption = certification.getInstitudeId() - 1;
        String str = option1[universityOption] + " - " + option2[instituteOption];

        school.setText(str);
        stuNumber.setText(certification.getStuNumber());
        stuName.setText(certification.getStuName());
        
        if (!editable) {
            school.setFocusable(false);
            stuNumber.setFocusable(false);
            stuName.setFocusable(false);
            
            stuCard.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            
            state.setVisibility(View.VISIBLE);
            state.setText(stateText);
            state.setFocusable(false);

            url = certification.getStuCard();
            selectString = Arrays.asList(url.split(","));

            List<ImageInfo> imageList = new ArrayList<>();
            for(String picUrl : selectString) {
                picUrl = picUrl.replace("localhost", getResources().getString(R.string.myIP));
                picUrl = picUrl.replace("\"", "");
                ImageInfo image = new ImageInfo();
                image.setThumbnailUrl(picUrl);
                image.setBigImageUrl(picUrl);
                imageList.add(image);
            }
            NineGridView.setImageLoader(new PicassoImageLoader());
            NineGridView verifyPic = findViewById(R.id.nineGrid_cu_verify_picture);
            verifyPic.setAdapter(new NineGridViewClickAdapter(this, imageList));
            verifyPic.setVisibility(View.VISIBLE);
        }
    }
    
    // 不联动的多级选项
    @SuppressLint("ClickableViewAccessibility")
    private void initNoLinkOptionsPicker() {
        
        pvNoLinkOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            universityOption = options1;
            instituteOption = options2;
            String str = option1[options1] + " - " + option2[options2];
            school.setText(str); 
        })
                .setOptionsSelectChangeListener((options1, options2, options3) -> {})
                .setSelectOptions(0, 0)
                .isRestoreItem(false)
                .setSubmitColor(getResources().getColor(R.color.blue))
                .setCancelColor(getResources().getColor(R.color.blue))
                .setTextColorCenter(getResources().getColor(R.color.blue))
                .setItemVisibleCount(8)
                .setLineSpacingMultiplier((float) 2.3)
                .setOutSideCancelable(false)
                .build();

        // 将数组类型转换成参数所需的ArrayList类型再传参
        pvNoLinkOptions.setNPicker(Arrays.asList(option1), Arrays.asList(option2), null);

        school.setOnTouchListener((v, event) -> {
            hideSystemKeyboard(StudentVerifyActivity.this, v);
            if (pvNoLinkOptions != null)
                pvNoLinkOptions.show();
            return false;
        });
    }
    
    // 显示图片的recyclerView
    private void initRecyclerView() {
        selectList = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_cu_verify_picture);

        themeId = R.style.picture_default_style;
        statusBarColorPrimaryDark = R.color.blue;
        upResId = R.drawable.arrow_up;
        downResId = R.drawable.arrow_down;

        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);

        // 每行显示3张照片
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        // 预览监听
        adapter.setOnItemClickListener((position, v) -> {
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:
                        // 预览视频
                        PictureSelector.create(StudentVerifyActivity.this).externalPictureVideo(media.getPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
                        PictureSelector.create(StudentVerifyActivity.this).externalPictureAudio(media.getPath());
                        break;
                    default:
                        // 预览图片 可自定长按保存路径
                        PictureSelector.create(StudentVerifyActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
                        break;
                }
            }
        });
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

    // 使toolbar带有保存按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit_menu, menu);
        return true;
    }

    // toolbar按钮监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.submit:
                String no = Objects.requireNonNull(stuNumber.getText()).toString().trim();
                String name = Objects.requireNonNull(stuName.getText()).toString().trim();
                
                if(TextUtils.isEmpty(Objects.requireNonNull(school.getText()).toString().trim())) {
                    hideSystemKeyboard(StudentVerifyActivity.this, school);
                    Toast.makeText(this, "请选择学校和学院", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(no)) {
                    stuNumber.requestFocus();
                    Toast.makeText(this, "请输入学号", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(name)) {
                    stuName.requestFocus();
                    Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
                } else if (selectList.size() == 0) {
                    Toast.makeText(this, "请添加证明信息", Toast.LENGTH_SHORT).show();
                } else {
                        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("请稍后");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        
                        Long infoId = null;
                        if (certification != null) infoId = certification.getInfoId();
                        certification = new Certification();
                        certification.setInfoId(infoId);
                        certification.setUserId(user.getUserId());
                        certification.setStuNumber(no);
                        certification.setStuName(name);
                        certification.setUniversityId(universityOption + 1);
                        certification.setInstitudeId(instituteOption + 1);
                        certification.setStuCer(0);

                        submit(pDialog);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 选择图片
    public void selectPhotos(String mode) {
        switch (mode) {
            case SELECT_FROM_PHOTO_ALBUM:
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(StudentVerifyActivity.this)
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
                    // .isChangeStatusBarFontColor(isChangeStatusBarFontColor)// 是否关闭白色状态栏字体颜色
                    .setStatusBarColorPrimaryDark(statusBarColorPrimaryDark)// 状态栏背景色
                    .setUpArrowDrawable(upResId)// 设置标题栏右侧箭头图标
                    .setDownArrowDrawable(downResId)// 设置标题栏右侧箭头图标
                    .isOpenStyleCheckNumMode(false)// 是否开启数字选择模式 类似QQ相册
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    // .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    // .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    // .enableCrop(cb_crop.isChecked())// 是否裁剪
                    // .compress(cb_compress.isChecked())// 是否压缩
                    .synOrAsy(false)// 同步true或异步false 压缩 默认同步
                    // .compressSavePath(getPath())// 压缩图片保存地址
                    // .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    // .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    // .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                    // .isGif(cb_isGif.isChecked())// 是否显示gif图片
                    // .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    // .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                    // .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    // .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    // .isDragFrame(false)// 是否可拖动裁剪框(固定)
                    // .videoMaxSecond(15)
                    // .videoMinSecond(10)
                    // .previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    // .cropCompressQuality(90)// 废弃 改用cutOutQuality()
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    // .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    // .rotateEnabled(true)// 裁剪是否可旋转图片
                    // .scaleEnabled(true)// 裁剪是否可放大缩小图片
                    // .videoQuality()// 视频录制质量 0 or 1
                    // .videoSecond()// 显示多少秒以内的视频or音频也可适用
                    // .recordVideoSecond()// 录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);// 结果回调onActivityResult code
                break;
            case TAKING_PHOTO:
            // 单独拍照
            PictureSelector.create(StudentVerifyActivity.this)
                    .openCamera(PictureMimeType.ofAll())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                    .theme(themeId)// 主题样式设置 具体参考 values/styles
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    // .querySpecifiedFormatSuffix(PictureMimeType.ofPNG())// 查询指定后缀格式资源
                    .selectionMode(PictureConfig.MULTIPLE)// PictureConfig.MULTIPLE : PictureConfig.SINGLE  多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    // .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
                    // .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    // .isChangeStatusBarFontColor(isChangeStatusBarFontColor)// 是否关闭白色状态栏字体颜色
                    .setStatusBarColorPrimaryDark(statusBarColorPrimaryDark)// 状态栏背景色
                    .isOpenStyleCheckNumMode(true)// 是否开启数字选择模式 类似QQ相册
                    .setUpArrowDrawable(upResId)// 设置标题栏右侧箭头图标
                    .setDownArrowDrawable(downResId)// 设置标题栏右侧箭头图标
                    // .enableCrop(cb_crop.isChecked())// 是否裁剪
                    // .compress(cb_compress.isChecked())// 是否压缩
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    // .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    // .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                    // .isGif(cb_isGif.isChecked())// 是否显示gif图片
                    // .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
                    // .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                    // .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    // .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    .previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    // .previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    // .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    // .rotateEnabled()// 裁剪是否可旋转图片
                    // .scaleEnabled()// 裁剪是否可放大缩小图片
                    // .videoQuality()// 视频录制质量 0 or 1
                    // .videoSecond()// 显示多少秒以内的视频or音频也可适用
                    .forResult(PictureConfig.CHOOSE_REQUEST);// 结果回调onActivityResult code
                break;
        }
    }

    // 给上传图片添加点击事件
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener =
            () -> {
                // 弹出对话框 选择拍照或从相册选择
                new AlertView.Builder().setContext(StudentVerifyActivity.this)
                        .setStyle(AlertView.Style.ActionSheet)
                        .setTitle("选择图片")
                        .setCancelText("取消")
                        .setDestructive("拍照", "相册")
                        .setOthers(null)
                        .setOnItemClickListener((object, position) -> {
                            switch (position) {
                                case 0:
                                    selectPhotos(TAKING_PHOTO);
                                    break;
                                case 1:
                                    selectPhotos(SELECT_FROM_PHOTO_ALBUM);
                                    break;
                            }
                        })
                        .build()
                        .show();
            };

    // 返回图片选择结果并显示
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    selectString = new ArrayList<>();
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    // 4.media.getAndroidQToPath();为Android Q版本特有返回的字段，此字段有值就用来做上传使用
                    for (LocalMedia media : selectList) {
                        selectString.add(media.getPath());
                        Log.i(TAG, "压缩---->" + media.getCompressPath());
                        Log.i(TAG, "原图---->" + media.getPath());
                        Log.i(TAG, "裁剪---->" + media.getCutPath());
                        Log.i(TAG, "Android Q 特有Path---->" + media.getAndroidQToPath());
                    }

                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
    
    /**
     * 描述：提交申请
     * 参数：
     * 返回：
     */
    private void submit(SweetAlertDialog sDialog) {

        @SuppressLint("HandlerLeak")
        Handler handler2 = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("上传认证信息: ", "失败 - 网络错误");
                        sDialog.setTitleText("提交失败")
                                .setContentText("网络出了点问题，请稍候再试")
                                .showCancelButton(false)
                                .setConfirmText("确定")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SERVER_ERR:
                        Log.i("上传认证信息: ", "失败 - 服务器错误");
                        sDialog.setTitleText("提交失败")
                                .setContentText("出了点问题，请稍候再试")
                                .showCancelButton(false)
                                .setConfirmText("确定")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SUCCESS:
                        Log.i("上传认证信息: ", "成功");
                        sDialog.setTitleText("提交成功")
                                .setContentText("请耐心等待管理员审核")
                                .showCancelButton(false)
                                .setConfirmText("关闭")
                                .setConfirmClickListener(sweetAlertDialog -> {
                                    sDialog.dismiss();
                                    finish();
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        
        @SuppressLint("HandlerLeak")
        Handler handler1 = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("上传图片: ", "失败 - 网络错误");
                        sDialog.setTitleText("提交失败")
                            .setContentText("网络出了点问题，请稍候再试")
                            .showCancelButton(false)
                            .setConfirmText("确定")
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SERVER_ERR:
                        Log.i("上传图片: ", "失败 - 服务器错误");
                        sDialog.setTitleText("提交失败")
                                .setContentText("出了点问题，请稍候再试")
                                .showCancelButton(false)
                                .setConfirmText("确定")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SUCCESS:
                        Log.i("上传图片: ", "成功");
                        certification.setStuCard(url);
                        insertCertification(handler2);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        
        uploadImage(handler1);
        
    }
    
    /**
     * 描述：上传认证信息
     * 参数：handler消息处理
     * 返回：void
     */
    private void insertCertification(Handler handler) {
        
        new Thread(()->{
            OkHttpClient client = new OkHttpClient();
            Message msg = new Message();

            Gson gson = new Gson();
            certification.setStuCard(certification.getStuCard().replaceAll("\"",""));
            certification.setStuCard(certification.getStuCard().replaceAll(",",""));
            String json = gson.toJson(certification);
            
            String requestUrl = isFirstTime ? getResources().getString(R.string.insertCertification) 
                    : getResources().getString(R.string.updateCertification);

            RequestBody requestBody = FormBody.create(json, MediaType.parse("application/json;charset=utf-8"));
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) + requestUrl)
                    .post(requestBody)
                    .build();
            
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("上传认证: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("上传认证: ", resStr);
                    
                    JsonObject jsonObject = new JsonParser().parse(resStr).getAsJsonObject();
                    JsonElement element = jsonObject.get("result");

                    boolean res = new Gson().fromJson(element, boolean.class);
                    msg.what = res ? SUCCESS : SERVER_ERR;
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }

    /**
     * 描述：先上传图片，返回图片在服务器的url，然后再提交认证申请
     * 参数：handler消息处理
     * 返回：void
     */
    private void uploadImage(Handler handler) {
        
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Message msg = new Message();
            
            // 创建MultipartBody.Builder，用于添加请求的数据
            MultipartBody.Builder builder = new MultipartBody.Builder();
            for (String tempName : selectString) { // 对文件进行遍历
                File file = new File(tempName); // 生成文件
                // 根据文件的后缀名，获得文件类型
                builder.addFormDataPart( // 给Builder添加上传的文件
                        "image",  // 请求的名字
                        file.getName(), // 文件名，服务器端用来解析的
                        RequestBody.create(file, MediaType.parse("multipart/form-data")) // 创建RequestBody，把上传的文件放入
                );
            }

            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.uploadImage))
                            .post(builder.build());
            
            Call call= client.newCall(requestBuilder.build());
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("上传图片: ","失败" + e.getMessage());
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }
                
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("上传图片: ", resStr);
                    // 图片上传成功后再上传表单信息
                    // 解析大学json字符串数组
                    JsonObject jsonObjectUrl = new JsonParser().parse(resStr).getAsJsonObject();
                    JsonArray jsonArrayUrl = jsonObjectUrl.getAsJsonArray("urlList");
                    
                    url = "";
                    // 循环遍历数组
                    for (JsonElement jsonElement : jsonArrayUrl) {
                        url += jsonElement.toString() + ",";
                    }
                    
                    if (url.equals("")) {
                        msg.what = SERVER_ERR;
                    }
                    else {
                        msg.what = SUCCESS;
                    }
                    
                    handler.sendMessage(msg);
                }

            });
        }).start();

    }
}
