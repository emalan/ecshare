package com.madalla.bo.image;

public interface IAlbumData {

	String getId();

	String getName();

	String getTitle();

	void setTitle(String title);

	String getType();

	void setType(String type);

	Integer getHeight();

	void setHeight(Integer height);

	Integer getWidth();

	void setWidth(Integer width);

	Integer getInterval();

	void setInterval(Integer interval);
}
