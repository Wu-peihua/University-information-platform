package com.example.uipfrontend.CommonUser.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.ForumPosts;
import com.example.uipfrontend.R;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.widget.CircleImageView;
import com.squareup.picasso.Picasso;
import com.sunbinqiang.iconcountview.IconCountView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostDetailActivity extends AppCompatActivity {
    
    private NineGridView detail_pictures;
    private CircleImageView detail_portrait;
    private TextView detail_title;
    private TextView detail_content;
    private TextView detail_poster;
    private TextView detail_time;
    private IconCountView praise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        NineGridView.setImageLoader(new PicassoImageLoader());
        init();
    }

    /** Picasso 加载 */
    private class PicassoImageLoader implements NineGridView.ImageLoader {

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
        detail_pictures = findViewById(R.id.nineGrid_cu_forum_detail_pic);
        detail_portrait = findViewById(R.id.imgv_cu_forum_detail_portrait);
        detail_title = findViewById(R.id.tv_cu_forum_detail_title);
        detail_content = findViewById(R.id.tv_cu_forum_detail_content);
        detail_poster = findViewById(R.id.tv_cu_forum_detail_poster);
        detail_time = findViewById(R.id.tv_cu_forum_detail_time);
        praise = findViewById(R.id.praise_view_cu_forum_detail_like);

        ForumPosts post = (ForumPosts) Objects.requireNonNull(getIntent().getExtras()).get("detail");

        List<ImageInfo> imageList = new ArrayList<>();
        ImageInfo image = new ImageInfo();
        String picUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585825362863&di=de3d04b6fa2086c93ba748613193b7c0&imgtype=0&src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_match%2F0%2F11506358584%2F0.jpg";
        image.setThumbnailUrl(picUrl);
        image.setBigImageUrl(picUrl);
        imageList.add(image); imageList.add(image);
        imageList.add(image); imageList.add(image);
        imageList.add(image); imageList.add(image);
        imageList.add(image); imageList.add(image);
        imageList.add(image); imageList.add(image);
        imageList.add(image); imageList.add(image);
        detail_pictures.setAdapter(new NineGridViewClickAdapter(this, imageList));
        
        Glide.with(this).load(Uri.parse("http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg"))
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(detail_portrait);
        detail_portrait.setBorderWidth(0);
        detail_title.setText(post.getTitle());
//        detail_content.setText(post.getContent());
        detail_poster.setText(post.getPoster());
        detail_time.setText(post.getPostTime());
        praise.setCount(post.getLikeNum());
    }
}