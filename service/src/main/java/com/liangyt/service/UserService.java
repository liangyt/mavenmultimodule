package com.liangyt.service;

import com.liangyt.entity.User;

/**
 * 描述：接口
 * 创建时间 2017-12-27 16:04
 * 作者 liangyongtong
 */
public interface UserService {
    void save(User user);

    default public void sayHello() {
        System.out.println("Hello");
    }
}
