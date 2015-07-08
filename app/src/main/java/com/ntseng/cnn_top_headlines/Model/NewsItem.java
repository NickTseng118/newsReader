package com.ntseng.cnn_top_headlines.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nicktseng on 15/6/25.
 */
@Table(name = "NewsItem")
public class NewsItem extends Model implements Serializable{

    @Column(name = "title")
    private String title;
    @Column(name = "guid")
    private String guid;
    @Column(name = "link")
    private String link;
    @Column(name = "description")
    private String description;
    @Column(name = "pubDate")
    private Date pubDate;
    @Column(name = "mediaThumbnail")
    private String mediaThumbnail;
    @Column(name = "mediaContent")
    private String mediaContent;
    @Column(name = "favorite")
    private Boolean favorite;

    public NewsItem(){
        super();
    }

    public  NewsItem(String title, String guid, String link, String description, Date pubDate, String mediaThumbnail, String mediaContent,Boolean favorite){
        super();
        this.title = title;
        this.guid = guid;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.mediaThumbnail = mediaThumbnail;
        this.mediaContent = mediaContent;
        this.favorite = favorite;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getMediaThumbnail() {
        return mediaThumbnail;
    }

    public void setMediaThumbnail(String mediaThumbnail) {
        this.mediaThumbnail = mediaThumbnail;
    }

    public String getMediaContent() {
        return mediaContent;
    }

    public void setMediaContent(String mediaContent) {
        this.mediaContent = mediaContent;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    
}
