package com.epam.jwd.hrmanager.command.impl;

import com.epam.jwd.hrmanager.command.Command;
import com.epam.jwd.hrmanager.controller.CommandRequest;
import com.epam.jwd.hrmanager.controller.CommandResponse;
import com.epam.jwd.hrmanager.controller.PropertyContext;
import com.epam.jwd.hrmanager.controller.RequestFactory;
import com.epam.jwd.hrmanager.exception.FillingOutTheFormException;
import com.epam.jwd.hrmanager.exception.SessionParamException;
import com.epam.jwd.hrmanager.model.*;
import com.epam.jwd.hrmanager.secvice.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.Time;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class CreatingInterviewCommand implements Command {

    public static final String JOB_REQUEST_PARAM_NAME = "jobRequest";
    private static final String INTERVIEW_CREATION_PAGE = "page.interviewCreation";

    private static final String ERROR_FILLING_OUT_THE_FORM_PARAM = "errorFillingOutTheFormMessage";
    private static final String SHOW_JOB_REQUESTS_COMMAND = "/controller?command=jobRequestsPage";
    private static final String CITY_PARAM_NAME = "city";
    private static final String STREET_PARAM_NAME = "street";
    private static final String HOUSE_PARAM_NAME = "house";
    private static final String FLAT_PARAM_NAME = "flat";
    private static final String DATE_PARAM_NAME = "date";
    private static final String TIME_PARAM_NAME = "time";
    private static final String SECONDS = ":00";
    private static final String CITY_NAME_IS_EMPTY = "city name is empty";
    private static final String STREET_NAME_IS_EMPTY = "street name is empty";
    private static final String INCORRECT_HOSE_NUMBER = "incorrect hose number";
    private static final String INCORRECT_DATE = "incorrect date";
    private static final String INCORRECT_TIME = "incorrect time";
    private final static Logger LOGGER = LogManager.getLogger(SingInCommand.class);
    private static final ReentrantLock lock = new ReentrantLock();
    private static CreatingInterviewCommand instance;

    private final RequestFactory requestFactory;
    private final JobRequestService jobRequestService;
    private final InterviewService interviewService;
    private final CityService cityService;
    private final StreetService streetService;
    private final AddressService addressService;
    private final PropertyContext propertyContext;

    private CreatingInterviewCommand(RequestFactory requestFactory, JobRequestService jobRequestService,
                                     InterviewService interviewService, CityService cityService,
                                     StreetService streetService, AddressService addressService,
                                     PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.jobRequestService = jobRequestService;
        this.interviewService = interviewService;
        this.cityService = cityService;
        this.streetService = streetService;
        this.addressService = addressService;
        this.propertyContext = propertyContext;
    }


    static CreatingInterviewCommand getInstance(RequestFactory requestFactory, JobRequestService jobRequestService,
                                                InterviewService interviewService, CityService cityService,
                                                StreetService streetService, AddressService addressService,
                                                PropertyContext propertyContext) {
        if (instance == null) {
            lock.lock();
            {
                if (instance == null) {
                    instance = new CreatingInterviewCommand(requestFactory, jobRequestService, interviewService,
                            cityService, streetService, addressService, propertyContext);
                }
            }
            lock.unlock();
        }
        return instance;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            createInterview(request);
        } catch (SessionParamException e) {
            LOGGER.error(e);
            request.addAttributeToJsp("errorMessage", e.getMessage());
            return requestFactory.createForwardResponse(propertyContext.get(INTERVIEW_CREATION_PAGE));
        } catch (FillingOutTheFormException e) {
            LOGGER.error(e);
            request.addAttributeToJsp(ERROR_FILLING_OUT_THE_FORM_PARAM, e.getMessage());
            return requestFactory.createForwardResponse(propertyContext.get(INTERVIEW_CREATION_PAGE));
        }
        final Optional<Object> jobRequest = request.retrieveFromSession(JOB_REQUEST_PARAM_NAME);
        jobRequestService.update(((JobRequest) jobRequest.get()).withStatus(VacancyRequestStatus.APPROVED));
        return requestFactory.createRedirectResponse(SHOW_JOB_REQUESTS_COMMAND);
    }

    private void createInterview(CommandRequest request) throws SessionParamException, FillingOutTheFormException {
        final Optional<Object> jobRequest = request.retrieveFromSession(JOB_REQUEST_PARAM_NAME);
        if (!jobRequest.isPresent()) {
            throw new SessionParamException("Failed to retrieve job request from session");
        }
        final String cityName = request.getParameter(CITY_PARAM_NAME);
        final String streetName = request.getParameter(STREET_PARAM_NAME);
        final String houseNumber = request.getParameter(HOUSE_PARAM_NAME);
        final String flatNumber = request.getParameter(FLAT_PARAM_NAME);
        final String date = request.getParameter(DATE_PARAM_NAME);
        final String time = request.getParameter(TIME_PARAM_NAME) + SECONDS;

        Optional<String> exception = validate(cityName, streetName, houseNumber, flatNumber, date, time);
        if (exception.isPresent()) {
            throw new FillingOutTheFormException(exception.get());
        }

        final Address address = createAddress(cityName, streetName,
                Integer.parseInt(houseNumber), Integer.parseInt(flatNumber));
        interviewService.add(new Interview(InterviewStatus.PLANNED, address,
                ((JobRequest) jobRequest.get()).getAccount().getUser(),
                ((JobRequest) jobRequest.get()).getVacancy(),
                Date.valueOf(date),
                Time.valueOf(time))
        );
    }

    Optional<String> validate(String cityName, String streetName, String hoseNumber,
                              String flatNumber, String date, String time) {
        if (cityName == null || cityName.isEmpty()) {
            return Optional.of(CITY_NAME_IS_EMPTY);
        } else if (streetName == null || streetName.isEmpty()) {
            return Optional.of(STREET_NAME_IS_EMPTY);
        } else if (hoseNumber == null || hoseNumber.isEmpty()) {
            return Optional.of(INCORRECT_HOSE_NUMBER);
        }else if (flatNumber == null || flatNumber.isEmpty()) {
            return Optional.of(INCORRECT_HOSE_NUMBER);
        } else if (date == null || date.isEmpty()) {
            return Optional.of(INCORRECT_DATE);
        } else if (time == null || time.equals(SECONDS)) {
            return Optional.of(INCORRECT_TIME);
        } else {
            return Optional.empty();
        }
    }

    private Address createAddress(String cityName, String streetName, int houseNumber, Integer flatNumber) {
        final City city = cityService.add(new City(cityName));
        final Street street = streetService.add(new Street(streetName));
        return addressService.add(new Address(city, street, houseNumber, flatNumber));
    }
}
