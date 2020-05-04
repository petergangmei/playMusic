package com.petergangmei.playmusic.model;

public class Song {
    public Song() {
    }

    private String id, songName,singerName, singerId, songUrl;
    private long views, upVote, downVote, timestamp;

    public Song(String id, String songName, String singerName, String singerId, String songUrl, long views, long upVote, long downVote, long timestamp) {
        this.id = id;
        this.songName = songName;
        this.singerName = singerName;
        this.singerId = singerId;
        this.songUrl = songUrl;
        this.views = views;
        this.upVote = upVote;
        this.downVote = downVote;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerId() {
        return singerId;
    }

    public void setSingerId(String singerId) {
        this.singerId = singerId;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getUpVote() {
        return upVote;
    }

    public void setUpVote(long upVote) {
        this.upVote = upVote;
    }

    public long getDownVote() {
        return downVote;
    }

    public void setDownVote(long downVote) {
        this.downVote = downVote;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
