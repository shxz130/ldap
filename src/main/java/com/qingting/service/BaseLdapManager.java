package com.qingting.service;

import com.ldap.User;
import com.qingting.model.Context;
import com.qingting.model.LdapUser;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/23.
 */
public interface BaseLdapManager {
    /**
     * 登陆是否成功
     * @param ctx
     * @return
     */
    boolean login(Context ctx);

    /**
     * 创建一个用户
     * @param user
     */
    boolean createUser(User user);

    /**
     * 删除用户
     * @param name
     * @param ou
     */
    boolean deleteUser(String name, String ou);

    /**
     * 根据名称获取用户
     * @param name
     * @param ou
     * @return
     */
    LdapUser getUserByName(String name, String ou);

    /**
     * 修改用户的属性值
     * @param user
     */
    boolean updateUserAttribute(User user);

    /**
     * 获取用户列表（待修改）
     * @param ou
     */
    void getObjList(String ou);

    /**
     * 检查是否登陆成功
     * @return
     */
    boolean checkConnected();

    /**
     * 创建一个组织
     * @param ou
     * @return
     */
    boolean createOu(String ou);

    /**
     * 获取用户列表，暂未实现
     */
    List<LdapUser> getUserList();
}
