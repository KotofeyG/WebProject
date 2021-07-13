package com.kotov.restaurant.controller;

import com.kotov.restaurant.command.PagePath;

public class Router {
    public enum RouteType {
        FORWARD, REDIRECT
    }

    private String pagePath = PagePath.INDEX_PAGE;
    private RouteType route = RouteType.FORWARD;

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public RouteType getRoute() {
        return route;
    }

    public void setRoute(RouteType route) {
        this.route = route;
    }
}