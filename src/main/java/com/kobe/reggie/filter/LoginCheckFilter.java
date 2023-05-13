package com.kobe.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kobe.reggie.common.BaseContext;
import com.kobe.reggie.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns ="/*" )
public class LoginCheckFilter implements Filter {
    public  static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        String requestURL = request.getRequestURI();
        //注意getrui和url的区别
        log.info("filter the request{}",requestURL);
        String urls[]=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"

        };
        boolean check=check(urls,requestURL);
        if(check){
            log.info("{} request no need filter",requestURL);
            filterChain.doFilter(request,response);
            return;
        }
        if(request.getSession().getAttribute("Emp")!=null){
            Long empid=(Long)request.getSession().getAttribute("Emp");
            BaseContext.setCurrentId(empid);
            log.info("user loged");
            filterChain.doFilter(request,response);
            return;
        }
        if(request.getSession().getAttribute("user")!=null){
            Long user=(Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(user);
            log.info("muser loged");
            filterChain.doFilter(request,response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }
    public boolean check(String urls[],String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match==true){
                return true;
            }
        }
                return false;
    }
}
