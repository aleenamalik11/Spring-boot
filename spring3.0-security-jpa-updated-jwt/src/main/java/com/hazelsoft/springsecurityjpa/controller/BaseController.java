package com.hazelsoft.springsecurityjpa.controller;

import com.hazelsoft.springsecurityjpa.dto.RequestResponse;
import com.hazelsoft.springsecurityjpa.dto.Status;

public interface BaseController {
    public RequestResponse prepareResponse(Object payload, Object errors, String msg, Status status);
    public void saveActivityAudit(String details);
}
