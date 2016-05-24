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
     * ��½�Ƿ�ɹ�
     * @param ctx
     * @return
     */
    boolean login(Context ctx);

    /**
     * ����һ���û�
     * @param user
     */
    boolean createUser(User user);

    /**
     * ɾ���û�
     * @param name
     * @param ou
     */
    boolean deleteUser(String name, String ou);

    /**
     * �������ƻ�ȡ�û�
     * @param name
     * @param ou
     * @return
     */
    LdapUser getUserByName(String name, String ou);

    /**
     * �޸��û�������ֵ
     * @param user
     */
    boolean updateUserAttribute(User user);

    /**
     * ��ȡ�û��б����޸ģ�
     * @param ou
     */
    void getObjList(String ou);

    /**
     * ����Ƿ��½�ɹ�
     * @return
     */
    boolean checkConnected();

    /**
     * ����һ����֯
     * @param ou
     * @return
     */
    boolean createOu(String ou);

    /**
     * ��ȡ�û��б���δʵ��
     */
    List<LdapUser> getUserList();
}
