package com.epam.jwd.hrmanager.command.impl.page;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;

import java.util.concurrent.locks.ReentrantLock;

public class ShowVacancyCreationCommand implements Command {

    private static final String VACANCY_CREATION_PAGE = "page.vacancyCreation";

    private static final ReentrantLock lock = new ReentrantLock();
    private static ShowVacancyCreationCommand instance;

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    private ShowVacancyCreationCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    public static ShowVacancyCreationCommand getInstance(RequestFactory requestFactory, PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new ShowVacancyCreationCommand(requestFactory, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(VACANCY_CREATION_PAGE));
    }
}
