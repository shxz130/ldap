package com.ldap;

import com.qingting.model.Context;

/**
 * Created by root on 2/1/16.
 */

//接口定义，这里是为了方便切换openldap 和 AD


public interface iManage {
    public void createUser(User user);
    public void deleteUser(String name, String ou);
    public User getUserByName(String name, String ou);
    public void updateUserAttribute(User user);
    public void getObjList(String ou);
    public boolean checkConnected();
    public boolean login(Context ctx);
    public void getUserList();
}
