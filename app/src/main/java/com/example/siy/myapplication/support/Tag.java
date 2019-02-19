package com.example.siy.myapplication.support;


import java.io.Serializable;

/**
 * Created by Siy on 2018/12/28.
 *
 * @author Siy
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = 6808547720134912816L;

    /**
     * 标签赞同数
     */

    private int count;

    /**
     * 标签id
     */

    private int tagId;

    /**
     * 标签名称
     */
    private String tagName;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
