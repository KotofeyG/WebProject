package com.kotov.restaurant.controller.tag;

import com.kotov.restaurant.model.entity.Meal;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Denis Kotov
 *
 * The type Order total cost tag.
 */
public class OrderTotalCostTag extends TagSupport {
    private Map<Meal, Integer> mealList;

    public Map<Meal, Integer> getMealList() {
        return mealList;
    }

    public void setMealList(Map<Meal, Integer> mealList) {
        this.mealList = mealList;
    }

    @Override
    public int doStartTag() throws JspException {
        BigDecimal totalCost = BigDecimal.valueOf(0);
        for (Map.Entry<Meal, Integer> meal : mealList.entrySet()) {
            totalCost = totalCost.add(meal.getKey().getPrice().multiply(BigDecimal.valueOf(meal.getValue())));
        }
        JspWriter out = pageContext.getOut();
        try {
            out.write(totalCost.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }
}