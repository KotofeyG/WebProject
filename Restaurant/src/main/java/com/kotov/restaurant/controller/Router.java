package com.kotov.restaurant.controller;

import com.kotov.restaurant.controller.command.PagePath;

/**
 * @author Denis Kotov
 *
 * The {@link Router} class contains four fields:
 * pagePath
 * routeType
 * errorCode
 * errorMessage
 * that are used with by controller to find out where and how
 * request and response should be processed after the controller.
 */
public class Router {
    public enum RouteType {
        FORWARD, REDIRECT
    }

    private String pagePath = PagePath.INDEX_PAGE;
    private RouteType routerType = RouteType.FORWARD;
    private Integer errorCode;
    private String errorMessage;

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public RouteType getRouterType() {
        return routerType;
    }

    public void setRouterType(RouteType routerType) {
        this.routerType = routerType;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public boolean hasError() {
        return errorCode != null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}