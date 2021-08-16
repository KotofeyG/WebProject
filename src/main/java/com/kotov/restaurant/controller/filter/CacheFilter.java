//package com.kotov.restaurant.controller.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//@WebFilter("/*")
//public class CacheFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
//        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//        httpResponse.setHeader("Pragma", "no-cache");
//        httpResponse.setDateHeader("Expires", 0);
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}