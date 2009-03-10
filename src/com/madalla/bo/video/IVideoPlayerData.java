package com.madalla.bo.video;

public interface IVideoPlayerData {

    String getId();

    String getName();

    String getVideoId();
    
    void setVideoId(String videoId);
    
    int getWidth();
    
    void setWidth(int width);
    
    int getHeight();
    
    void setHeight(int height);
    
    int getStartSeconds();
    
    void setStartSeconds(int startSeconds);

}
