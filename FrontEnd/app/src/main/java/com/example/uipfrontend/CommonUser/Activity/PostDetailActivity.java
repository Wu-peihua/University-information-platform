package com.example.uipfrontend.CommonUser.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.example.uipfrontend.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.widget.CircleImageView;
import com.parfoismeng.expandabletextviewlib.weiget.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.sunbinqiang.iconcountview.IconCountView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/*
 * 当某条帖子被点击时跳转到这个Activity
 * 跳转时携带该条帖子对象
 */
public class PostDetailActivity extends AppCompatActivity {

    private ForumPosts post; // intent传递过来的对象

    private View headView; // 帖子详情作为 RecyclerView 的头部

    private CircleImageView    detail_portrait; // 发帖人头像
    private TextView           detail_title;    // 帖子标题
    private TextView           detail_poster;   // 发帖人
    private ExpandableTextView detail_content;  // 帖子内容
    private NineGridView       detail_pictures; // 帖子图片
    private TextView           detail_time;     // 发布时间
    private IconCountView      praise;          // 点赞按钮
    private ImageView          detail_report;   // 举报按钮
    
    private TextView           commentSum;      // 评论数
    private TextView           order_by_time;   // 按时间排序
    private TextView           order_by_like;   // 按热度排序

    private LinearLayout       commentBar;      // 底部评论栏
    private BottomSheetDialog  commentDialog;

    private XRecyclerView              xRecyclerView;
    private CommentRecyclerViewAdapter adapter;

    private List<PostComment> list; // 评论列表 默认按热度从高到低
    private List<PostComment> list_order_by_asc; // ↓：按时间从远到近
    private List<PostComment> list_order_by_des; // ↑：按时间从近到远

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post_detail);
        NineGridView.setImageLoader(new PicassoImageLoader());
        initCommentData(); // 评论测试数据
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

    private void init() {
        commentBar = findViewById(R.id.ll_cu_forum_post_detail_comment_bar);

        // 获取intent传递来的帖子对象
        post = (ForumPosts) Objects.requireNonNull(getIntent().getExtras()).get("detail");

        //        list = getComment();

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

        // 帖子详情
        setPostDetail();

        setListener();
    }

    private void setListener() {
        
        // 按热度从高到低排序
        order_by_like.setOnClickListener(view -> {
            if(!order_by_time.getText().toString().equals("时间")) {
                order_by_like.setTextColor(getResources().getColor(R.color.blue));
                order_by_time.setTextColor(getResources().getColor(R.color.gray));
                order_by_time.setText("时间");
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        });
        
        // 按时间排序
        order_by_time.setOnClickListener(view -> {
            order_by_time.setTextColor(getResources().getColor(R.color.blue));
            order_by_like.setTextColor(getResources().getColor(R.color.gray));
            String text = order_by_time.getText().toString();
            if(text.contains("↑")){
                order_by_time.setText("时间↓");
                adapter.setList(list_order_by_asc);
            } else if(text.contains("↓")) {
                order_by_time.setText("时间↑");
                adapter.setList(list_order_by_des);
            } else {
                order_by_time.setText("时间↓");
                adapter.setList(list_order_by_asc);
            }
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
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                xRecyclerView.setNoMore(true);
            }
        });

        // 举报监听
        detail_report.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("如果该条帖子含有不恰当的内容，请点击确定")
                    .setPositiveButton("确定", (dialog1, which) -> {
                        Toast.makeText(this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("取消", null)
                    .setCancelable(false)
                    .create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
        });

        // 弹出评论框
        commentBar.setOnClickListener(view -> {
            commentDialog = new BottomSheetDialog(this, R.style.BottomSheetEdit);
            View commentView = LayoutInflater.from(this).inflate(R.layout.dialog_comment_write, null);
            EditText commentText = commentView.findViewById(R.id.et_dialog_comment);
            Button btn_submit = commentView.findViewById(R.id.btn_dialog_comment);
            commentDialog.setContentView(commentView);

            // 配合 R.style.BottomSheetEdit 解决键盘遮挡问题
            View parent = (View) commentView.getParent();
            BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
            commentView.measure(0, 0);
            behavior.setPeekHeight(commentView.getMeasuredHeight());

            btn_submit.setOnClickListener(view1 -> {
                String commentContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(commentContent)) {
                    commentDialog.dismiss();
                    PostComment postComment = new PostComment();
                    postComment.setPortrait("");
                    postComment.setFromName("精神小伙");
                    postComment.setContent(commentContent);
                    postComment.setDate("2020-4-25 12:34");
                    postComment.setLikeNum(0);
                    list.add(postComment);
                    list_order_by_asc.add(postComment);
                    list_order_by_des.add(0, postComment);
                    adapter.notifyDataSetChanged();
                    commentSum.setText(list.size() + "条评论");
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

            commentDialog.show();
        });
        
    }

    private List getComment() {
        //        Integer id = post.getPostId();
        //        根据帖子id从评论表中获取该条帖子的评论
        List<PostComment> res = null;
        return res;
    }

    /**
     * 显示帖子详情
     */
    private void setPostDetail() {
        initPostDetail();

        // String uri = "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg";
        String uri = post.getPortrait();
        Glide.with(this).load(Uri.parse(uri))
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(detail_portrait);
        detail_portrait.setBorderWidth(0);

        detail_title.setText(post.getTitle());
        detail_poster.setText(post.getUserName());
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

        if (list.size() == 0) {
            commentSum.setText("还没有人评论，快来抢沙发吧。");
        } else {
            commentSum.setText(list.size() + "条评论");
        }
    }

    /**
     * 初始化帖子详情
     */
    private void initPostDetail() {
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

    private void initCommentData() {
        list = new ArrayList<>();

        PostComment comment = new PostComment();
        comment.setFromName("韦骁龙1"); comment.setContent("英雄所见略同"); 
        comment.setDate("2020-4-1 22:35"); comment.setLikeNum(88);
        list.add(comment);

        comment = new PostComment();
        comment.setFromName("韦骁龙2"); comment.setContent("英雄所见略同");
        comment.setDate("2020-4-20 22:35"); comment.setLikeNum(8);
        list.add(comment);

        comment = new PostComment();
        comment.setFromName("韦骁龙3"); comment.setContent("英雄所见略同");
        comment.setDate("2020-3-1 22:35"); comment.setLikeNum(63);
        list.add(comment);

        comment = new PostComment();
        comment.setFromName("韦骁龙4"); comment.setContent("英雄所见略同");
        comment.setDate("2020-4-1 20:35"); comment.setLikeNum(3);
        list.add(comment);

        comment = new PostComment();
        comment.setFromName("韦骁龙5"); comment.setContent("英雄所见略同");
        comment.setDate("2020-4-1 22:39"); comment.setLikeNum(28);
        list.add(comment);

        comment = new PostComment();
        comment.setFromName("韦骁龙6"); comment.setContent("英雄所见略同");
        comment.setDate("2020-4-1 12:35"); comment.setLikeNum(47);
        list.add(comment);

        list_order_by_asc = new ArrayList<>();
        list_order_by_asc.addAll(list);
        Collections.sort(list_order_by_asc, new Comparator<PostComment>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @Override
            public int compare(PostComment p1, PostComment p2) {
                try {
                    return f.parse(p1.getDate()).compareTo(f.parse(p2.getDate()));
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
                    return f.parse(p2.getDate()).compareTo(f.parse(p1.getDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        Collections.sort(list, new Comparator<PostComment>() {
            @Override
            public int compare(PostComment p1, PostComment p2) {
                int s1 = p1.getLikeNum();
                int s2 = p2.getLikeNum();
                return s1 < s2 ? s1 : (s1 == s2) ? 0 : -1;
            }
        });

    }

}