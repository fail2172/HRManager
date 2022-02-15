package com.epam.jwd.hrmanager.tag;

import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.Role;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.Optional;

public class SecurityTag extends BodyTagSupport {

    private static final long serialVersionUID = -2026185793034269080L;

    private static final String SESSION_ACCOUNT_PROPERTY = "session.account";

    private static final PropertyContext propertyContext = PropertyContext.getInstance();

    private int level;

    @Override
    public int doStartTag() throws JspException {
        if (checkSecurityLevel()) {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

    private boolean checkSecurityLevel() {
        Optional<Integer> securityLevel = Optional.ofNullable(pageContext.getSession())
                .map(s -> (Account) s.getAttribute(propertyContext.get(SESSION_ACCOUNT_PROPERTY)))
                .map(Account::getRole)
                .map(Role::getSecurityLevel);
        return securityLevel.isPresent() && securityLevel.get() >= level;
    }

    public void setLevel(String level) {
        this.level = Integer.parseInt(level);
    }
}
