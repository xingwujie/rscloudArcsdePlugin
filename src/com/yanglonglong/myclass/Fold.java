package com.yanglonglong.myclass;

import java.util.ArrayList;

public class Fold {
    private String id;
    private String text;
    private String uid;
    private String pid;
    private String type;
    private String level;
    private ArrayList<Fold> children;

    public ArrayList getChildren() {
        return children;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getPid() {
        return pid;
    }

    public String getType() {
        return type;
    }

    public String getUid() {
        return uid;
    }

    public String getLevel() {
        return level;
    }

    public void setChildren(ArrayList children) {
        this.children = children;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
