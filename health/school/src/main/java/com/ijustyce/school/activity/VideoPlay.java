package com.ijustyce.school.activity;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ijustyce.fastandroiddev.base.BaseActivity;
import com.ijustyce.fastandroiddev.baseLib.utils.ILog;
import com.ijustyce.school.R;

import butterknife.Bind;

/**
 * Created by yc on 16-3-20.    视频播放
 */
public class VideoPlay extends BaseActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener{

    @Bind(R.id.videoView)
    VideoView videoView;

    @Override
    public void afterCreate() {

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.containsKey("uri")){
            finish();
            return;
        }

        Uri uri = Uri.parse(bundle.getString("uri"));
        if (uri == null){
            finish();
            return;
        }

        //  解决启动黑屏
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //  横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        MediaController mc = new MediaController(mContext);
        videoView.setMediaController(mc);
        videoView.setOnCompletionListener(this);
        videoView.setOnErrorListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_play;
    }

    /**
     * 视频播放完成以后调用的回调函数
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }
    /**
     * 视频播放发生错误时调用的回调函数
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what){
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                ILog.e("text","发生未知错误");

                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                ILog.e("text","媒体服务器死机");
                break;
            default:
                ILog.e("text","onError " + what);
                break;
        }
        switch (extra){
            case MediaPlayer.MEDIA_ERROR_IO:
                //io读写错误
                ILog.e("text","文件或网络相关的IO操作错误");
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                //文件格式不支持
                ILog.e("text","比特流编码标准或文件不符合相关规范");
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                //一些操作需要太长时间来完成,通常超过3 - 5秒。
                ILog.e("text","操作超时");
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                //比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
                ILog.e("text","比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
                break;
            default:
                ILog.e("text","onError " + extra);
                break;
        }
        //如果未指定回调函数， 或回调函数返回假，VideoView 会通知用户发生了错误。
        return false;
    }

    public void doResume(){

        if (videoView != null && !videoView.isPlaying()){
            videoView.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    /**
     * 视频文件加载文成后调用的回调函数
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        //如果文件加载成功,隐藏加载进度条

        ILog.d("===mediaplayer===", "文件加载成功...");
    }
}
