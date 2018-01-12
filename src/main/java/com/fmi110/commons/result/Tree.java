package com.fmi110.commons.result;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * 前端 tree 属性菜单使用的数据
 */
public class Tree implements Serializable {
    private Long   id;
    private String text;
    private String  state   = "open";// open , closed
    private Boolean checked = false; // 默认不勾选
    /**
     * 节点属性,这里时资源对应的url
     */
    private Object     attributes;
    @JsonInclude(JsonInclude.Include.NON_NULL) // null 不转为json
    private List<Tree> children;
    private String     iconCls;
    private Long       pid; // 父节点

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(Integer opened) {
        this.state = (null!=opened&&opened==1)?"open":"closed";
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Tree{" +
               "id=" + id +
               ", text='" + text + '\'' +
               ", attributes=" + attributes +
               ", children=" + children +
               ", pid=" + pid +
               '}';
    }
}
