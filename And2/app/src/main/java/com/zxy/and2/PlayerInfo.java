package com.zxy.and2;


import com.aliyun.player.AliPlayer;

public class PlayerInfo {

    private String playURL;
    private AliPlayer aliPlayer;
    private int position;
    private int playerState;
    private long videoProgress;
    public boolean thumb_up;

    public PlayerInfo() {

    }

    public PlayerInfo(String playURL, AliPlayer aliPlayer, int position, int playerState, long videoProgress) {
        this.playURL = playURL;
        this.aliPlayer = aliPlayer;
        this.position = position;
        this.playerState = playerState;
        this.videoProgress = videoProgress;
    }

    public String getPlayURL() {
        return playURL;
    }

    public void setPlayURL(String playURL) {
        this.playURL = playURL;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public AliPlayer getAliPlayer() {
        return aliPlayer;
    }

    public void setAliPlayer(AliPlayer aliPlayer) {
        this.aliPlayer = aliPlayer;
    }

    public int getPlayerState() {
        return playerState;
    }

    public void setPlayerState(int playerState) {
        this.playerState = playerState;
    }

    public long getVideoProgress() {
        return videoProgress;
    }

    public void setVideoProgress(long videoProgress) {
        this.videoProgress = videoProgress;
    }
//    private int playURL;
//    private MediaPlayer mediaPlayer;
//    private int position;
//    public int progress;
//    public boolean thumb_up=false;
//
//    public PlayerInfo() {
//
//    }
//
//    public PlayerInfo(int playURL, MediaPlayer aliPlayer, int position) {
//        this.playURL = playURL;
//        this.mediaPlayer = aliPlayer;
//        this.position = position;
//    }
//
//    public int getPlayURL() {
//        return playURL;
//    }
//
//    public void setPlayURL(int playURL) {
//        this.playURL = playURL;
//    }
//
//    public int getPosition() {
//        return position;
//    }
//
//    public void setPosition(int position) {
//        this.position = position;
//    }
//
//    public MediaPlayer getMediaPlayer() {
//        return mediaPlayer;
//    }
//
//    public void setMediaPlayer(MediaPlayer aliPlayer) {
//        this.mediaPlayer = aliPlayer;
//    }
}
