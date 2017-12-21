package com.ascend.zkclient;

import java.io.Serializable;

public class RunningData implements Serializable {
    private static final long serialVersionUID = 8237929783769899854L;

    private Long cid;
    private String name;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RunningData{");
        sb.append("cid=").append(cid);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
