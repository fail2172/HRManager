package com.epam.jwd.hrmanager.controller;

import com.epam.jwd.hrmanager.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class Controller extends HttpServlet {

    private static final long serialVersionUID = -1303640745642398238L;
    private static final Logger LOGGER = LogManager.getLogger(Controller.class);
    public static final String COMMAND_NAME_PARAM = "command";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        LOGGER.trace("caught req and resp in doGet method");
        processRequest(req, resp);
        main(req);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        LOGGER.trace("caught req and resp in doPost method");
        processRequest(req, resp);
        main(req);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        final String commandName = req.getParameter(COMMAND_NAME_PARAM);
        LOGGER.info("Command execution: {}", commandName);
        final Command command = Command.of(commandName);
        final CommandRequest commandRequest = requestFactory.createRequest(req);
        final CommandResponse commandResponse = command.execute(commandRequest);
        proceedWithResponse(req, resp, commandResponse);
    }

    private void proceedWithResponse(HttpServletRequest req, HttpServletResponse resp, CommandResponse commandResponse) {
        try {
            forwardOrRedirectToResponseLocation(req, resp, commandResponse);
        } catch (ServletException e) {
            LOGGER.error("Servlet exception occurred", e);
        } catch (IOException e) {
            LOGGER.error("IO exception occurred", e);
        }
    }

    private void forwardOrRedirectToResponseLocation(HttpServletRequest req, HttpServletResponse resp,
                                                     CommandResponse commandResponse) throws IOException, ServletException {
        if (commandResponse.isRedirect()) {
            resp.sendRedirect(commandResponse.getPath());
        } else {
            final String desiredPath = commandResponse.getPath();
            final RequestDispatcher dispatcher = req.getRequestDispatcher(desiredPath);
            dispatcher.forward(req, resp);
        }
    }

    private void main(HttpServletRequest request){
//        VacancyService service = (VacancyService) ServiceFactory.getInstance().serviceFor(Vacancy.class);
//        EmployerService employerService = (EmployerService) ServiceFactory.getInstance().serviceFor(Employer.class);
//        CityService cityService = (CityService) ServiceFactory.getInstance().serviceFor(City.class);
//
//        final Employer employer = employerService.add(new Employer("Группа Компаний Армтек", ""));
//        final City city = cityService.add(new City("Минск"));
//
//        Vacancy vacancy = new Vacancy("Комплектовщик товара", new BigDecimal(2000), employer, city, Employment.PART_TIME_EMPLOYMENT,1,
//                "Комплектование заказов согласно заявкам. Отбор товара на местах хранения, укладка в контейнеры, передача для последующей обработки. Раскладка товара на местах...\n" +
//                        "Ищем к себе в команду таких же Энергичных, Позитивных и Нацеленных на результат ! Рассмотрим кандидатов без опыта работы, но желающих...");
//        System.out.println(service.add(vacancy));
        System.out.println(request.getParameter("date"));
        System.out.println(request.getParameter("time"));
    }
}
