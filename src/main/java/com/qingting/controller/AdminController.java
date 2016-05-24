package com.qingting.controller;

import com.ldap.User;
import com.qingting.model.AjaxResponse;
import com.qingting.model.LdapUser;
import com.qingting.service.BaseLdapManager;
import com.qingting.utils.Constants;
import com.qingting.utils.ResponseUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shxz130 on 2015/3/5.
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private BaseLdapManager baseLdapManager;

    private static String BASE_URL="ldap/";

    private static Logger LOG= Logger.getLogger(AdminController.class);


    @RequestMapping(value = "init.shtml",method = { RequestMethod.POST, RequestMethod.GET })
    public String home(Model model){

        return BASE_URL+"index";
    }

    @RequestMapping(value = "goUserList.shtml",method = { RequestMethod.POST, RequestMethod.GET })
    public String getUserListPage(Model model,String givenName){
        List<LdapUser> userList=new ArrayList<LdapUser>();
        if(givenName==null || givenName ==""){
          userList=baseLdapManager.getUserList();
        }else{
            LdapUser ldapUser= baseLdapManager.getUserByName(givenName, "");
            userList.add(ldapUser);
        }
        model.addAttribute("users",userList);
        return BASE_URL+"userList";
    }
    @RequestMapping(value = "goUserAdd.shtml",method = { RequestMethod.POST, RequestMethod.GET })
    public String goUserAdd(Model model){
        List<LdapUser> userList=baseLdapManager.getUserList();
        model.addAttribute("users",userList);
        return BASE_URL+"userAdd";
    }


    @RequestMapping(value = "userAdd.shtml",method = { RequestMethod.POST, RequestMethod.GET })
    public void userAdd(String givenName,String password,HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ajaxResponse=new AjaxResponse();
        if(givenName==null || password==null){
            ajaxResponse.setCode(Constants.AJAX_FAIL_CODE);
            ajaxResponse.setMemo(Constants.AJAX_FAIL_MEMO);
            try {
                ResponseUtils.write(response,ajaxResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            User user=new User(givenName,"");
            user.setPwd(password);
            if(baseLdapManager.createUser(user)){
                ajaxResponse.setCode(Constants.AJAX_SUCCESS_CODE);
                ajaxResponse.setMemo(Constants.AJAX_SUCCESS_MEMO);
                try {
                    ResponseUtils.write(response,ajaxResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                ajaxResponse.setCode(Constants.AJAX_FAIL_CODE);
                ajaxResponse.setMemo(Constants.AJAX_FAIL_MEMO);
                try {
                    ResponseUtils.write(response,ajaxResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "userDelete.shtml",method = { RequestMethod.POST, RequestMethod.GET })
    public void goUserAdd(String givenName,HttpServletRequest request,HttpServletResponse response){
        AjaxResponse ajaxResponse=new AjaxResponse();
        if(givenName==null){
            ajaxResponse.setCode(Constants.AJAX_FAIL_CODE);
            ajaxResponse.setMemo(Constants.AJAX_FAIL_MEMO);
            try {
                ResponseUtils.write(response,ajaxResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(baseLdapManager.deleteUser(givenName,"")){
            ajaxResponse.setCode(Constants.AJAX_SUCCESS_CODE);
            ajaxResponse.setMemo(Constants.AJAX_SUCCESS_MEMO);
            try {
                ResponseUtils.write(response,ajaxResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            ajaxResponse.setCode(Constants.AJAX_FAIL_CODE);
            ajaxResponse.setMemo(Constants.AJAX_FAIL_MEMO);
            try {
                ResponseUtils.write(response,ajaxResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
