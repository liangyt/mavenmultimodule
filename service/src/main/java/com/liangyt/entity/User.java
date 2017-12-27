package com.liangyt.entity;

import java.io.Serializable;

/**
 * 描述：实体
 * 创建时间 2017-12-27 16:01
 * 作者 liangyongtong
 */
public class User implements Serializable{
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
