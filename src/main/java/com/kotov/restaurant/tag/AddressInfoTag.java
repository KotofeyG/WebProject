package com.kotov.restaurant.tag;

import com.kotov.restaurant.model.entity.Address;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

public class AddressInfoTag extends TagSupport {
    private static final String SEPARATOR = ", ";
    private static final String CITY = "г.";
    private static final String STREET = "ул.";
    private static final String BUILDING = "д.";
    private static final String BLOCK = "К";
    private static final String FLAT = "кв.";
    private static final String ENTRANCE = "под.";
    private static final String FLOOR = "этаж ";
    private static final String INTERCOM_CODE = "домофон ";

    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder view = new StringBuilder();
        view.append(CITY).append(address.getCity().getRussianName()).append(SEPARATOR).append(STREET).append(address.getStreet())
                .append(SEPARATOR).append(BUILDING).append(address.getBuilding());
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