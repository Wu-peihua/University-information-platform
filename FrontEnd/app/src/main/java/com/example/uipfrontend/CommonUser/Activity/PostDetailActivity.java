package com.example.uipfrontend.CommonUser.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.uipfrontend.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
 * 当某条帖子被点击时跳转到这个Activity
 * 跳转时携带该条帖子对象
 */
public class PostDetailActivity extends AppCompatActivity {

    private static final int FAILURE = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;

    private static final int PAGE_SIZE = 10; // 默认一次请求10条数据
    private int CUR_PAGE_NUM = 1;

    private static final DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static boolean flag = true;
    
    private UserInfo user;
    
    private ForumPosts post; // intent传递过来的对象

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
                    case FAILURE:
                        Log.i("获取评论: ", "失败");
                        break;
                    case ZERO:
                        Log.i("获取评论: ", "空");
                        setCommentSum();
                        break;
                    case SUCCESS:
                        Log.i("获取评论: ", "成功");
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        setCommentSum();
                        break;
                }
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
                    Log.i("获取评论: ", e.getMessage());
                    msg.what = FAILURE;
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
                            msg.what = FAILURE;
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
    
    private void insertComment(PostComment comment) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAILURE:
                        Log.i("插入评论: ", "失败");
                        Toast.makeText(PostDetailActivity.this, "网络出了点问题，请稍候再试",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        Log.i("插入评论: ", "成功");
                        
                        if (order_by_time.getText().toString().equals("时间↑")) {
                            list.add(0, comment);
                        } else {
                            list.add(comment);
                        }
                        adapter.notifyDataSetChanged();
                        setCommentSum();
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
            String json = gson.toJson(comment);

            Log.i("提交表单-评论：", json);

            RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"), json);

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.insertComment))
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
                    Log.i("插入评论：", resStr);
                    String resId = resStr.substring(1, resStr.length()-1).split(":")[1];
                    if (isNumber(resId)) {
                        comment.setInfoId(Long.valueOf(resId));
                        msg.what = SUCCESS;
                    } else {
                        msg.what = FAILURE;
                    }
                    handler.sendMessage(msg);
                }
            });

        }).start();
    }
    
    private void deletePost() {
        
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAILURE:
                        Log.i("删除帖子: ", "失败");
                        Toast.makeText(PostDetailActivity.this, "网络出了点问题，请稍候再试", 
                                Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        Log.i("删除帖子: ", "成功");
                        Toast.makeText(PostDetailActivity.this, "删除成功",
                                Toast.LENGTH_LONG).show();
                        // finish();
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
                    Log.i("删除帖子: ", e.getMessage());
                    msg.what = FAILURE;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.i("删除帖子：", response.body().string());
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }
            });
            
        }).start();
    }
    
    private void init() {
        user = (UserInfo) getApplication();
        initRecyclerView();
        initPostDetail();
        setPostDetail();
        initCommentDialog();
        setHeadListener();
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

        // 点赞按钮监听
        praise.setOnStateChangedListener(new IconCountView.OnSelectedStateChangedListener() {
            @Override
            public void select(boolean isSelected) {
                if (isSelected) {
                    post.setLikeNumber(post.getLikeNumber() + 1);
                }
                else {
                    post.setLikeNumber(post.getLikeNumber() - 1);
                }
            }
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
                                    Log.i("刷新评论", "成功");
                                    adapter.setList(list);
                                    adapter.notifyDataSetChanged();
                                    setCommentSum();
                                    break;
                                case FAILURE:
                                    Log.i("刷新评论", "失败");
                                    break;
                                case ZERO:
                                    Log.i("刷新评论", "0");
                                    setCommentSum();
                                    break;
                            }
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
                            xRecyclerView.loadMoreComplete();
                            switch (msg.what) {
                                case SUCCESS:
                                    Log.i("加载评论", "成功");
                                    adapter.notifyDataSetChanged();
                                    setCommentSum();
                                    break;
                                case FAILURE:
                                    Log.i("加载评论", "失败");
                                    break;
                                case ZERO:
                                    Log.i("加载评论", "0");
                                    setCommentSum();
                                    xRecyclerView.setNoMore(true);
                                    break;
                            }
                        }
                    };
                    CUR_PAGE_NUM++;
                    queryComment(handler, true);

                }, 1500);
            }
        });

        // 举报or删除监听
        detail_report.setOnClickListener(view -> {
            if (user.getUserId().equals(post.getUserId())) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定要删除吗")
                        .setPositiveButton("确定", (dialog1, which) -> {
                            deletePost();
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
                        .setMessage("如果该条帖子含有不恰当的内容，请点击确定")
                        .setPositiveButton("确定", (dialog1, which) -> {
                            post.setReportNumber(post.getReportNumber() + 1);
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

        // 弹出评论框
        commentBar.setOnClickListener(view -> {
            commentDialog.show();
        });

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
        adapter.setOnMoreClickListener((view, pos) -> {
            if (user.getUserId().equals(list.get(pos).getFromId())) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定要删除吗")
                        .setPositiveButton("确定", (dialog1, which) -> {
                            deleteComment(list.get(pos).getInfoId(), pos);
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
        });
    }
    
    private void setCommentSum() {
        if (list.size() == 0) {
            commentSum.setText("还没有人评论，快来抢沙发吧。");
        } else {
            commentSum.setText(list.size() + "条评论");
        }
    }
    
    /**
     * 显示帖子详情
     */
    private void setPostDetail() {
        String isMe = user.getUserId().equals(post.getUserId()) ? "(我)" : "";

        // String uri = "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg";
        String uri = post.getPortrait();
        Glide.with(this).load(Uri.parse(uri))
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(detail_portrait);
        detail_portrait.setBorderWidth(0);

        detail_title.setText(post.getTitle());
        detail_poster.setText(post.getUserName() + isMe);
        detail_content.setContentText(post.getContent());

        //        List<String> picUris = post.getPictures();
        List<ImageInfo> imageList = new ArrayList<>();
        //        for(String picUri : picUris) {
        //            ImageInfo image = new ImageInfo();
        //            image.setThumbnailUrl(picUri);
        //            image.setBigImageUrl(picUri);
        //            imageList.add(image);
        //        }

        // 临时图片
        ImageInfo image = new ImageInfo();
        String picUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585825362863&di=de3d04b6fa2086c93ba748613193b7c0&imgtype=0&src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_match%2F0%2F11506358584%2F0.jpg";
        image.setThumbnailUrl(picUrl);
        image.setBigImageUrl(picUrl);
        imageList.add(image); imageList.add(image); imageList.add(image);
        imageList.add(image); imageList.add(image); imageList.add(image);
        imageList.add(image); imageList.add(image); imageList.add(image);
        imageList.add(image); imageList.add(image); imageList.add(image);
        // 临时图片

        detail_pictures.setAdapter(new NineGridViewClickAdapter(this, imageList));

        detail_time.setText(post.getCreated());

        praise.setCount(post.getLikeNumber());
        // 如果是本人查看自己发的帖子，则需查找否点过赞
        
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
    
    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private void deleteComment(Long id, int pos) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAILURE:
                        Log.i("删除评论: ", "失败");
                        Toast.makeText(PostDetailActivity.this, "网络出了点问题，请稍候再试",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        Log.i("删除评论: ", "成功");
                        Toast.makeText(PostDetailActivity.this, "删除成功",
                                Toast.LENGTH_LONG).show();
                        list.remove(pos);
                        setCommentSum();
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
                            + getResources().getString(R.string.deleteComment))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("删除评论: ", e.getMessage());
                    msg.what = FAILURE;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.i("删除评论：", response.body().string());
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }
            });

        }).start();
    }
}