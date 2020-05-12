package com.example.uipfrontend.CommonUser.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.CommonUser.Adapter.CommentRecyclerViewAdapter;
import com.example.uipfrontend.Entity.ForumPosts;
import com.example.uipfrontend.Entity.PostComment;
import com.example.uipfrontend.Entity.ResponsePostComment;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.Entity.UserRecord;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Utils.UserOperationRecord;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.widget.CircleImageView;
import com.parfoismeng.expandabletextviewlib.weiget.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.sunbinqiang.iconcountview.IconCountView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 当某条帖子被点击时跳转到这个Activity
 * 跳转时携带该条帖子对象
 */
public class PostDetailActivity extends AppCompatActivity {

    private static final int NETWORK_ERR = -2;
    private static final int SERVER_ERR = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;

    private static final int PAGE_SIZE = 10; // 默认一次请求10条数据
    private int CUR_PAGE_NUM = 1;
    
    private static final int myResultCode2 = 2; // 从帖子列表进入时删除帖子的resultCode
    private static final int myResultCode3 = 3; // 从帖子记录进入时删除帖子的resultCode

    private static final String s1 = "insertComment";
    private static final String s2 = "deleteComment";
    private static final String s3 = "increaseLikeInPostDetail";
    private static final String s4 = "decreaseLikeInPostDetail";
    private static final String s5 = "insertReply";
    private static final String s6 = "deleteReply";
    private static final String s7 = "increaseLikeInCommentDetail";
    private static final String s8 = "decreaseLikeInCommentDetail";

    private MyBroadcastReceiver receiver1, receiver2, // 评论增减
                                receiver3, receiver4; // 点赞数增减

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static boolean flag = true;
    
    private static UserInfo user;
    
    private String     beginFrom; // 标识从哪里启动这个Activity
    private int        postPos;   // 该条帖子在list的位置
    private ForumPosts post;      // intent传递过来的对象

    private View headView; // 帖子详情作为 RecyclerView 的头部

    private CircleImageView    detail_portrait; // 发帖人头像
    private TextView           detail_title;    // 帖子标题
    private TextView           detail_poster;   // 发帖人
    private ExpandableTextView detail_content;  // 帖子内容
    private NineGridView       detail_pictures; // 帖子图片
    private TextView           detail_time;     // 发布时间
    private IconCountView      praise;          // 点赞按钮
    private ImageView          detail_report;   // 举报按钮(本人则为删除按钮)
    
    private TextView           commentSum;      // 评论数
    private TextView           order_by_time;   // 按时间排序
    private TextView           order_by_like;   // 按热度排序

    private LinearLayout       commentBar;      // 底部评论栏
    private BottomSheetDialog  commentDialog;   // 评论输入框
    private EditText           commentText;     // 评论内容
    private Button             btn_submit;      // 评论提交按钮

    private XRecyclerView              xRecyclerView;
    private CommentRecyclerViewAdapter adapter;

    private List<PostComment> list; // 评论列表 默认按热度从高到低

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post_detail);
        
        beginFrom = getIntent().getStringExtra("beginFrom");
        postPos = getIntent().getIntExtra("pos", -1);
        post = (ForumPosts) Objects.requireNonNull(getIntent().getExtras())
                .get("detail"); // 获取intent传递来的帖子对象
        list = new ArrayList<>();
        
        getComment();
        init();
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
     * 描述：分页获取评论
     */
    private void getComment() {
        
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("获取评论: ", "失败 - 网络错误");
                        break;
                    case SERVER_ERR:
                        Log.i("获取评论: ", "失败 - 服务器错误");
                        break;
                    case ZERO:
                        Log.i("获取评论: ", "空");
                        break;
                    case SUCCESS:
                        Log.i("获取评论: ", "成功");
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        break;
                }
                setCommentSum();
                super.handleMessage(msg);
            }
        };

        queryComment(handler, false);
    }

    /**
     * 描述：启动线程获取评论
     * 参数: handler: 消息处理
     * 参数: isLoadMore: 加载处理
     * 返回：void
     */
    private void queryComment(Handler handler, boolean isLoadMore) {
        
        new Thread(()-> {
            
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.queryCommentById)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE
                            + "&infoId=" + post.getInfoId())
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取评论: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();

                    ResponsePostComment responsePostComment = new Gson().fromJson(resStr, ResponsePostComment.class);
                    
                    if (isLoadMore) {
                        list.addAll(responsePostComment.getCommentList());
                        if (CUR_PAGE_NUM * PAGE_SIZE >= responsePostComment.getTotal()) {
                            msg.what = ZERO;
                        } else {
                            msg.what = SUCCESS;
                        }
                    } else {
                        list = responsePostComment.getCommentList();
                        if (list == null) {
                            list = new ArrayList<>();
                            msg.what = SERVER_ERR;
                        } else if (list.size() == 0) {
                            msg.what = ZERO;
                        } else {
                            Log.i("获取评论：", list.toString());
                            msg.what = SUCCESS;
                        }
                    }
                    handler.sendMessage(msg);
                }
            });
            
        }).start();
    }

    /**
     * 描述：用户删除帖子
     * 参数：sDialog: 结果显示
     * 返回：void
     */
    private void deletePost(SweetAlertDialog sDialog) {
        
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("删除帖子: ", "失败");
                        sDialog.setTitleText("删除失败")
                                .setContentText("网络出了点问题，请稍候再试")
                                .showCancelButton(false)
                                .setConfirmText("确定")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SERVER_ERR:
                        Log.i("删除帖子: ", "失败");
                        sDialog.setTitleText("删除失败")
                                .setContentText("出了点问题，请稍候再试")
                                .showCancelButton(false)
                                .setConfirmText("确定")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SUCCESS:
                        Log.i("删除帖子: ", "成功");
                        sDialog.setTitleText("删除成功")
                                .setContentText("")
                                .showCancelButton(false)
                                .setConfirmText("关闭")
                                .setConfirmClickListener(sweetAlertDialog -> {
                                    Intent intent = new Intent();
                                    intent.putExtra("pos", postPos);
                                    int resultCode = -1;
                                    if (beginFrom != null) {
                                        switch (beginFrom) {
                                            case "forumList": resultCode = myResultCode2; break;
                                            case "myReleasePost": resultCode = myResultCode3; break;
                                        }
                                    }
                                    setResult(resultCode, intent);
                                    sDialog.dismiss();
                                    finish();
                                })
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        
        new Thread(()->{
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("infoId", String.valueOf(post.getInfoId()));
            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.deletePost))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("删除帖子: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("删除帖子：", resStr);

                    JsonObject jsonObject = new JsonParser().parse(resStr).getAsJsonObject();
                    JsonElement element = jsonObject.get("result");

                    boolean res = new Gson().fromJson(element, boolean.class);
                    msg.what = res ? SUCCESS : SERVER_ERR;
                    handler.sendMessage(msg);
                }
            });
            
        }).start();
    }
    
    private void init() {
        user = (UserInfo) getApplication();
        registerBroadCast();
        initRecyclerView();
        initPostDetail();
        setPostDetail();
        initCommentDialog();
        setListener();
        setListListener();
    }

    private void initRecyclerView() {
        adapter = new CommentRecyclerViewAdapter(this, list);
        adapter.setHasStableIds(true);

        xRecyclerView = findViewById(R.id.rv_cu_forum_post_comment);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);

        headView = LayoutInflater.from(this).inflate(R.layout.item_forum_post_detail,
                findViewById(android.R.id.content), false);
        xRecyclerView.addHeaderView(headView);
    }

    /**
     * 描述：刷新和加载更多监听
     *      帖子的点赞、举报、删除监听
     *      排序按钮监听
     *      底部评论栏监听
     */
    private void setListener() {

        // 刷新和加载更多
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(()->{

                    @SuppressLint("HandlerLeak")
                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case SUCCESS:
                                    Log.i("刷新评论", "成功");
                                    adapter.setList(list);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case ZERO:
                                    Log.i("刷新评论", "0");
                                    break;
                                case SERVER_ERR:
                                    Log.i("刷新评论", "失败 - 服务器错误");
                                    break;
                                case NETWORK_ERR:
                                    Log.i("刷新评论", "失败 - 网络错误");
                                    break;
                                    
                            }
                            setCommentSum();
                            xRecyclerView.refreshComplete();
                        }
                    };
                    CUR_PAGE_NUM = 1;
                    queryComment(handler, false);

                }, 1500);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(()->{

                    @SuppressLint("HandlerLeak")
                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case SUCCESS:
                                    Log.i("加载评论", "成功");
                                    adapter.notifyDataSetChanged();
                                    break;
                                case ZERO:
                                    Log.i("加载评论", "0");
                                    xRecyclerView.setNoMore(true);
                                    break;
                                case SERVER_ERR:
                                    Log.i("加载评论", "失败 - 服务器错误");
                                    break;
                                case NETWORK_ERR:
                                    Log.i("加载评论", "失败 - 网络错误");
                                    break;
                            }
                            setCommentSum();
                            xRecyclerView.loadMoreComplete();
                        }
                    };
                    CUR_PAGE_NUM++;
                    queryComment(handler, true);

                }, 1500);
            }
        });

        // 点赞按钮监听
        praise.setOnStateChangedListener(isSelected -> {
            if (isSelected) {
                post.setLikeNumber(post.getLikeNumber() + 1);
                UserRecord record = new UserRecord();
                record.setUserId(user.getUserId());
                record.setToId(post.getInfoId());
                record.setTag(1);
                UserOperationRecord.insertRecord(this, record, user);
                mySendBroadCast(s3);
            }
            else {
                post.setLikeNumber(post.getLikeNumber() - 1);
                Long infoId = user.getLikeRecord().get(post.getInfoId());
                UserOperationRecord.deleteRecord(this, infoId);
                user.getLikeRecord().remove(post.getInfoId());
                mySendBroadCast(s4);
            }
        });

        // 举报or删除监听
        detail_report.setOnClickListener(view -> {
            if (user.getUserId().equals(post.getUserId())) {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("确定要删除这条帖子吗？")
                        .setConfirmText("确定")
                        .setCancelText("点错了")
                        .showCancelButton(true)
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                            sweetAlertDialog.setTitle("请稍后");
                            sweetAlertDialog.setContentText("");
                            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            sweetAlertDialog.setCancelable(false);
                            sweetAlertDialog.showCancelButton(false);
                            sweetAlertDialog.show();
                            deletePost(sweetAlertDialog);
                        })
                        .setCancelClickListener(SweetAlertDialog::cancel)
                        .show();
            } else {
                if (user.getReportRecord().containsKey(post.getInfoId())) {
                    Toast.makeText(this, "您已举报过，请等待处理", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("如果该条帖子含有不恰当的内容，请点击确定")
                            .setPositiveButton("确定", (dialog1, which) -> {
                                post.setReportNumber(post.getReportNumber() + 1);
                                
                                UserRecord record = new UserRecord();
                                record.setUserId(user.getUserId());
                                record.setToId(post.getInfoId());
                                record.setTag(2);
                                UserOperationRecord.insertRecord(this, record, user);
                                
                                Toast.makeText(this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("取消", null)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
                }
            }
        });

        // todo 按热度从高到低排序
        order_by_like.setOnClickListener(view -> {
            if(!order_by_time.getText().toString().equals("时间")) {
                order_by_like.setTextColor(getResources().getColor(R.color.blue));
                order_by_time.setTextColor(getResources().getColor(R.color.gray));
                order_by_time.setText("时间");
                flag = true;
                sortByLikeNum();
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        });

        // todo 按时间排序
        order_by_time.setOnClickListener(view -> {
            order_by_time.setTextColor(getResources().getColor(R.color.blue));
            order_by_like.setTextColor(getResources().getColor(R.color.gray));
            String text = order_by_time.getText().toString();
            if (flag) { sortByTimeAsc(); flag = false; }
            else { Collections.reverse(list); }

            if (text.contains("↑")) {
                order_by_time.setText("时间↓");
            } else if (text.contains("↓")) {
                order_by_time.setText("时间↑");
            } else {
                order_by_time.setText("时间↓");
            }
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        });

        // 弹出评论框
        commentBar.setOnClickListener(view -> commentDialog.show());

        // 提交评论
        btn_submit.setOnClickListener(view1 -> {
            String commentContent = commentText.getText().toString().trim();
            if (!TextUtils.isEmpty(commentContent)) {
                commentDialog.dismiss();
                PostComment postComment = new PostComment();
                postComment.setFromId(user.getUserId());
                postComment.setToId(post.getInfoId());
                postComment.setContent(commentContent);
                postComment.setPortrait(user.getPortrait());
                postComment.setFromName(user.getUserName());
                postComment.setLikeNumber(0);
                postComment.setReplyNumber(0);
                postComment.setReportNumber(0);
                postComment.setCreated(f.format(new Date()));
                
                insertComment(postComment);
                
            } else {
                Toast.makeText(PostDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            }
        });

        // 改变按钮颜色
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 0) {
                    btn_submit.setBackgroundColor(Color.parseColor("#46b3e6"));
                } else {
                    btn_submit.setBackgroundColor(Color.parseColor("#D5C9C9"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {  }
        });
        
    }

    /**
     * 描述：评论的点赞、举报、删除监听
     */
    private void setListListener() {
        
        adapter.setOnLikeSelectListener((isSelected, pos) -> {
            if (isSelected) {
                list.get(pos).setLikeNumber(list.get(pos).getLikeNumber() + 1);
                UserRecord record = new UserRecord();
                record.setUserId(user.getUserId());
                record.setToId(list.get(pos).getInfoId());
                record.setTag(1);
                UserOperationRecord.insertRecord(this, record, user);
            }
            else {
                list.get(pos).setLikeNumber(list.get(pos).getLikeNumber() - 1);
                Long infoId = user.getLikeRecord().get(list.get(pos).getInfoId());
                UserOperationRecord.deleteRecord(this, infoId);
                user.getLikeRecord().remove(list.get(pos).getInfoId());
            }
            adapter.notifyDataSetChanged();
        });
        
        adapter.setOnMoreClickListener((view, pos) -> {
            if (user.getUserId().equals(list.get(pos).getFromId())) {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("确定要删除这条评论吗？")
                        .setConfirmText("确定")
                        .setCancelText("点错了")
                        .showCancelButton(true)
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                            sweetAlertDialog.setTitle("请稍后");
                            sweetAlertDialog.setContentText("");
                            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            sweetAlertDialog.setCancelable(false);
                            sweetAlertDialog.showCancelButton(false);
                            sweetAlertDialog.show();
                            deleteComment(list.get(pos).getInfoId(), pos, sweetAlertDialog);
                        })
                        .setCancelClickListener(SweetAlertDialog::cancel)
                        .show();
            } else {
                if (user.getReportRecord().containsKey(list.get(pos).getInfoId())) {
                    Toast.makeText(this, "您已举报过，请等待处理", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("如果该条评论含有不恰当的内容，请点击确定")
                            .setPositiveButton("确定", (dialog1, which) -> {
                                list.get(pos).setReportNumber(list.get(pos).getReportNumber() + 1);

                                UserRecord record = new UserRecord();
                                record.setUserId(user.getUserId());
                                record.setToId(list.get(pos).getInfoId());
                                record.setTag(2);
                                UserOperationRecord.insertRecord(this, record, user);
                                
                                Toast.makeText(this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("取消", null)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
                }
            }
        });
    }

    /**
     * 设置评论数目
     */
    private void setCommentSum() {
        if (list == null) {
            commentSum.setText("");
        } else {
            if (list.size() == 0) {
                commentSum.setText("还没有人评论，快来抢沙发吧。");
            } else {
                commentSum.setText(MessageFormat.format("{0}条评论", list.size()));
            }
        }
    }
    
    /**
     * 显示帖子详情
     */
    private void setPostDetail() {
        String isMe = user.getUserId().equals(post.getUserId()) ? "(我)" : "";
        
        Glide.with(this).load(Uri.parse(post.getPortrait()))
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(detail_portrait);
        detail_portrait.setBorderWidth(0);

        detail_title.setText(post.getTitle());
        detail_poster.setText(String.format("%s%s", post.getUserName(), isMe));
        detail_content.setContentText(post.getContent());
        
        String pics = post.getPictures();
        List<ImageInfo> imageList = new ArrayList<>();
        if (pics != null) {
            String[] picUrls = pics.split(",");
            for (String picUrl : picUrls) {
                picUrl = picUrl.replace("localhost", getResources().getString(R.string.myIP));
                picUrl = picUrl.replace("\"", "");
                ImageInfo image = new ImageInfo();
                image.setThumbnailUrl(picUrl);
                image.setBigImageUrl(picUrl);
                imageList.add(image);
            }
        }
        detail_pictures.setAdapter(new NineGridViewClickAdapter(this, imageList));

        detail_time.setText(post.getCreated());

        praise.setCount(post.getLikeNumber());
        if (user.getLikeRecord().containsKey(post.getInfoId())) {
            praise.setState(true);
        }
        
        setCommentSum();
    }

    /**
     * 初始化帖子详情
     */
    private void initPostDetail() {
        NineGridView.setImageLoader(new PicassoImageLoader());
        
        detail_portrait = headView.findViewById(R.id.imgv_cu_forum_detail_portrait);
        detail_title = headView.findViewById(R.id.tv_cu_forum_detail_title);
        detail_poster = headView.findViewById(R.id.tv_cu_forum_detail_poster);
        detail_content = headView.findViewById(R.id.etv_cu_forum_detail_content);
        detail_pictures = headView.findViewById(R.id.nineGrid_cu_forum_detail_pic);
        detail_time = headView.findViewById(R.id.tv_cu_forum_detail_time);
        praise = headView.findViewById(R.id.praise_view_cu_forum_detail_like);
        detail_report = headView.findViewById(R.id.imgv_cu_forum_detail_more);
        commentSum = headView.findViewById(R.id.tv_cu_forum_detail_comment_sum);
        order_by_time = headView.findViewById(R.id.tv_cu_forum_detail_comment_order_by_time);
        order_by_like = headView.findViewById(R.id.tv_cu_forum_detail_comment_order_by_like);
    }

    /**
     * 初始化评论框
     */
    private void initCommentDialog() {
        commentBar = findViewById(R.id.ll_cu_forum_post_detail_comment_bar);

        commentDialog = new BottomSheetDialog(this, R.style.BottomSheetEdit);
        View commentView = LayoutInflater.from(this).inflate(R.layout.dialog_comment_write, null);
        commentText = commentView.findViewById(R.id.et_dialog_comment);
        btn_submit = commentView.findViewById(R.id.btn_dialog_comment);
        commentDialog.setContentView(commentView);

        // 配合 R.style.BottomSheetEdit 解决键盘遮挡问题
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0, 0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());
    }
    
    private void sortByLikeNum() {
        Collections.sort(list, (p1, p2) -> {
            int s1 = p1.getLikeNumber();
            int s2 = p2.getLikeNumber();
            return s1 < s2 ? s1 : (s1 == s2) ? 0 : -1;
        });
    }
    
    private void sortByTimeAsc() {
        Collections.sort(list, (p1, p2) -> {
            try {
                return Objects.requireNonNull(f.parse(p1.getCreated())).compareTo(f.parse(p2.getCreated()));
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }
    
    private void sortByTimeDesc() {
        Collections.sort(list, (p1, p2) -> {
            try {
                return Objects.requireNonNull(f.parse(p2.getCreated())).compareTo(f.parse(p1.getCreated()));
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }
    
    /**
     * 描述：判断字符串是否全部为数字，用于插入评论的返回值判断
     * 参数：插入评论的返回值字符串
     * 返回：true：全部为数字，插入成功，返回的是评论的infoId
     *      false：包含非数字，插入失败，返回的是错误信息
     */
    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 描述：用户发表评论
     * 参数：comment: 评论对象
     * 返回：void
     */
    private void insertComment(PostComment comment) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("插入评论: ", "失败 - 网络错误");
                        Toast.makeText(PostDetailActivity.this, "网络出了点问题，请稍候再试",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SERVER_ERR:
                        Log.i("插入评论: ", "失败 - 服务器错误");
                        Toast.makeText(PostDetailActivity.this, "出了点问题，请稍候再试",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        Log.i("插入评论: ", "成功");

                        if (order_by_time.getText().toString().equals("时间↑")) {
                            list.add(0, comment);
                        } else {
                            list.add(comment);
                        }
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        setCommentSum();
                        commentText.setText("");

                        mySendBroadCast(s1);
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread(()->{
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();
            String json = gson.toJson(comment);

            Log.i("提交表单-评论：", json);

            RequestBody requestBody = FormBody.create(json, MediaType.parse("application/json;charset=utf-8"));

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.insertComment))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("插入评论: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("插入评论：", resStr);
                    String resId = resStr.substring(1, resStr.length()-1).split(":")[1];
                    if (isNumber(resId)) {
                        comment.setInfoId(Long.valueOf(resId));
                        msg.what = SUCCESS;
                    } else {
                        msg.what = SERVER_ERR;
                    }
                    handler.sendMessage(msg);
                }
            });

        }).start();
    }

    /**
     * 描述：用户删除评论
     * 参数：id: 评论的infoId
     *      pos: 该条评论在list中的位置
     *      sDialog: 结果显示
     * 返回：void
     */
    private void deleteComment(Long id, int pos, SweetAlertDialog sDialog) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("删除评论: ", "失败");
                        sDialog.setTitleText("删除失败")
                                .setContentText("网络出了点问题，请稍候再试")
                                .showCancelButton(false)
                                .setConfirmText("确定")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SERVER_ERR:
                        Log.i("删除评论: ", "失败");
                        sDialog.setTitleText("删除失败")
                                .setContentText("出了点问题，请稍候再试")
                                .showCancelButton(false)
                                .setConfirmText("确定")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SUCCESS:
                        Log.i("删除评论: ", "成功");
                        sDialog.setTitleText("删除成功")
                                .setContentText("")
                                .showCancelButton(false)
                                .setConfirmText("关闭")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        list.remove(pos);
                        adapter.notifyDataSetChanged();
                        setCommentSum();
                        
                        mySendBroadCast(s2);
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread(()->{
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();

            FormBody.Builder builder = new FormBody.Builder();
            builder.add("infoId", String.valueOf(id));
            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.deleteComment))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("删除评论: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("删除评论：", resStr);
                    
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
     * 描述：自定义广播
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {

        /**
         * 描述：处理广播
         * 参数：intent: 广播发送的数据
         *      isIncrease: 增or减
         *      where: 1~ReplyNumber; 2~LikeNumber
         * 返回：void
         */
        private void solve(Intent intent, boolean isIncrease, int where) {
            int pos = intent.getIntExtra("pos", -1);
            if (pos != -1) {
                switch (where) {
                    case 1:
                        int n = list.get(pos).getReplyNumber();
                        n = isIncrease ? n+1 : n-1;
                        list.get(pos).setReplyNumber(n);
                        break;
                    case 2: 
                        n = list.get(pos).getLikeNumber();
                        n = isIncrease ? n+1 : n-1;
                        list.get(pos).setLikeNumber(n);
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        }
        
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case s5: solve(intent, true, 1); break;
                case s6: solve(intent, false, 1); break;
                case s7: solve(intent, true, 2); break;
                case s8: solve(intent, false, 2); break;
            }
        }
    }

    /**
     * 描述：注册广播
     */
    private void registerBroadCast() {
        receiver1 = new MyBroadcastReceiver();
        registerReceiver(receiver1, new IntentFilter(s5));

        receiver2 = new MyBroadcastReceiver();
        registerReceiver(receiver2, new IntentFilter(s6));

        receiver3 = new MyBroadcastReceiver();
        registerReceiver(receiver3, new IntentFilter(s7));

        receiver4 = new MyBroadcastReceiver();
        registerReceiver(receiver4, new IntentFilter(s8));
    }

    /**
     * 描述：取消广播注册
     */
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
        unregisterReceiver(receiver3);
        unregisterReceiver(receiver4);
    }
    
    /**
     * 描述：评论、点赞后发送广播(给ForumFragment)
     */
    private void mySendBroadCast(String s) {
        Intent intent = new Intent();
        intent.putExtra("pos", postPos);
        intent.setAction(s);
        sendBroadcast(intent);
    }
}