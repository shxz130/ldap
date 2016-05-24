package com.qingting.filter;

import com.qingting.service.BaseLdapManager;
import com.qingting.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2016/5/23.
 */
public class LoginFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request= (HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        if(request.getServletPath().indexOf("login")>0||request.getSession().getAttribute(Constants.SESSION_KEY)!=null){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            response.sendRedirect("/login/goLogin.shtml");
        }
    }

    public void destroy() {

    }
}
