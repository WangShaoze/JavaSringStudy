package com.show.words.entity;


import java.io.Serializable;

/**
 * ws_words 单词信息
 * */
public class WsWord implements Serializable{
    /**
     * 单词对应的id
     * */
    private String id;

    /**
     * 单词
     * */
    private String word;

    /**
     * 单词对应的意思及词性
     * */
    private String mean;

    /**
     * 磁性列表
     * */
    private String ciXin;

    /**
     * 词义转化为json数据
     * */
    private String meanDetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getCiXin() {
        return ciXin;
    }

    public void setCiXin(String ciXin) {
        this.ciXin = ciXin;
    }

    public String getMeanDetail() {
        return meanDetail;
    }

    public void setMeanDetail(String meanDetail) {
        this.meanDetail = meanDetail;
    }

    @Override
    public String toString() {
        return "单词id: "+id+" 单词: "+word+" 词性:["+ciXin+"];";
    }
}