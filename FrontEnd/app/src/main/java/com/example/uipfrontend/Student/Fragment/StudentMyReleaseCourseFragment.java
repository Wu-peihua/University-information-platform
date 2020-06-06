package com.example.uipfrontend.Student.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Activity.AddResActivity;
import com.example.uipfrontend.CommonUser.Adapter.MyReleaseResInfoAdapter;
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.Entity.ResponseCourseComment;
import com.example.uipfrontend.Entity.ResponsePosts;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.Entity.UserRecord;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Adapter.StudentMyCommentCourseAdapter;
import com.example.uipfrontend.Utils.UserOperationRecord;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentMyReleaseCourseFragment extends Fragment {

    XRecyclerView xrv_studentReleaseCourse;

    private View rootView;
    private XRecyclerView xRecyclerView;
    private StudentMyCommentCourseAdapter adapter;
    private List<CourseComment> list;
    private int commentPos;
    private double commentScore;

    private FButton SubmitBtn;//提交评论按钮
    private EditText CommentEidt ;//编辑评论框
    private RatingBar UserRating;//评分星星
    private TextView tv_blank_text; // 空白提示

    private static final int FAIL = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;

    private static final int PAGE_SIZE = 6; // 默认一次请求10条数据
    private int CUR_PAGE_NUM = 1;


    private UserInfo user;//全局用户信息
    private static final String s3 = "deleteFromMyRelease";
    private static final String s4 = "updateFromMyRelease";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_my_release_course, null);
            tv_blank_text = rootView.findViewById(R.id.tv_blank_my_course);
            //initData();
            //initXRecyclerView();//我的评论列表
            //修改评论对话框
        }
        getCoursesComment();
        initXRecyclerView();
        setListener();
        return rootView;
    }

    /**
     * 描述：分页获取用户评论的记录
     */
    private void getCoursesComment() {

        ZLoadingDialog dialog = new ZLoadingDialog(getContext());
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(getResources().getColor(R.color.blue))//颜色
                .setHintText("加载中...")
                .show();
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAIL:
                        Log.i("获取参与的课程评论: ", "失败");
                        tv_blank_text.setText("好像出了点问题");
                        tv_blank_text.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        break;
                    case ZERO:
                        Log.i("获取参与的课程评论: ", "空");
                        //tv_blank_text.setText("还没有发过的帖子，去发一条");
                        tv_blank_text.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        break;
                    case SUCCESS:
                        Log.i("获取参与的课程评论: ", "成功");
                        tv_blank_text.setVisibility(View.GONE);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        queryCoursesComment(handler, false);
    }


    /**
     * 描述：启动线程获取评论
     * 参数: handler: 消息处理
     * 参数: isLoadMore: 加载处理
     * 返回：void
     */
    private void queryCoursesComment(Handler handler, boolean isLoadMore) {

        user = (UserInfo) Objects.requireNonNull(getActivity()).getApplication();

        new Thread(()->{

            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.queryCourseEvaluationByUserId)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE
                            + "&userId=" + user.getUserId())
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取参与的课程评论: ", e.getMessage());
                    msg.what = FAIL;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();

                    //日期格式化
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();
                    ResponseCourseComment responseCourseComment = gson.fromJson(resStr, ResponseCourseComment.class);

                    if (isLoadMore) {
                        list.addAll(responseCourseComment.getCourseEvaluationInfoList());
                        if (CUR_PAGE_NUM * PAGE_SIZE >= responseCourseComment.getTotal()) {
                            msg.what = ZERO;
                        } else {
                            msg.what = SUCCESS;
                        }
                    } else {
                        list = responseCourseComment.getCourseEvaluationInfoList();
                        if (list == null) {
                            list = new ArrayList<>();
                            msg.what = FAIL;
                        } else if (list.size() == 0) {
                            msg.what = ZERO;
                        } else {
                            Log.i("获取参与的课程评论: ", list.toString());
                            msg.what = SUCCESS;
                        }
                    }
                    handler.sendMessage(msg);
                }
            });

        }).start();
    }


    public void initXRecyclerView() {
        xRecyclerView = rootView.findViewById(R.id.xrv_studentReleaseCourse);
        adapter = new StudentMyCommentCourseAdapter(list, rootView.getContext());
        xRecyclerView.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext()) {
            @Override
            public boolean canScrollVertically() {
                if (list == null || list.size() == 0)
                    return false;
                return super.canScrollVertically();
            }
        };
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);


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
                                    Log.i("刷新我的课程评论", "成功");
                                    adapter.setList(list);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case FAIL:
                                    Log.i("刷新我的课程评论", "失败");
                                    break;
                                case ZERO:
                                    Log.i("刷新我的课程评论", "0");
                                    break;
                            }
                            xRecyclerView.refreshComplete();
                        }
                    };
                    CUR_PAGE_NUM = 1;
                    queryCoursesComment(handler, false);

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
                                    Log.i("加载我的课程评论", "成功");
                                    adapter.notifyDataSetChanged();
                                    break;
                                case FAIL:
                                    Log.i("加载我的课程评论", "失败");
                                    break;
                                case ZERO:
                                    Log.i("加载我的课程评论", "0");
                                    xRecyclerView.setNoMore(true);
                                    break;
                            }
                        }
                    };
                    CUR_PAGE_NUM++;
                    queryCoursesComment(handler, true);

                }, 1500);
            }
        });



    }

    public void setListener(){
        //编辑评论
        View DialogView = getLayoutInflater().inflate(R.layout.dialog_course_comment_edit, null);

        BottomSheetDialog Editdialog = new BottomSheetDialog(getContext());
        SubmitBtn = (FButton) DialogView.findViewById(R.id.my_comment_fbtn_submit);
        CommentEidt = (EditText) DialogView.findViewById(R.id.my_comment_edit_content);
        UserRating = (RatingBar) DialogView.findViewById(R.id.my_comment_set_rating);


        adapter.setOnItemDeleteClickListener(new StudentMyCommentCourseAdapter.OnItemDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                if (user.getUserId().equals(list.get(position).getCommentatorId())) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
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
                                deleteComment(list.get(position).getInfoId(), position, sweetAlertDialog);
                            })
                            .setCancelClickListener(SweetAlertDialog::cancel)
                            .show();
                    //mySendBroadCast(s3);
                }
            }
        });

        adapter.setOnItemModifyClickListener(new StudentMyCommentCourseAdapter.OnItemModifyClickListener() {
            @Override
            public void onModifyClick(int position) {
                commentPos = position;//获取修改的位置
                if(DialogView.getParent()!=null){
                    ((ViewGroup)DialogView.getParent()).removeView(DialogView);
                }
                //预设原始评分数据
                CommentEidt.setText(list.get(commentPos).getContent());
                UserRating.setRating(list.get(commentPos).getScore());
                Editdialog.setContentView(DialogView);
                Editdialog.show();

                Log.i("click","点击修改评论按钮");
            }

        });

        adapter.setOnLikeSelectListener((isSelected, pos) -> {
           // if (!user.getLikeRecord().containsKey("course_comment"+list.get(pos).getInfoId())) {
            if(isSelected){
                list.get(pos).setLikeCount(list.get(pos).getLikeCount() + 1);
                UserRecord record = new UserRecord();
                record.setUserId(user.getUserId());
                record.setToId(list.get(pos).getInfoId());
                record.setTag(1);
                record.setType(5);
                UserOperationRecord.insertRecord(getContext(), record, user);
                //System.out.println("当前举报："+comments.get(pos).getBadReportCount());
                //System.out.println("当前点赞："+comments.get(pos).getLikeCount());
            }
            else {
                list.get(pos).setLikeCount(list.get(pos).getLikeCount() - 1);
                //System.out.println("当前user like record:"+user.getLikeRecord());
                Long infoId = user.getLikeRecord().get("course_comment"+list.get(pos).getInfoId());
                //System.out.println("当前删除info:"+infoId);
                UserOperationRecord.deleteRecord(getContext(), infoId);
                user.getLikeRecord().remove("course_comment"+list.get(pos).getInfoId());

            }

        });

        UserRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
                                                {
                                                    @Override
                                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                        UserRating.setRating(rating);
                                                        commentScore = rating;
                                                        Log.i("set","修改ratingbar");
                                                    }
                                                }
        );

        /*************提交评论************************************************************/
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CourseComment newComment = list.get(commentPos);
                newComment.setCommentatorId(user.getUserId());
                newComment.setContent(CommentEidt.getText().toString());
                newComment.setScore((float)commentScore);
                newComment.setBadReportCount(0);
                newComment.setLikeCount(0);
                newComment.setInfoDate(new Date());
                //传送至数据库更新课程详情数据
                Editdialog.dismiss();
                SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("请稍后");
                pDialog.setCancelable(false);
                pDialog.show();
                updateCourseComment(newComment,pDialog);
                adapter.notifyDataSetChanged();
                //Toast.makeText(getContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                //添加到列表中
                /*
                Integer commentid = (int) Math.random()*1000;
                String UserName = "LIZZ";
                CourseComment newcomment = new CourseComment(commentid,UserName,new Date(),CommentEidt.getText().toString(),UserRating.getRating(),0);
                mTags.add(newcomment);
                commentAdapter.notifyDataSetChanged();
                commentSum.setText(mTags.size() + "条评论");

                System.out.println("用户评分"+UserRating.getRating());
                System.out.println("用户描述内容"+CommentEidt.getText().toString());
                Log.i("submit","成功添加评分");


                //mTags.add(userid,rating)
                Commentdialog.dismiss();
                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
*/
            }
        });

    }

    private void deleteComment(Long id, int pos, SweetAlertDialog sDialog) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAIL:
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
                        mySendBroadCast(s3);

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
                            + getResources().getString(R.string.deleteCourseEvaluation))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("删除评论: ", e.getMessage());
                    msg.what = FAIL;
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


    public void updateCourseComment(CourseComment courseComment, SweetAlertDialog sDialog){

        new Handler().postDelayed(()->{

            @SuppressLint("HandlerLeak")
            Handler handler = new Handler() {
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case SUCCESS:
                            String tipText = "修改成功";

                            sDialog.setTitleText(tipText)
                                    .setContentText("")
                                    .showCancelButton(false)
                                    .setConfirmText("关闭")
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            mySendBroadCast(s4);
                            // editFlag = false;
                            break;
                        case FAIL:
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
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();
                String json = gson.toJson(courseComment);

                Log.i("更新课程评论信息：", json);

                RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"), json);

                String requestUrl = getResources().getString(R.string.updateCourseEvaluation);

                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.serverBasePath) + requestUrl)
                        .post(requestBody)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.i("提交失败:", e.getMessage());
                        msg.what = FAIL;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.i("提交成功:", response.body().string());
                        msg.what = SUCCESS;
                        handler.sendMessage(msg);
                    }
                });

            }).start();

        }, 2000);
    }



    /**
     * 描述：评论、点赞后发送广播(给StudentCommentFragment)
     */
    private void mySendBroadCast(String s) {
        Intent intent = new Intent();
        //intent.putExtra("pos", coursePos);
        //System.out.println("发送广播！>>>>>>>>>>>>>");
        //intent.putExtra("courseId",1);
        intent.setAction(s);
        LocalBroadcastManager.getInstance(getContext())
                .sendBroadcast(intent);
        //System.out.println("发送完毕！>>>>>>>>>>>>>");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == 1) {
                list.remove(commentPos);
                CourseComment receiveData = (CourseComment) data.getSerializableExtra("resInfo");
                list.add(0, receiveData);
                adapter.notifyDataSetChanged();
            }
    }
}