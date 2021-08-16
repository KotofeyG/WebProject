package com.kotov.restaurant.tag;

import com.kotov.restaurant.model.entity.Address;
import com.kotov.restaurant.model.entity.User;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

public class ClientInfoTag extends TagSupport {
    private static final String FIRST_NAME = "";
    private static final String PATRONYMIC = "";
    private static final String LAST_NAME = "";

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder view = new StringBuilder();


        JspWriter out = pageContext.getOut();
        try {
            out.write(view.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}