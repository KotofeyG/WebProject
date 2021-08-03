package com.kotov.restaurant.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.kotov.restaurant.controller.command.ParamName.CURRENT_PAGE;

@WebFilter(urlPatterns = {"*.jsp"}, dispatcherTypes = {DispatcherType.FORWARD}
        , initParams = {@WebInitParam(name = "PAGES_ROOT_DIRECTORY", value = "/jsp", description = "Pages Param")
        , @WebInitParam(name = "INDEX_PAGE", value = "/index.jsp", description = "Pages Param")})
public class CurrentPageFilter implements Filter {
    private String root;
    private String indexPage;

    @Override
    public void init(FilterConfig filterConfig) {
        root = filterConfig.getInitParameter("PAGES_ROOT_DIRECTORY");
        indexPage = filterConfig.getInitParameter("INDEX_PAGE");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String currentPage = indexPage;
        int rootFirstIndex = requestURI.indexOf(root);
        if (rootFirstIndex != -1) {
            currentPage = requestURI.substring(rootFirstIndex);
        }
        httpRequest.getSession().setAttribute(CURRENT_PAGE, currentPage);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}