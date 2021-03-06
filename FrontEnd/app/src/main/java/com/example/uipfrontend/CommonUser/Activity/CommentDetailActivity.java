package com.example.uipfrontend.CommonUser.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.CommonUser.Adapter.ReplyRecyclerViewAdapter;
import com.example.uipfrontend.Entity.PostComment;
import com.example.uipfrontend.Entity.ResponseCommentReply;
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
import com.lzy.widget.CircleImageView;
import com.parfoismeng.expandabletextviewlib.weiget.ExpandableTextView;
import com.sunbinqiang.iconcountview.IconCountView;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

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

import static com.example.uipfrontend.Utils.UserOperationRecord.isNumber;

/**
 * 当某条评论被点击时跳转到这个Activity
 * 跳转时携带该条评论对象
 */

public class CommentDetailActivity extends AppCompatActivity {

    private static final int NETWORK_ERR = -2;
    private static final int SERVER_ERR = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;

    private static final int PAGE_SIZE = 10; // 默认一次请求10条数据
    private int CUR_PAGE_NUM = 1;
    
    private static final String s5 = "insertReply";
    private static final String s6 = "deleteReply";
    private static final String s7 = "increaseLikeInCommentDetail";
    private static final String s8 = "decreaseLikeInCommentDetail";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static boolean isDefaultOrder = true;
    private static boolean isTimeDesc = false;
    
    private static UserInfo user;

    private int commentPos;
    private PostComment comment; // intent传递过来的对象

    private View headView; // 评论作为 RecyclerView 的头部

    private CircleImageView    portrait;     // 评论者头像
    private TextView           commentator;  // 评论者
    private ExpandableTextView content;      // 评论内容
    private TextView           time;         // 发布时间
    private IconCountView      praise;       // 点赞按钮
    private ImageView          report;       // 举报按钮
    private LinearLayout       click_write;  // 点击回复
    
    private RelativeLayout    rv_division;   // headView与item的分隔
    private TextView          replySum;      // 评论数
    private TextView          order_by_time; // 按时间排序
    private TextView          order_by_like; // 按热度排序

    private String            toName;        // 评论的对象
    private String            reference;     // 评论引用的内容

    private LinearLayout      commentBar;    // 底部评论栏
    private BottomSheetDialog commentDialog; // 评论输入框
    private EditText          commentText;   // 评论内容
    private Button            btn_submit;    // 评论提交按钮

    private XRecyclerView            xRecyclerView;
    private ReplyRecyclerViewAdapter adapter;

    private List<PostComment> list; // 评论列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_comment_detail);
        
        commentPos = getIntent().getIntExtra("pos", -1);
        comment = (PostComment) Objects.requireNonNull(getIntent().getExtras())
                .get("comment");
        list = new ArrayList<>();
        
        getReply();
        init();
    }

    private void init() {
        user = (UserInfo) getApplication();
        initToolBar();
        initRecyclerView();
        initHeadView();
        setHeadView();
        initCommentDialog();
        setHeadListener();
        setListListener();
    }

    /**
     * 描述：自定义toolbar
     */
    public void initToolBar() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        setTitle("");
        Toolbar toolbar = findViewById(R.id.toolbar_commentDetail);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    
    /**
     * 描述：分页获取回复
     */
    private void getReply() {
        CUR_PAGE_NUM = 1;

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
                        Log.i("获取回复: ", "失败 - 网络错误");
                        break;
                    case SERVER_ERR:
                        Log.i("获取回复: ", "失败 - 服务器错误");
                        break;
                    case ZERO:
                        Log.i("获取回复: ", "空");
                        break;
                    case SUCCESS:
                        Log.i("获取回复: ", "成功");
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        xRecyclerView.setNoMore(false);
                        break;
                }
                setReplySum();
                dialog.dismiss();
                super.handleMessage(msg);
            }
        };
        
        queryReply(handler, false);
    }

    /**
     * 描述：启动线程获取回复
     * 参数: handler: 消息处理
     * 参数: isLoadMore: 加载处理
     * 返回：void
     */
    private void queryReply(Handler handler, boolean isLoadMore) {

        int orderMode = isDefaultOrder ? 0 : isTimeDesc ? 2 : 1;
        
        new Thread(()->{
            
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.queryReplyById)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE
                            + "&infoId=" + comment.getInfoId() + "&orderMode=" + orderMode)
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取回复: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();

                    ResponseCommentReply responseCommentReply = new Gson().fromJson(resStr, ResponseCommentReply.class);

                    if (isLoadMore) {
                        list.addAll(responseCommentReply.getReplyList());
                        if (CUR_PAGE_NUM * PAGE_SIZE >= responseCommentReply.getTotal()) {
                            msg.what = ZERO;
                        } else {
                            msg.what = SUCCESS;
                        }
                    } else {
                        list = responseCommentReply.getReplyList();
                        if (list == null) {
                            list = new ArrayList<>();
                            msg.what = SERVER_ERR;
                        } else if (list.size() == 0) {
                            msg.what = ZERO;
                        } else {
                            Log.i("获取回复：", list.toString());
                            msg.what = SUCCESS;
                        }
                    }
                    handler.sendMessage(msg);
                }
            });
            
        }).start();
    }

    /**
     * 描述：刷新和加载更多监听
     *      原评论的点赞、举报监听
     *      排序按钮监听
     *      底部评论栏监听
     */
    private void setHeadListener() {

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
                                    Log.i("刷新回复", "成功");
                                    adapter.setList(list);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case ZERO:
                                    Log.i("刷新回复", "0");
                                    break;
                                case SERVER_ERR:
                                    Log.i("刷新回复", "失败 - 服务器错误");
                                    break;
                                case NETWORK_ERR:
                                    Log.i("刷新回复", "失败 - 网络错误");
                                    break;
                            }
                            setReplySum();
                            xRecyclerView.refreshComplete();
                        }
                    };
                    CUR_PAGE_NUM = 1;
                    queryReply(handler, false);

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
                                    Log.i("加载回复", "成功");
                                    adapter.notifyDataSetChanged();
                                    break;
                                case ZERO:
                                    Log.i("加载回复", "0");
                                    xRecyclerView.setNoMore(true);
                                    break;
                                case SERVER_ERR:
                                    Log.i("加载回复", "失败 - 服务器错误");
                                    break;
                                case NETWORK_ERR:
                                    Log.i("加载回复", "失败 - 网络错误");
                                    break;
                            }
                            setReplySum();
                            xRecyclerView.loadMoreComplete();
                        }
                    };
                    CUR_PAGE_NUM++;
                    queryReply(handler, true);

                }, 1500);
            }
        });

        // 评论点赞按钮监听
        praise.setOnStateChangedListener(isSelected -> {
            if (isSelected) {
                comment.setLikeNumber(comment.getLikeNumber() + 1);
                UserRecord record = new UserRecord();
                record.setUserId(user.getUserId());
                record.setToId(comment.getInfoId());
                record.setTag(1);
                record.setType(2);
                UserOperationRecord.insertRecord(this, record, user);
                mySendBroadCast(s7);
            } 
            else {
                comment.setLikeNumber(comment.getLikeNumber() - 1);
                String key = "comment" + comment.getInfoId();
                Long infoId = user.getLikeRecord().get(key);
                UserOperationRecord.deleteRecord(this, infoId);
                user.getLikeRecord().remove(key);
                mySendBroadCast(s8);
            }
        });

        // 评论举报监听
        report.setOnClickListener(view -> {
            if (user.getUserId().equals(comment.getFromId())) {
                Toast.makeText(this, "注意这是您发的哦", Toast.LENGTH_SHORT).show();
            } 
            else {
                String key = "comment" + comment.getInfoId();
                if (user.getReportRecord().containsKey(key)) {
                    Toast.makeText(this, "您已举报过，请等待处理", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("举报")
                            .setMessage("如果该条评论含有不恰当的内容，请点击确定")
                            .setPositiveButton("确定", (dialog1, which) -> {
                                comment.setReportNumber(comment.getReportNumber() + 1);

                                UserRecord record = new UserRecord();
                                record.setUserId(user.getUserId());
                                record.setToId(comment.getInfoId());
                                record.setTag(2);
                                record.setType(2);
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

        // 按点赞数从高到低排序
        order_by_like.setOnClickListener(view -> {
            if(!order_by_time.getText().toString().equals("时间")) {
                order_by_like.setTextColor(getResources().getColor(R.color.blue));
                order_by_time.setTextColor(getResources().getColor(R.color.gray));
                order_by_time.setText("时间");
                isDefaultOrder = true;
                getReply();
            }
        });

        // 按时间排序
        order_by_time.setOnClickListener(view -> {
            order_by_time.setTextColor(getResources().getColor(R.color.blue));
            order_by_like.setTextColor(getResources().getColor(R.color.gray));
            
            if (isDefaultOrder) {
                isDefaultOrder = false;
            } else {
                isTimeDesc = !isTimeDesc;
            }
            getReply();

            if (isTimeDesc) {
                order_by_time.setText("时间↑");
            } else {
                order_by_time.setText("时间↓");
            }
        });

        click_write.setOnClickListener(view -> {
            commentText.setHint("回复给：" + comment.getFromName());
            commentDialog.show();
        });

        commentBar.setOnClickListener(view -> {
            commentText.setHint("回复给：" + comment.getFromName());
            commentDialog.show();
        });

        // 提交评论
        btn_submit.setOnClickListener(view1 -> {
            String commentContent = commentText.getText().toString().trim();
            if (!TextUtils.isEmpty(commentContent)) {
                
                commentDialog.dismiss();
                PostComment postComment = new PostComment();
                postComment.setToId(comment.getInfoId());
                postComment.setFromId(user.getUserId());
                postComment.setFromName(user.getUserName());
                postComment.setPortrait(user.getPortrait());
                postComment.setToName(toName);
                postComment.setReference(reference);
                postComment.setContent(commentContent);
                postComment.setCreated(f.format(new Date()));
                postComment.setLikeNumber(0);
                postComment.setReportNumber(0);
                
                insertReply(postComment);
                
            } else {
                Toast.makeText(CommentDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
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
     * 描述：回复的点赞、举报、删除监听
     */
    private void setListListener() {

        // 弹出评论框
        adapter.setReplyClickListener((view, pos) -> {
            toName = list.get(pos).getFromName();
            reference = list.get(pos).getContent();
            if (reference.length() > 256) {
                reference = reference.substring(0, 256);
            }
            commentText.setHint("回复给：" + toName);
            commentDialog.show();
        });

        adapter.setOnMoreClickListener(((view, pos) -> {
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
                            deleteReply(list.get(pos).getInfoId(), pos, sweetAlertDialog);
                        })
                        .setCancelClickListener(SweetAlertDialog::cancel)
                        .show();
            } 
            else {
                String key = "reply" + list.get(pos).getInfoId();
                if (user.getReportRecord().containsKey(key)) {
                    Toast.makeText(this, "您已举报过，请等待处理", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("举报")
                            .setMessage("如果该条评论含有不恰当的内容，请点击确定")
                            .setPositiveButton("确定", (dialog1, which) -> {
                                list.get(pos).setReportNumber(list.get(pos).getReportNumber() + 1);

                                UserRecord record = new UserRecord();
                                record.setUserId(user.getUserId());
                                record.setToId(list.get(pos).getInfoId());
                                record.setTag(2);
                                record.setType(3);
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
        }));

        adapter.setOnLikeSelectListener((isSelected, pos) -> {
            if (isSelected) {
                list.get(pos).setLikeNumber(list.get(pos).getLikeNumber() + 1);
                UserRecord record = new UserRecord();
                record.setUserId(user.getUserId());
                record.setToId(list.get(pos).getInfoId());
                record.setTag(1);
                record.setType(3);
                UserOperationRecord.insertRecord(this, record, user);
            }
            else {
                list.get(pos).setLikeNumber(list.get(pos).getLikeNumber() - 1);
                String key = "reply" + list.get(pos).getInfoId();
                Long infoId = user.getLikeRecord().get(key);
                UserOperationRecord.deleteRecord(this, infoId);
                user.getLikeRecord().remove(key);
            }
        });
    }

    /**
     * 描述：显示的提示信息-无取消按钮、确定按钮无事件响应
     */
    private void ErrorShow(SweetAlertDialog sDialog, String title, String content, String confirm, int type) {
        sDialog.setTitleText(title)
                .setContentText(content)
                .showCancelButton(false)
                .setConfirmText(confirm)
                .setConfirmClickListener(null)
                .changeAlertType(type);
    }
    
    /**
     * 设置回复数目
     */
    private void setReplySum() {
        if (list == null) {
            replySum.setText("");
        } else {
            if (list.size() == 0) {
                replySum.setText("还没有人回复，快来抢沙发吧。");
            } else {
                replySum.setText(MessageFormat.format("{0}条对话", list.size()));
            }
        }
    }
    
    private void initRecyclerView() {
        adapter = new ReplyRecyclerViewAdapter(this, list);
        adapter.setHasStableIds(true);

        xRecyclerView = findViewById(R.id.rv_cu_forum_comment_reply);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);

        headView = LayoutInflater.from(this).inflate(R.layout.item_forum_post_comment,
                findViewById(android.R.id.content), false);
        xRecyclerView.addHeaderView(headView);
    }
    
    /**
     * 初始化评论框
     */
    private void initCommentDialog() {
        commentBar = findViewById(R.id.ll_cu_forum_comment_detail_comment_bar);
        
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

    private void setHeadView() {
        String isMe = user.getUserId().equals(comment.getFromId()) ? "(我)" : "";

        String uri = comment.getPortrait();
        Glide.with(this).load(Uri.parse(uri))
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(portrait);
        portrait.setBorderWidth(0);

        commentator.setText(String.format("%s%s", comment.getFromName(), isMe));
        content.setContentText(comment.getContent());
        time.setText(comment.getCreated());
        
        praise.setCount(comment.getLikeNumber());
        if (user.getLikeRecord().containsKey("comment" + comment.getInfoId())) {
            praise.setState(true);
        }
        
        rv_division.setVisibility(View.VISIBLE);
        setReplySum();
    }

    private void initHeadView() {
        portrait = headView.findViewById(R.id.imgv_cu_forum_comment_portrait);
        commentator = headView.findViewById(R.id.tv_cu_forum_comment_from);
        content = headView.findViewById(R.id.etv_cu_forum_comment_content);
        time = headView.findViewById(R.id.tv_cu_forum_comment_time);
        praise = headView.findViewById(R.id.praise_view_cu_forum_comment_like);
        report = headView.findViewById(R.id.imgv_cu_forum_comment_more);
        click_write = headView.findViewById(R.id.ll_cu_forum_comment_write);
        rv_division = headView.findViewById(R.id.rv_cu_forum_comment_division);
        replySum = headView.findViewById(R.id.tv_cu_forum_comment_reply_sum);
        order_by_time = headView.findViewById(R.id.tv_cu_forum_comment_order_by_time);
        order_by_like = headView.findViewById(R.id.tv_cu_forum_comment_order_by_like);
    }

    /**
     * 描述：用户发表回复
     * 参数：comment: 回复对象
     * 返回：void
     */
    private void insertReply(PostComment reply) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("插入回复: ", "失败 - 网络错误");
                        Toast.makeText(CommentDetailActivity.this, "请检查网络",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SERVER_ERR:
                        Log.i("插入回复: ", "失败 - 服务器错误");
                        Toast.makeText(CommentDetailActivity.this, "请稍后再试",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        Log.i("插入回复: ", "成功");
                        if (order_by_time.getText().toString().equals("时间↑")) {
                            list.add(0, reply);
                        } else {
                            list.add(reply);
                        }
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        setReplySum();
                        toName = null;
                        commentText.setText("");
                        
                        mySendBroadCast(s5);
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread(()->{
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();
            String json = gson.toJson(reply);

            Log.i("提交表单-回复：", json);

            RequestBody requestBody = FormBody.create(json, MediaType.parse("application/json;charset=utf-8"));

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.insertReply))
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
                    Log.i("插入回复：", resStr);
                    String resId = resStr.substring(1, resStr.length()-1).split(":")[1];
                    if (isNumber(resId)) {
                        reply.setInfoId(Long.valueOf(resId));
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
     * 描述：用户删除回复
     * 参数：id: 回复的infoId
     *      pos: 该条回复在list中的位置
     *      sDialog: 结果显示
     * 返回：void
     */
    private void deleteReply(Long id, int pos, SweetAlertDialog sDialog) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("删除回复: ", "失败 - 网络错误");
                        ErrorShow(sDialog, "删除失败", "请检查网络", "确定",
                                SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SERVER_ERR:
                        Log.i("删除评论: ", "失败 - 服务器错误");
                        ErrorShow(sDialog, "删除失败", "请稍后再试", "确定",
                                SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SUCCESS:
                        Log.i("删除回复: ", "成功");
                        ErrorShow(sDialog, "删除成功", "", "关闭", SweetAlertDialog.SUCCESS_TYPE);
                        list.remove(pos);
                        adapter.notifyDataSetChanged();
                        setReplySum();
                        
                        mySendBroadCast(s6);
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
                            + getResources().getString(R.string.deleteReply))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("删除回复: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("删除回复：", resStr);

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
     * 描述：评论、点赞后发送广播(给PostDetail)
     */
    private void mySendBroadCast(String s) {
        Intent intent = new Intent();
        intent.putExtra("pos", commentPos);
        intent.setAction(s);
        sendBroadcast(intent);
    }

    /**
     * 描述：toolbar按钮监听
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}