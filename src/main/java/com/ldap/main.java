package com.ldap;


import com.qingting.model.Context;

/**
 * Created by root on 4/15/16.
 */
public class main {
    public static void main(String[] args)throws Exception{


//这里是openldap的操作
        //因为openldapManage 是从iManage接口派生的，可以直接new 一个openldapManage对象。
//        iManage opm = new openldapManage();
        //Context 是用来封装登陆所需要对参数。
//        Context loginCtx = new Context("admin", "leaboy", "192.168.0.244", "DC=test,DC=com");

        //登陆验证
//        if (!opm.login(loginCtx)){
//            System.out.println("connect to ad server failed!");
//            System.exit(0);
//        }

        //现在分别对创建、删除、查找、更新用户做测试。
        //1. 创建用户
//        User u = new User("new", "");
//        u.setPwd("password");
//        opm.createUser(u);

        //2.删除用户
//        opm.deleteUser("new","");

        //3.查找用户
//        User retUser = opm.getUserByName("new","");
//        System.out.println("##############"+retUser.getOrgnization());

        //4.更新用户属性
//        User u = new User("new","");
//        u.setPwd("newpassword");
//        opm.updateUserAttribute(u);

        //5.获取同一层目录下对所有元素，包括用户(cn)、组织单元(ou)
//        opm.getObjList("");





//以下是关于Windows 2008 AD 的操作。
        Context loginCtx = new Context("admin", "Jhadm1n", "192.168.217.128", "DC=test,DC=com");
        openldapManage adm = new openldapManage();
        if (!adm.login(loginCtx)){
            System.out.println("connect to ad server failed!");
            System.exit(0);
        }else{



            User u = new User("yan", "");
            u.setPwd("mim");
            adm.createUser(u);
////        adm.deleteUser("new", "");
//            adm.updateUserAttribute(u);
//            adm.getUserList();
        }


    }
}
