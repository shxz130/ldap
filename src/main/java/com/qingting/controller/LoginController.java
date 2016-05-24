package com.qingting.controller;

import com.qingting.model.Context;
import com.qingting.service.BaseLdapManager;
import com.qingting.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/5/23.
 */
@Controller
@RequestMapping("login")
public class LoginController {
    @Autowired
    private BaseLdapManager baseLdapManager;

    private static String BASE_URL="ldap/";

    @RequestMapping(value = "goLogin.shtml",method = { RequestMethod.POST, RequestMethod.GET })
    public String goLogin(){
        return BASE_URL+"login";
    }

    @RequestMapping(value = "login.shtml",method = { RequestMethod.POST, RequestMethod.GET })
    public String login(String userName,String password,String URL,String base,HttpServletRequest request,HttpServletResponse response){
        Context context=new Context(userName,password,URL,base);
        if(baseLdapManager.login(context)){
            request.getSession().setAttribute(Constants.SESSION_KEY,"认证成功");
            return "redirect:../admin/init.shtml";
        }else{
            return "redirect:/goLogin.shtml";
        }
    }


    @RequestMapping(value = "goLoginOut.shtml",method = { RequestMethod.POST, RequestMethod.GET })
    public String loginOut(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute(Constants.SESSION_KEY);
        return BASE_URL+"login";
    }
}
