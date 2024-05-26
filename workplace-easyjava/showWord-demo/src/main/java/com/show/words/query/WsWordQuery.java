package com.show.words.query;

import com.show.words.utils.BaseQuery;

public class WsWordQuery extends BaseQuery {
    /**
     * 单词对应的id
     * */
    private String id;

    /**
     * 单词
     * */
    private String word;
    private String wordFuzzy; // 单词支持模糊查询



    /**
     * 单词对应的意思及词性
     * */
    private String mean;
    private String meanFuzzy;  // 支持模糊查询

    /**
     * 磁性列表
     * */
    private String ciXin;
    private String ciXinFuzzy;  // 支持模糊查询

    /**
     * 词义转化为json数据
     * */
    private String meanDetail;

    public String getId(){
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

    public String getWordFuzzy() {
        return wordFuzzy;
    }

    public void setWordFuzzy(String wordFuzzy) {
        this.wordFuzzy = wordFuzzy;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getMeanFuzzy() {
        return meanFuzzy;
    }

    public void setMeanFuzzy(String meanFuzzy) {
        this.meanFuzzy = meanFuzzy;
    }

    public String getCiXin() {
        return ciXin;
    }

    public void setCiXin(String ciXin) {
        this.ciXin = ciXin;
    }

    public String getCiXinFuzzy() {
        return ciXinFuzzy;
    }

    public void setCiXinFuzzy(String ciXinFuzzy) {
        this.ciXinFuzzy = ciXinFuzzy;
    }

    public String getMeanDetail() {
        return meanDetail;
    }

    public void setMeanDetail(String meanDetail) {
        this.meanDetail = meanDetail;
    }
}
