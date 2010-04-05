package com.madalla.cms.bo.impl.ocm.video;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.page.IPageData;
import com.madalla.bo.video.VideoPlayerData;

@Node
public class VideoPlayer extends VideoPlayerData {
    
    private static final long serialVersionUID = 1L;

    @Field(path=true) private String id;
    @Field private String videoId;
    @Field private int width;
    @Field private int height;
    @Field private int startSeconds;
    
    public VideoPlayer(){
        
    }
    
    public VideoPlayer(final IPageData page, final String name){
        this.id = page.getId() + "/" + name;
    }
    
    public String getName(){
        return StringUtils.substringAfterLast(getId(), "/");
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getVideoId() {
        return videoId;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getStartSeconds() {
        return startSeconds;
    }
    public void setStartSeconds(int startSeconds) {
        this.startSeconds = startSeconds;
    }
    

}
