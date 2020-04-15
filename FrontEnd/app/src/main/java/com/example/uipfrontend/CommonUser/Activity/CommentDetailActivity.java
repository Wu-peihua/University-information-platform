package com.example.uipfrontend.CommonUser.Activity;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.CommonUser.Adapter.ReplyRecyclerViewAdapter;
import com.example.uipfrontend.Entity.PostComment;
import com.example.uipfrontend.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.widget.CircleImageView;
import com.parfoismeng.expandabletextviewlib.weiget.ExpandableTextView;
import com.sunbinqiang.iconcountview.IconCountView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * 当某条评论被点击时跳转到这个Activity
 * 跳转时携带该条评论对象
 */

public class CommentDetailActivity extends AppCompatActivity {

    private PostComment comment; // intent传递过来的对象

    private View headView; // 评论作为 RecyclerView 的头部

    private CircleImageView    portrait;     // 评论者头像
    private TextView           commentator;  // 评论者
    private ExpandableTextView content;      // 评论内容
    private TextView           time;         // 发布时间
    private IconCountView      praise;       // 点赞按钮
    private ImageView          report;       // 举报按钮
    private TextView           replySum;     // 评论数
//    private View               division;     // 分割线
    private LinearLayout       click_write;  // 点击回复

    private String             toName;       // 评论的对象
    private String             reference;    // 评论引用的内容

    private LinearLayout      commentBar;    // 底部评论栏
    private BottomSheetDialog commentDialog;
    private EditText          commentText;

    private XRecyclerView            xRecyclerView;
    private ReplyRecyclerViewAdapter adapter;

    private List<PostComment> list; // 评论列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_comment_detail);
        initReplyData(); // 回复测试数据
        init();
    }

    private void init() {
        commentBar = findViewById(R.id.ll_cu_forum_comment_detail_comment_bar);

        comment = (PostComment) Objects.requireNonNull(getIntent().getExtras()).get("comment");

        adapter = new ReplyRecyclerViewAdapter(this, list);

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

        initCommentDialog();
        setHeadView();
        setListener();

    }

    private void setListener() {
        adapter.setOnItemClickListener((view, pos) -> {
            toName = list.get(pos).getFromName();
            reference = list.get(pos).getContent();
            commentText.setHint("回复给：" + toName);
            commentDialog.show();
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

        praise.setOnStateChangedListener(new IconCountView.OnSelectedStateChangedListener() {
            @Override
            public void select(boolean isSelected) {
                if (isSelected == true)
                    comment.setLikeNum(comment.getLikeNum() + 1);
                else
                    comment.setLikeNum(comment.getLikeNum() - 1);
            }
        });

        report.setOnClickListener(view -> {
            // 
            Toast.makeText(this, "弹出提示,询问是否举报", Toast.LENGTH_SHORT).show();
        });

        click_write.setOnClickListener(view -> {
            commentText.setHint("回复给：" + comment.getFromName());
            commentDialog.show();
        });

        commentBar.setOnClickListener(view -> {
            commentText.setHint("回复给：" + comment.getFromName());
            commentDialog.show();
        });
    }

    private void initCommentDialog() {
        commentDialog = new BottomSheetDialog(this, R.style.BottomSheetEdit);
        View commentView = LayoutInflater.from(this).inflate(R.layout.dialog_comment_write, null);
        commentText = commentView.findViewById(R.id.et_dialog_comment);
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
                postComment.setToName(toName);
                postComment.setReference(reference);
                postComment.setContent(commentContent);
                postComment.setDate("2020-4-5 22:44");
                postComment.setLikeNum(0);
                list.add(postComment);
                adapter.notifyDataSetChanged();
                toName = null;
                commentText.setText("");
            } else {
                Toast.makeText(CommentDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            }
        });

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

    private void setHeadView() {
        initHeadView();

        String uri = "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg";
        //        String uri = comment.getPortrait();
        Glide.with(this).load(Uri.parse(uri))
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(portrait);
        portrait.setBorderWidth(0);

        commentator.setText(comment.getFromName());
        content.setContentText(comment.getContent());
        time.setText(comment.getDate());
        praise.setCount(comment.getLikeNum());
        
        replySum.setVisibility(View.VISIBLE);
        if(list.size() == 0) {
            replySum.setText("还没有人回复，快来抢沙发吧。");
        } else {
            replySum.setText(list.size() + "条对话");
        }

//        division.setVisibility(View.VISIBLE);

    }

    private void initHeadView() {
        portrait = headView.findViewById(R.id.imgv_cu_forum_comment_portrait);
        commentator = headView.findViewById(R.id.tv_cu_forum_comment_from);
        content = headView.findViewById(R.id.etv_cu_forum_comment_content);
        time = headView.findViewById(R.id.tv_cu_forum_comment_time);
        praise = headView.findViewById(R.id.praise_view_cu_forum_comment_like);
        report = headView.findViewById(R.id.imgv_cu_forum_comment_more);
//        division = headView.findViewById(R.id.view_cu_forum_comment_division);
        click_write = headView.findViewById(R.id.ll_cu_forum_comment_write);
        replySum = headView.findViewById(R.id.tv_cu_forum_comment_reply_sum);
    }

    private void initReplyData() {
        list = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            PostComment comment = new PostComment();
            comment.setFromName("郭麒麟" + i);
            comment.setContent("英雄所见略同");
            comment.setDate("2020-4-3 22:35");
            comment.setLikeNum(0);
            list.add(comment);
        }
    }
}
