package com.epam.jwd.hrmanager.tag;

import com.epam.jwd.hrmanager.model.Account;
import com.epam.jwd.hrmanager.model.Role;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.Optional;

public class SecurityTag extends BodyTagSupport {

    private static final long serialVersionUID = -2793591976067340973L;

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
                .map(s -> (Account) s.getAttribute("sessionAccount"))
                .map(Account::getRole)
                .map(Role::getSecurityLevel);
        return securityLevel.isPresent() && securityLevel.get() >= level;
    }

    public void setLevel(String level) {
        this.level = Integer.parseInt(level);
    }
}
