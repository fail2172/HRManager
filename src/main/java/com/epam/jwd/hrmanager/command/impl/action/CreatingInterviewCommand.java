package com.epam.jwd.hrmanager.command.impl.action;

import com.epam.jwd.hrmanager.command.Authorized;
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

    private static final String SESSION_ERROR_MESSAGE_PROPERTY = "session.error_message";
    private static final String SESSION_VARIABLE_PROPERTY = "session.variable";

    private static final String COMMAND_NOT_FOUND_ERROR_PROPERTY = "/controller?command=notFoundError";
    private static final String COMMAND_INTERVIEW_CREATION_PAGE_PROPERTY = "command/interview_creation_page";
    private static final String COMMAND_JOB_REQUESTS_PAGE_PROPERTY = "command/job_requests_page";

    private static final String CITY_NAME_IS_EMPTY = "city name is empty";
    private static final String STREET_NAME_IS_EMPTY = "street name is empty";
    private static final String INCORRECT_HOSE_NUMBER = "incorrect hose number";
    private static final String INCORRECT_FLAT_NUMBER = "incorrect flat number";
    private static final String INCORRECT_DATE = "incorrect date";
    private static final String INCORRECT_TIME = "incorrect time";

    private static final String CITY_PARAM_NAME = "city";
    private static final String STREET_PARAM_NAME = "street";
    private static final String HOUSE_PARAM_NAME = "house";
    private static final String FLAT_PARAM_NAME = "flat";
    private static final String DATE_PARAM_NAME = "date";
    private static final String TIME_PARAM_NAME = "time";
    private static final String SECONDS = ":00";
    private static final int ZERO = 0;

    private static final Logger LOGGER = LogManager.getLogger(CreatingInterviewCommand.class);
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


    public static CreatingInterviewCommand getInstance(RequestFactory requestFactory, JobRequestService jobRequestService,
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
    @Authorized
    public CommandResponse execute(CommandRequest request) {
        try {
            createInterview(request);
        } catch (SessionParamException e) {
            LOGGER.error(e);
            return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_NOT_FOUND_ERROR_PROPERTY));
        } catch (FillingOutTheFormException e) {
            LOGGER.error(e);
            request.addToSession(propertyContext.get(SESSION_ERROR_MESSAGE_PROPERTY), e.getMessage());
            return requestFactory.createRedirectResponse(propertyContext.get(COMMAND_INTERVIEW_CREATION_PAGE_PROPERTY));
        }
        updateJobRequest(request);
        return requestFactory.createRedirectResponse(
                propertyContext.get(COMMAND_JOB_REQUESTS_PAGE_PROPERTY)
        );
    }

    private void createInterview(CommandRequest request) throws SessionParamException, FillingOutTheFormException {
        final Optional<Object> jobRequest = request.retrieveFromSession(propertyContext.get(SESSION_VARIABLE_PROPERTY));
        if (!jobRequest.isPresent()) {
            throw new SessionParamException("Failed to retrieve job request from session");
        }
        final String cityName = request.getParameter(CITY_PARAM_NAME);
        final String streetName = request.getParameter(STREET_PARAM_NAME);
        final String houseNumber = request.getParameter(HOUSE_PARAM_NAME);
        final String flatNumber = request.getParameter(FLAT_PARAM_NAME);
        final String date = request.getParameter(DATE_PARAM_NAME);
        final String time = request.getParameter(TIME_PARAM_NAME) + SECONDS;

        validate(cityName, streetName, houseNumber, flatNumber, date, time);

        final Address address = createAddress(cityName, streetName,
                Integer.parseInt(houseNumber), Integer.parseInt(flatNumber));
        interviewService.add(new Interview(InterviewStatus.PLANNED, address,
                ((JobRequest) jobRequest.get()).getAccount().getUser(),
                ((JobRequest) jobRequest.get()).getVacancy(),
                Date.valueOf(date),
                Time.valueOf(time))
        );
    }

    private void validate(String cityName, String streetName, String hoseNumber,
                          String flatNumber, String date, String time) throws FillingOutTheFormException {
        if (cityName == null || cityName.isEmpty()) {
            throw new FillingOutTheFormException(CITY_NAME_IS_EMPTY);
        } else if (streetName == null || streetName.isEmpty()) {
            throw new FillingOutTheFormException(STREET_NAME_IS_EMPTY);
        } else if (hoseNumber == null || hoseNumber.isEmpty()) {
            throw new FillingOutTheFormException(INCORRECT_HOSE_NUMBER);
        } else if (Integer.parseInt(hoseNumber) <= ZERO) {
            throw new FillingOutTheFormException(INCORRECT_HOSE_NUMBER);
        } else if (flatNumber == null || flatNumber.isEmpty()) {
            throw new FillingOutTheFormException(INCORRECT_FLAT_NUMBER);
        } else if (Integer.parseInt(flatNumber) <= 0) {
            throw new FillingOutTheFormException(INCORRECT_FLAT_NUMBER);
        } else if (date == null || date.isEmpty()) {
            throw new FillingOutTheFormException(INCORRECT_DATE);
        } else if (time == null || time.equals(SECONDS)) {
            throw new FillingOutTheFormException(INCORRECT_TIME);
        }
    }

    private Address createAddress(String cityName, String streetName, int houseNumber, Integer flatNumber) {
        final City city = cityService.add(new City(cityName));
        final Street street = streetService.add(new Street(streetName));
        return addressService.add(new Address(city, street, houseNumber, flatNumber));
    }

    private void updateJobRequest(CommandRequest request) {
        final Optional<Object> jobRequest = request.retrieveFromSession(propertyContext.get(SESSION_VARIABLE_PROPERTY));
        if (jobRequest.isPresent()) {
            jobRequestService.update(((JobRequest) jobRequest.get()).withStatus(JobRequestStatus.APPROVED));
            request.removeFromSession(propertyContext.get(SESSION_VARIABLE_PROPERTY));
        }
    }
}
