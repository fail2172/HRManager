package com.epam.jwd.hrmanager.tag;

import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.model.Account;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.Optional;

public class AuthorizedTag extends BodyTagSupport {

    private static final long serialVersionUID = 5307504689564917463L;

    private static final String SESSION_ACCOUNT_PROPERTY = "session.account";

    private static final PropertyContext propertyContext = PropertyContext.getInstance();

    private boolean auth;

    @Override
    public int doStartTag() throws JspException {
        if (checkAuthorized()) {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

    boolean checkAuthorized() {
        Optional<Account> account = Optional.ofNullable(pageContext.getSession())
                .map(s -> (Account) s.getAttribute(propertyContext.get(SESSION_ACCOUNT_PROPERTY)));
        return account.isPresent() == auth;
    }

    public void setAuth(String authorized) {
        this.auth = Boolean.parseBoolean(authorized);
    }
}
