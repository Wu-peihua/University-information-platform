package com.example.uipfrontend.CommonUser.Activity;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.CommonUser.Adapter.ReplyRecyclerViewAdapter;
import com.example.uipfrontend.Entity.PostComment;
import com.example.uipfrontend.Entity.ResponseCommentReply;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.widget.CircleImageView;
import com.parfoismeng.expandabletextviewlib.weiget.ExpandableTextView;
import com.sunbinqiang.iconcountview.IconCountView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 * 当某条评论被点击时跳转到这个Activity
 * 跳转时携带该条评论对象
 */

public class CommentDetailActivity extends AppCompatActivity {

    private static final int FAILURE = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;

    private static final int PAGE_SIZE = 10; // 默认一次请求10条数据
    private int CUR_PAGE_NUM = 1;

    private static final DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static boolean flag = true;
    
    private UserInfo user;

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
    private List<PostComment> list_order_by_asc; // ↓：按时间从远到近
    private List<PostComment> list_order_by_des; // ↑：按时间从近到远

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_comment_detail);
        
        comment = (PostComment) Objects.requireNonNull(getIntent().getExtras())
                .get("comment");
        list = new ArrayList<>();
        
        getReply();
        init();
    }

    private void init() {
        user = (UserInfo) getApplication();
        initRecyclerView();
        initHeadView();
        setHeadView();
        initCommentDialog();
        setHeadListener();
        setListListener();
    }
    
    /**
     * 描述：分页获取回复
     */
    private void getReply() {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAILURE:
                        Log.i("获取回复: ", "失败");
                        break;
                    case ZERO:
                        Log.i("获取回复: ", "空");
                        setReplySum();
                        break;
                    case SUCCESS:
                        Log.i("获取回复: ", "成功");
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        setReplySum();
                        break;
                }
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
        
        new Thread(()->{
            
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.queryReplyById)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE
                            + "&infoId=" + comment.getInfoId())
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取回复: ", e.getMessage());
                    msg.what = FAILURE;
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
                            msg.what = FAILURE;
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
    
    private void insertReply(PostComment reply) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAILURE:
                        Log.i("插入回复: ", "失败");
                        Toast.makeText(CommentDetailActivity.this, "网络出了点问题，请稍候再试",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        Log.i("插入回复: ", "成功");
                        if (order_by_time.getText().toString().equals("时间↑")) {
                            list.add(0, reply);
                        } else {
                            list.add(reply);
                        }
                        adapter.notifyDataSetChanged();
                        setReplySum();
                        toName = null;
                        commentText.setText("");
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

            RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"), json);

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.insertReply))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("插入评论: ", e.getMessage());
                    msg.what = FAILURE;
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
                        msg.what = FAILURE;
                    }
                    handler.sendMessage(msg);
                }
            });

        }).start();
    }
    
    private void setHeadListener() {

        // 按热度从高到低排序
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

        // 按时间排序
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
                                    setReplySum();
                                    break;
                                case FAILURE:
                                    Log.i("刷新回复", "失败");
                                    break;
                                case ZERO:
                                    Log.i("刷新回复", "0");
                                    setReplySum();
                                    break;
                            }
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
                            xRecyclerView.loadMoreComplete();
                            switch (msg.what) {
                                case SUCCESS:
                                    Log.i("加载回复", "成功");
                                    adapter.notifyDataSetChanged();
                                    setReplySum();
                                    break;
                                case FAILURE:
                                    Log.i("加载回复", "失败");
                                    break;
                                case ZERO:
                                    Log.i("加载回复", "0");
                                    setReplySum();
                                    xRecyclerView.setNoMore(true);
                                    break;
                            }
                        }
                    };
                    CUR_PAGE_NUM++;
                    queryReply(handler, true);

                }, 1500);
            }
        });

        // 点赞按钮监听
        praise.setOnStateChangedListener(new IconCountView.OnSelectedStateChangedListener() {
            @Override
            public void select(boolean isSelected) {
                if (isSelected) {
                    comment.setLikeNumber(comment.getLikeNumber() + 1);
                } else {
                    comment.setLikeNumber(comment.getLikeNumber() - 1);
                }
            }
        });

        // 举报监听
        report.setOnClickListener(view -> {
            if (user.getUserId().equals(comment.getFromId())) {
                report.setVisibility(View.GONE);
            } else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("如果该条评论含有不恰当的内容，请点击确定")
                        .setPositiveButton("确定", (dialog1, which) -> {
                            Toast.makeText(this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
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
                UserInfo user = (UserInfo) getApplication();
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 0) {
                    btn_submit.setBackgroundColor(Color.parseColor("#46b3e6"));
                } else {
                    btn_submit.setBackgroundColor(Color.parseColor("#D5C9C9"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    
    private void setListListener() {

        // 弹出评论框
        adapter.setReplyClickListener((view, pos) -> {
            toName = list.get(pos).getFromName();
            reference = list.get(pos).getContent();
            commentText.setHint("回复给：" + toName);
            commentDialog.show();
        });

        adapter.setOnMoreClickListener(((view, pos) -> {
            if (user.getUserId().equals(list.get(pos).getFromId())) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定要删除吗")
                        .setPositiveButton("确定", (dialog1, which) -> {
                            deleteReply(list.get(pos).getInfoId(), pos);
                        })
                        .setNegativeButton("点错了", null)
                        .setCancelable(false)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
            } else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("如果该条评论含有不恰当的内容，请点击确定")
                        .setPositiveButton("确定", (dialog1, which) -> {
                            Toast.makeText(this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
            }
        }));
    }
    
    private void setReplySum() {
        if(list.size() == 0) {
            replySum.setText("还没有人回复，快来抢沙发吧。");
        } else {
            replySum.setText(list.size() + "条对话");
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

        // String uri = "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg";
        String uri = comment.getPortrait();
        Glide.with(this).load(Uri.parse(uri))
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(portrait);
        portrait.setBorderWidth(0);

        commentator.setText(comment.getFromName() + isMe);
        content.setContentText(comment.getContent());
        time.setText(comment.getCreated());
        
        praise.setCount(comment.getLikeNumber());
        // 如果是本人查看自己发的帖子，则需查找否点过赞
        
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

    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private void initReplyData() {
        list = new ArrayList<>();

        PostComment comment = new PostComment();
        comment.setFromName("郭麒麟1"); comment.setContent("英雄所见略同");
        comment.setCreated("2020-4-1 22:35"); comment.setLikeNumber(88);
        list.add(comment);

        comment = new PostComment();
        comment.setFromName("郭麒麟2"); comment.setContent("英雄所见略同");
        comment.setCreated("2020-4-20 22:35"); comment.setLikeNumber(8);
        list.add(comment);

        comment = new PostComment();
        comment.setFromName("郭麒麟3"); comment.setContent("英雄所见略同");
        comment.setCreated("2020-3-1 22:35"); comment.setLikeNumber(63);
        list.add(comment);

        comment = new PostComment();
        comment.setFromName("郭麒麟4"); comment.setContent("英雄所见略同");
        comment.setCreated("2020-4-1 20:35"); comment.setLikeNumber(3);
        list.add(comment);

        comment = new PostComment();
        comment.setFromName("郭麒麟5"); comment.setContent("英雄所见略同");
        comment.setCreated("2020-4-1 22:39"); comment.setLikeNumber(28);
        list.add(comment);

        comment = new PostComment();
        comment.setFromName("郭麒麟6"); comment.setContent("英雄所见略同");
        comment.setCreated("2020-4-1 12:35"); comment.setLikeNumber(47);
        list.add(comment);

        list_order_by_asc = new ArrayList<>();
        list_order_by_asc.addAll(list);
        Collections.sort(list_order_by_asc, new Comparator<PostComment>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @Override
            public int compare(PostComment p1, PostComment p2) {
                try {
                    return f.parse(p1.getCreated()).compareTo(f.parse(p2.getCreated()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        list_order_by_des = new ArrayList<>();
        list_order_by_des.addAll(list);
        Collections.sort(list_order_by_des, new Comparator<PostComment>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @Override
            public int compare(PostComment p1, PostComment p2) {
                try {
                    return f.parse(p2.getCreated()).compareTo(f.parse(p1.getCreated()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        Collections.sort(list, new Comparator<PostComment>() {
            @Override
            public int compare(PostComment p1, PostComment p2) {
                int s1 = p1.getLikeNumber();
                int s2 = p2.getLikeNumber();
                return s1 < s2 ? s1 : (s1 == s2) ? 0 : -1;
            }
        });
    }

    private void deleteReply(Long id, int pos) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAILURE:
                        Log.i("删除回复: ", "失败");
                        Toast.makeText(CommentDetailActivity.this, "网络出了点问题，请稍候再试",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        Log.i("删除回复: ", "成功");
                        Toast.makeText(CommentDetailActivity.this, "删除成功",
                                Toast.LENGTH_LONG).show();
                        list.remove(pos);
                        setReplySum();
                        adapter.notifyDataSetChanged();
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
                    Log.i("删除回复: ", e.getMessage());
                    msg.what = FAILURE;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.i("删除回复：", response.body().string());
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }
            });

        }).start();
    }
}
