package com.zxy.and2;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.InfoBean;
import com.aliyun.player.bean.InfoCode;
import com.aliyun.player.nativeclass.CacheConfig;
import com.aliyun.player.source.UrlSource;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CommunityActivity extends BaseActivity {


    private VerticalViewPager vp;
    private MyPagerAdapter myPagerAdapter;
    private int currentFlagPostion;//传递过来播放第几个
    private List<Video> list = new ArrayList<>();//播放列表
    private int mCurrentPosition;//当前正在播放第几个
    private AliPlayer aliPlayer;//当前正在播放的播放器

    public CommunityActivity() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        vp = findViewById(R.id.main_vp);
        initViewPager();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    ProgressBar progressBar = vp.getChildAt(vp.getCurrentItem()).findViewById(R.id.com_seekbar);
                    progressBar.setProgress((int) (myPagerAdapter.playerInfoList.get(vp.getCurrentItem()).getVideoProgress() * 100 / aliPlayer.getDuration()));
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        };
        timer.schedule(task, 1000, 100);
    }


    private void initViewPager() {
        currentFlagPostion = getIntent().getIntExtra("currentPostion", 0);
        List<Video> videoList=new ArrayList<>();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://" + AppControlor.serverIp + "/video_items").build();
            Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            videoList=gson.fromJson(response.body().string(),new TypeToken<List<Video>>(){}.getType());
        }catch (Exception e){
            e.printStackTrace();
            //注意我这里的封面图是随便加的，和数据不匹配。
//            Video video1 = new Video();
//            video1.setVideoUrl("https://www.douyin.com/aweme/v1/play/?video_id=v0300fg10000c1im43d9gcp4tevkikfg&line=0&file_id=1d9c0981d079407680c762b6f88cb81a&sign=681a1943dbb9506b4c73fe4fe4d43bcc&is_play_url=1&source=PackSourceEnum_AWEME_DETAIL&aid=6383");
////        video1.setVideoUrl("http://192.168.0.168/video?id=video0.mp4");
//            video1.setTitle("和易烊千玺一起，关注留守儿童");
//            video1.setImageUrl("https://imgaliyuncdn.miaopai.com/images/oUiRwCTfrjAbqXC2LlgwhYwKkBc5RspLUcDuAg___fqbC_4.jpg");
//            video1.setAuthor("易烊千玺");
//            video1.setContent("有一些孩子，他们可能不善言辞，但他们坚强、勇敢，他们也需要被爱，让我们一起关注留守儿童，为他们献出我们的小红花吧！");
//
//            Video video2 = new Video();
//            video2.setVideoUrl("https://www.douyin.com/aweme/v1/play/?video_id=v0300fde0000bsvu0qf1knst617l4j10&line=0&file_id=037433f6fa104cdab4b1c0c18b9de228&sign=ad32cb6d47530c4bd6bd69cbb9659b96&is_play_url=1&source=PackSourceEnum_AWEME_DETAIL&aid=6383");
//            video2.setTitle("李现助力人人公益节");
//            video2.setImageUrl("https://wx4.sinaimg.cn/large/721397ecly1g6vwv65jjsj20i60a80ti.jpg");
//            video2.setAuthor("李现");
//            video2.setContent("和 @李现 一起把你的艺术创作或是受到艺术启发的故事分享给留守儿童们，一起关注乡村儿童、关注乡村教育，献出爱心，成为志愿者！人人公益，我愿意!");
//
//            Video video3 = new Video();
//            video3.setVideoUrl("https://www.douyin.com/aweme/v1/play/?video_id=v0200fff0000bell43akr6g5lor88irg&line=0&file_id=6112cf6f35c74d779f01acee577b32ae&sign=2d802574a3a68d520a9e009cd29aa071&is_play_url=1&source=PackSourceEnum_AWEME_DETAIL&aid=6383");
//            video3.setTitle("“电力妈妈”点亮留守儿童微心愿");
//            video3.setImageUrl("https://wx1.sinaimg.cn/large/d2b7854cly1h22d4ax1ahj20ru0fotby.jpg");
//            video3.setAuthor("国家电网-浙江电力");
//            video3.setContent("近日，巾帼文明岗爱心团队走进大东坝中心小学，充当留守儿童的“电力妈妈”，为留守儿童精心准备低碳环保的电力安全知识课程，并认领留守儿童微心愿，与孩子们一同包饺子、共进爱心午餐。");
        }

        list.addAll(videoList);

        myPagerAdapter = new MyPagerAdapter();
        vp.setAdapter(myPagerAdapter);
        vp.setOffscreenPageLimit(3);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int postition) {
                mCurrentPosition = postition;
                // 滑动界面，首先让之前的播放器暂停，并seek到0
                if (aliPlayer != null) {
                    aliPlayer.seekTo(0);
                    aliPlayer.pause();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        vp.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                if (position != 0) {
                    return;
                }
                PlayerInfo playerInfo = myPagerAdapter.findPlayerInfo(mCurrentPosition);
                if (playerInfo != null) {
                    if (playerInfo.getAliPlayer() != null) {
                        playerInfo.getAliPlayer().start();
                        aliPlayer = playerInfo.getAliPlayer();
                    }
                }
            }
        });
        vp.setCurrentItem(currentFlagPostion);
    }


    class MyPagerAdapter extends PagerAdapter {

        ArrayList<PlayerInfo> playerInfoList = new ArrayList<>();
        private LinkedList<View> mViewCache = new LinkedList<>();

        protected PlayerInfo instantiatePlayerInfo(int position) {
            AliPlayer aliyunVodPlayer = AliPlayerFactory.createAliPlayer(getApplicationContext());
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.setPlayURL(list.get(position).getVideoUrl());
            aliyunVodPlayer.setOnStateChangedListener(new IPlayer.OnStateChangedListener() {
                @Override
                public void onStateChanged(int newState) {
                    playerInfo.setPlayerState(newState);
                    /*
                      int idle = 0;
                      int initalized = 1;
                      int prepared = 2;
                      int started = 3;
                      int paused = 4;
                      int stopped = 5;
                      int completion = 6;
                      int error = 7;
                  */
                }
            });
            aliyunVodPlayer.setOnInfoListener(new IPlayer.OnInfoListener() {
                @Override
                public void onInfo(InfoBean infoBean) {
                    if (infoBean.getCode() == InfoCode.CurrentPosition) {
                        //extraValue为当前播放进度，单位为毫秒
                        playerInfo.setVideoProgress(infoBean.getExtraValue());
//                        SeekBar seekBar=vp.getChildAt(vp.getCurrentItem()).findViewById(R.id.com_seekbar);
//                        seekBar.setProgress((int)(infoBean.getExtraValue()/aliyunVodPlayer.getDuration()*100));
                    }
                }
            });
            playerInfo.setAliPlayer(aliyunVodPlayer);
            playerInfo.setPosition(position);
            playerInfoList.add(playerInfo);
            return playerInfo;
        }

        public PlayerInfo findPlayerInfo(int position) {
            for (int i = 0; i < playerInfoList.size(); i++) {
                PlayerInfo playerInfo = playerInfoList.get(i);
                if (playerInfo.getPosition() == position) {
                    return playerInfo;
                }
            }
            return null;
        }

        public void mOnDestroy() {
            for (PlayerInfo playerInfo : playerInfoList) {
                if (playerInfo.getAliPlayer() != null) {
                    playerInfo.getAliPlayer().release();
                }
            }
            playerInfoList.clear();
        }

        protected void destroyPlayerInfo(int position) {
            while (true) {
                PlayerInfo playerInfo = findPlayerInfo(position);
                if (playerInfo == null)
                    break;
                if (playerInfo.getAliPlayer() == null)
                    break;
                playerInfo.getAliPlayer().release();
                playerInfoList.remove(playerInfo);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View view;
            if (mViewCache.size() == 0) {
                view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_com_viewpage, null, false);
            } else {
                view = mViewCache.removeFirst();
            }
            view.setId(position);

            TextView videoTitle = view.findViewById(R.id.item_main_video_title);
            TextView videoAuthor = view.findViewById(R.id.com_name);
            TextView videoContent = view.findViewById(R.id.com_content);
//            SeekBar seekBar = view.findViewById(R.id.com_seekbar);
            final ImageView coverPicture = view.findViewById(R.id.item_main_cover_picture);
            SurfaceView surfaceView = view.findViewById(R.id.item_main_surface_view);

            surfaceView.setZOrderOnTop(true);
//            surfaceView.setZOrderMediaOverlay(true);

            if (!TextUtils.isEmpty(list.get(position).getImageUrl())) {
                coverPicture.setVisibility(View.VISIBLE);
                Glide.with(CommunityActivity.this).load(list.get(position).getImageUrl()).into(coverPicture);
            }
            videoTitle.setText("< 视频：" + list.get(position).getTitle());
            videoAuthor.setText(" @" + list.get(position).getAuthor());
            videoContent.setText(list.get(position).getContent());

            videoTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnDestroy();
                    finish();
                }
            });

            final PlayerInfo playerInfo = instantiatePlayerInfo(position);
            surfaceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (playerInfo.getPlayerState() == 3) {
                        aliPlayer.pause();
                        Toast.makeText(CommunityActivity.this,"已暂停",Toast.LENGTH_SHORT).show();
                    }
                    else if (playerInfo.getPlayerState() == 4) aliPlayer.start();
                }
            });
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    playerInfo.getAliPlayer().setDisplay(holder);

                    ImageButton thumb_up = view.findViewById(R.id.com_thumb_up);
                    thumb_up.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void onClick(View view) {
                            if (playerInfo.thumb_up) {
                                thumb_up.setImageDrawable(getDrawable(R.drawable.ic_baseline_thumb_up_24));
                            } else {
                                thumb_up.setImageDrawable(getDrawable(R.drawable.ic_baseline_thumb_up_24_orange));
                            }
                            playerInfo.thumb_up = !playerInfo.thumb_up;
                        }
                    });

                    ImageButton share=view.findViewById(R.id.com_share);
                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //获取剪贴板管理器
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            // 创建普通字符型ClipData
                            ClipData mClipData = ClipData.newPlainText("VideoURL", playerInfo.getPlayURL());
                            // 将ClipData内容放到系统剪贴板里。
                            cm.setPrimaryClip(mClipData);
                            Toast.makeText(CommunityActivity.this,"已将视频链接复制到剪切板",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    playerInfo.getAliPlayer().redraw();
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    playerInfo.getAliPlayer().setDisplay(null);
                }
            });
            UrlSource urlSource = new UrlSource();
            urlSource.setUri(list.get(position).getVideoUrl());
            //设置播放源
            playerInfo.getAliPlayer().setDataSource(urlSource);
            //准备播放
            playerInfo.getAliPlayer().prepare();

            //开启缓存
            CacheConfig cacheConfig = new CacheConfig();
            //开启缓存功能
            cacheConfig.mEnable = true;
            //能够缓存的单个文件最大时长。超过此长度则不缓存
            cacheConfig.mMaxDurationS = 300;
            //缓存目录的位置
            cacheConfig.mDir = "hbw";
            //缓存目录的最大大小。超过此大小，将会删除最旧的缓存文件
            cacheConfig.mMaxSizeMB = 200;
            //设置缓存配置给到播放器
            playerInfo.getAliPlayer().setCacheConfig(cacheConfig);

            playerInfo.getAliPlayer().setLoop(true);
            playerInfo.getAliPlayer().setOnPreparedListener(new IPlayer.OnPreparedListener() {
                @Override
                public void onPrepared() {
                    // 视频准备成功之后影响封面图
                    if (!TextUtils.isEmpty(list.get(position).getImageUrl())) {
                        coverPicture.setVisibility(LinearLayout.GONE);
                    }
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            destroyPlayerInfo(position);
            View contentView = (View) object;
            container.removeView(contentView);
            mViewCache.add(contentView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (aliPlayer != null) {
            aliPlayer.release();
        }
        if (myPagerAdapter != null) {
            myPagerAdapter.mOnDestroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (aliPlayer != null) {
            aliPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (aliPlayer != null) {
            aliPlayer.start();
        }
    }

//    private void initViewPager(){
//        Video video0 = new Video();
//        video0.setVideoUrl(R.raw.video0);
//        video0.setTitle("易烊千玺呼吁关爱留守儿童");
//        video0.setAuthor("易烊千玺");
//        video0.setContent("有一些孩子，他们可能得不到父母的悉心照料...");
//
//        Video video1 = new Video();
//        video1.setVideoUrl(R.raw.video1);
//        video1.setTitle("李现助力人人公益节");
//        video1.setAuthor("李现");
//        video1.setContent("你还记得当年希望工程中的那个大眼睛女孩吗？");
//
//        Video video2 = new Video();
//        video2.setVideoUrl(R.raw.video2);
//        video2.setTitle("电力妈妈点亮留守儿童微心愿");
//        video2.setAuthor("国家电网-浙江");
//        video2.setContent("近日，@国网松阳县供电公司 巾帼文明岗爱心团队走进大东坝中心小学，充当留守儿童的“电力妈妈”，为留守儿童精心准备低碳环保、电力安全知识课程，并认领留守儿童“微心愿”，与孩子们一同包饺子，共进爱心午餐。 \u200B\n");
//
//        Video video4 = new Video();
//        video4.setVideoUrl(R.raw.video4);
//        video4.setTitle("“95后”校长！留守儿童的“小太阳”");
//        video4.setAuthor("贺梦琦");
//        video4.setContent("近日，记者随访来到陶营镇姚沟小学，整个学校共有5间教室，47名学生，其中留守儿童就有20多名。虽然班级小，学生少，但年轻的校长——贺梦琦以“95后”的蓬勃朝气，像“大哥哥”，又像“父亲”一样，带领乡村留守儿童向快乐出发，把学校办得像个家。");
//
//        list.add(video0);
//        list.add(video1);
//        list.add(video2);
//        list.add(video4);
//
//        myPagerAdapter=new MyPagerAdapter();
//        vp.setAdapter(myPagerAdapter);
//        vp.setOffscreenPageLimit(2);
//        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//            }
//
//            @Override
//            public void onPageSelected(int postition) {
//                mCurrentPosition = postition;
//                // 滑动界面，首先让之前的播放器暂停
//                if (player != null) {
//                    myPagerAdapter.findPlayerInfo(postition).progress=player.getCurrentPosition();
//                    player.pause();
//                    SurfaceView videoView=vp.getChildAt(postition).findViewById(R.id.item_main_surface_view);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//        vp.setPageTransformer(false, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                if (position != 0) {
//                    return;
//                }
//                PlayerInfo playerInfo = myPagerAdapter.findPlayerInfo(mCurrentPosition);
//                if (playerInfo != null) {
//                    if (playerInfo.getMediaPlayer() != null) {
//                        playerInfo.getMediaPlayer().start();
//                        playerInfo.getMediaPlayer().setLooping(true);
//                        player = playerInfo.getMediaPlayer();
//                    }
//                }
//            }
//        });
//        vp.setCurrentItem(currentFlagPostion);
//    }
//
//    class MyPagerAdapter extends PagerAdapter {
//
//        ArrayList<PlayerInfo> playerInfoList = new ArrayList<>();
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//            return view == object;
//        }
//
//        protected PlayerInfo instantiatePlayerInfo(int position) {
//            PlayerInfo playerInfo = new PlayerInfo();
//            playerInfo.setPlayURL(list.get(position).getVideoUrl());
//            MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(),list.get(position).getVideoUrl());
//            playerInfo.setMediaPlayer(mediaPlayer);
//            playerInfo.setPosition(position);
//            playerInfoList.add(playerInfo);
//            return playerInfo;
//        }
//
//        public PlayerInfo findPlayerInfo(int position) {
//            for (int i = 0; i < playerInfoList.size(); i++) {
//                PlayerInfo playerInfo = playerInfoList.get(i);
//                if (playerInfo.getPosition() == position) {
//                    return playerInfo;
//                }
//            }
//            return null;
//        }
//
//        @SuppressLint("SetTextI18n")
//        @NonNull
//        @Override
//        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
//            View view;
////            if (mViewCache.size() == 0) {
//                view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_com_viewpage, null, false);
////            } else {
////                view = mViewCache.removeFirst();
////            }
////            view.setId(position);
//
//            TextView videoTitle = view.findViewById(R.id.item_main_video_title);
//            TextView videoAuthor = view.findViewById(R.id.com_name);
//            TextView videoContent = view.findViewById(R.id.com_content);
//            SurfaceView surfaceView = view.findViewById(R.id.item_main_surface_view);
//
//            surfaceView.setZOrderOnTop(true);
//            surfaceView.setZOrderMediaOverlay(true);
//            surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//
//            videoTitle.setText("视频：" + list.get(position).getTitle());
//            videoAuthor.setText(" @"+list.get(position).getAuthor());
//            videoContent.setText(list.get(position).getContent());
//            surfaceView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(player.isPlaying())player.pause();
//                    else player.start();
//                }
//            });
//
//            final PlayerInfo playerInfo = instantiatePlayerInfo(position);
//            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//                @Override
//                public void surfaceCreated(SurfaceHolder holder) {
//                    playerInfo.getMediaPlayer().setDisplay(holder);
//
//                    LayoutInflater inflater = (LayoutInflater)requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View mMainView = inflater.inflate(R.layout.activity_main, null);
//                    int width = requireContext().getResources().getDisplayMetrics().widthPixels;
////                    int height = requireContext().getResources().getDisplayMetrics().heightPixels;
//                    int height=width*9/16;
//                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
//                    lp.gravity = Gravity.CENTER;
////                    lp.leftMargin = 0; //矩形距离原点最近的点距离X轴的距离
////                    lp.topMargin = 0; //矩形距离原点最近的点距离Y轴的距离
//                    //以上两个值，即坐标(x,y);
//                    surfaceView.setLayoutParams(lp); //调整surfaceview控件的大小
//
//                    ImageButton thumb_up=view.findViewById(R.id.com_thumb_up);
//                    thumb_up.setOnClickListener(new View.OnClickListener() {
//                        @SuppressLint("UseCompatLoadingForDrawables")
//                        @Override
//                        public void onClick(View view) {
//                            if(playerInfo.thumb_up){
//                                thumb_up.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_thumb_up_24));
//                            }
//                            else {
//                                thumb_up.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_thumb_up_24_red));
//                            }
//                            playerInfo.thumb_up=!playerInfo.thumb_up;
//                        }
//                    });
//                }
//
//                @Override
//                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                    Toast.makeText(requireContext(),"1",Toast.LENGTH_SHORT).show();
//                    playerInfo.getMediaPlayer().start();
//                    if(playerInfo.getPosition()!=mCurrentPosition)playerInfo.getMediaPlayer().pause();
//                }
//
//                @Override
//                public void surfaceDestroyed(SurfaceHolder holder) {
//                    playerInfo.getMediaPlayer().setDisplay(null);
//                }
//            });
//
//            container.addView(view);
//            return view;
//        }
//
//        @Override
//        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
////            super.destroyItem(container, position, object);
//            container.removeView((View) object);
//        }
//    }
//
//    @Override
//    public void onPause() {
//        if(player.isPlaying())player.pause();
//        SurfaceView videoView=vp.getChildAt(vp.getCurrentItem()).findViewById(R.id.item_main_surface_view);
//        videoView.setVisibility(View.INVISIBLE);
//        super.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if(player!=null&&MainActivity.currentFragment==MainActivity.comFragment){
//            SurfaceView videoView=vp.getChildAt(vp.getCurrentItem()).findViewById(R.id.item_main_surface_view);
//            videoView.setVisibility(View.VISIBLE);
//            if(!player.isPlaying())player.start();
//        }
//    }
}