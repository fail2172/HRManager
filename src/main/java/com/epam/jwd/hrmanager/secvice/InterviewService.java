package com.epam.jwd.hrmanager.secvice;

import com.epam.jwd.hrmanager.model.Interview;
import com.epam.jwd.hrmanager.model.User;

import java.util.List;

public interface InterviewService extends EntityService<Interview> {

    List<Interview> findInterviewsByUser(User user);

}
