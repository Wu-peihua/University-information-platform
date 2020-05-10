package com.example.uipfrontend.CommonUser.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.example.uipfrontend.Entity.ForumPosts;
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

import net.steamcrafted.loadtoast.LoadToast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

/**
 * 1、点击搜索框旁边的+号跳转到这个Activity
 * 跳转时携带用户ID；
 * 2、发布记录里点击详情跳转到这个Activity
 * 跳转时携带帖子对象
 */
public class WritePostActivity extends AppCompatActivity {

    private static final int NETWORK_ERR = -2;
    private static final int SERVER_ERR = -1;
    private static final int SUCCESS = 1;
    private static final int FAILURE = 0;

    private static final int myResultCode1 = 1; // 新建帖子
    private static final int myResultCode4 = 4; // 修改帖子

    private final int     maxSelectNum            = 9; //最大照片数
    private final Boolean SELECT_FROM_PHOTO_ALBUM = true;
    private final Boolean TAKING_PHOTO            = false;
    
    private int        postPos;
    private ForumPosts post;  // 修改：intent 传递过来帖子对象
    private Long     userId;  // 新建：intent 传递过来的用户id
    
    private static boolean isUpdate = false;
    private static boolean editFlag = false;
    
    private EditText et_title;           // 帖子标题
    private EditText et_content;         // 帖子内容
    private List<LocalMedia> selectList; // 帖子配图
    private List<String> selectString;   // 帖子配图的本地地址
    private String url;                  // 帖子配图的网络地址

    private GridImageAdapter adapter;

    private boolean mode; //记录用户选择，拍照或从相册选择

    private int themeId; //主题风格

    private int statusBarColorPrimaryDark; //状态栏背景色

    private int upResId, downResId; //向上箭头、向下箭头

    private int chooseMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_write_post);
        
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        
        init();
    }
    
    private void init() {
        et_title = findViewById(R.id.edt_cu_forum_write_title);
        et_content = findViewById(R.id.edt_cu_forum_write_content);

        getData();
        initToolBar();
        initRecyclerView();
        setTextChangedWatcher();
    }

    /**
     * 描述：获取intent传递的数据
     */
    private void getData() {
        Intent intent = getIntent();
        if(intent != null) {
            userId = intent.getLongExtra("userId", -1);
            if(userId == -1) {
                isUpdate = true;
                postPos = intent.getIntExtra("pos", -1);
                post = (ForumPosts) Objects.requireNonNull(intent.getExtras()).get("post");
                if (post != null) {
                    et_title.setText(post.getTitle());
                    et_content.setText(post.getContent());
                }
            }
        }
    }

    /**
     * 描述：如果有图片先提交图片，成功后再提交帖子
     * 参数：显示结果的dialog
     * 返回：void
     */
    private void submit(SweetAlertDialog sDialog) {
        
        if (selectList.size() > 0) {
            @SuppressLint("HandlerLeak")
            Handler handler1 = new Handler() {
                public void handleMessage(Message message) {
                    switch (message.what) {
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
                            post.setPictures(url);
                            submitPost(sDialog);
                            break;
                    }
                }
            };
            uploadImage(handler1);
            
        } else  {
            submitPost(sDialog);
        }

    }
    
    /**
     * 描述：提交帖子
     * 参数：显示结果的dialog
     * 返回：void
     */
    private void submitPost(SweetAlertDialog sDialog) {

        new Handler().postDelayed(()->{

            @SuppressLint("HandlerLeak")
            Handler handler2 = new Handler() {
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case SUCCESS:
                            String tipText = isUpdate ? "修改成功" : "发布成功";

                            sDialog.setTitleText(tipText)
                                    .setContentText("")
                                    .showCancelButton(false)
                                    .setConfirmText("关闭")
                                    .setConfirmClickListener(sweetAlertDialog -> {
                                        if (isUpdate) {
                                            Intent intent = new Intent();
                                            intent.putExtra("newPost", post);
                                            intent.putExtra("pos", postPos);
                                            setResult(myResultCode4, intent);
                                        } else {
                                            setResult(myResultCode1);
                                        }
                                        sDialog.dismiss();
                                        finish();
                                    })
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            editFlag = false;
                            break;
                        case NETWORK_ERR:
                            sDialog.setTitleText("提交失败")
                                    .setContentText("出了点问题，请稍候再试")
                                    .showCancelButton(false)
                                    .setConfirmText("确定")
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            break;
                    }
                }
            };

            new Thread(()->{
                Message msg = new Message();
                OkHttpClient okHttpClient = new OkHttpClient();
                Gson gson = new Gson();
                String json = gson.toJson(post);

                Log.i("提交表单-帖子：", json);

                RequestBody requestBody = FormBody.create(json, MediaType.parse("application/json;charset=utf-8"));

                String requestUrl = isUpdate ? getResources().getString(R.string.updatePost) :
                        getResources().getString(R.string.insertPost);

                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.serverBasePath) + requestUrl)
                        .post(requestBody)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("提交失败:", Objects.requireNonNull(e.getMessage()));
                        msg.what = NETWORK_ERR;
                        handler2.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = Objects.requireNonNull(response.body()).string();
                        Log.i("提交成功:", resStr);
                        // TODO: 解析返回结果
                        msg.what = SUCCESS;
                        handler2.sendMessage(msg);
                    }
                });

            }).start();

        }, 1500);
        
    }

    // toolbar按钮监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (editFlag) {
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("真的要离开吗？")
                            .setContentText("离开后内容将丢失！")
                            .setConfirmText("确定")
                            .setCancelText("点错了")
                            .showCancelButton(true)
                            .setConfirmClickListener(sweetAlertDialog -> finish())
                            .setCancelClickListener(SweetAlertDialog::cancel)
                            .show();
                } else { finish(); }
                break;
            case R.id.submit:
                String title = et_title.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                if(TextUtils.isEmpty(title)) {
                    Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(content)) {
                    Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("请稍后");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    if (!isUpdate) {
                        post = new ForumPosts();
                        post.setUserId(userId);
                    }
                    post.setTitle(title);
                    post.setContent(content);
                    submit(pDialog);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 使toolbar带有保存按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit_menu, menu);
        return true;
    }

    private void initToolBar() {

        setTitle("");

        Toolbar toolbar = findViewById(R.id.toolbar_cu_forum_write_post);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // 提交的动画
    private void initSubmitAnim() {

        // 获取屏幕高度
        Rect outSize = new Rect();
        getWindowManager().getDefaultDisplay().getRectSize(outSize);
        int height = outSize.bottom;

        // 提交动画
        LoadToast lt = new LoadToast(this);
        lt.setTranslationY(height / 2);
        lt.setText("正在提交");
        lt.setTextColor(getResources().getColor(R.color.gray));
        lt.setBackgroundColor(getResources().getColor(R.color.white));
        lt.setProgressColor(getResources().getColor(R.color.colorPrimary));
        lt.setBorderWidthDp(1);
        lt.setBorderColor(getResources().getColor(R.color.darkGray));
    }

    // 文本改动监听
    private void setTextChangedWatcher() {
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) editFlag = true;
            }
        });

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) editFlag = true;
            }
        });
    }

    // 照片显示的recyclerview
    private void initRecyclerView() {
        selectList = new ArrayList<>();
        
        RecyclerView recyclerView = findViewById(R.id.rv_cu_forum_write_picture);

        themeId = R.style.picture_default_style;
        statusBarColorPrimaryDark = R.color.blue;
        upResId = R.drawable.arrow_up;
        downResId = R.drawable.arrow_down;
        chooseMode = PictureMimeType.ofAll();

        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);

        //每行显示3张照片
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((position, v) -> {
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:
                        // 预览视频
                        PictureSelector.create(WritePostActivity.this).externalPictureVideo(media.getPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
                        PictureSelector.create(WritePostActivity.this).externalPictureAudio(media.getPath());
                        break;
                    default:
                        // 预览图片 可自定长按保存路径
                        PictureSelector.create(WritePostActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
                        break;
                }
            }
        });
    }

    // 选择图片
    public void selectPhotos() {
        if (mode) {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(WritePostActivity.this)
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
                    .synOrAsy(false)// 同步true或异步false 压缩 默认同步
                    //.compressSavePath(getPath())// 压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
//                        .isGif(cb_isGif.isChecked())// 是否显示gif图片
//                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                        .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 废弃 改用cutOutQuality()
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled(true)// 裁剪是否可旋转图片
                    //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()// 显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()// 录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);// 结果回调onActivityResult code
        } else {
            // 单独拍照
            PictureSelector.create(WritePostActivity.this)
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
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    .previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled()// 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()// 显示多少秒以内的视频or音频也可适用
                    .forResult(PictureConfig.CHOOSE_REQUEST);// 结果回调onActivityResult code
        }
    }

    // 给上传图片添加点击事件
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener =
            new GridImageAdapter.onAddPicClickListener() {
                @Override
                public void onAddPicClick() {
                    //弹出对话框 选择拍照或从相册选择
                    new AlertView.Builder().setContext(WritePostActivity.this)
                            .setStyle(AlertView.Style.ActionSheet)
                            .setTitle("选择图片")
                            .setCancelText("取消")
                            .setDestructive("拍照", "相册")
                            .setOthers(null)
                            .setOnItemClickListener((object, position) -> {
                                switch (position) {
                                    case 0:
                                        mode = TAKING_PHOTO;
                                        selectPhotos();
                                        break;
                                    case 1:
                                        mode = SELECT_FROM_PHOTO_ALBUM;
                                        selectPhotos();
                                        break;
                                }
                            })
                            .build()
                            .show();
                }
            };

    // 返回结果并显示
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
     * 描述：先上传图片，返回图片在服务器的url，然后再提交帖子
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
