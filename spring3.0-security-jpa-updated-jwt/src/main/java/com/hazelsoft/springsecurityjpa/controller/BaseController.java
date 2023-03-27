package com.hazelsoft.springsecurityjpa.controller;

import com.hazelsoft.springsecurityjpa.model.RequestResponse;
import com.hazelsoft.springsecurityjpa.enums.Status;

public abstract class BaseController<T, W>{

    private RequestResponse requestResponse;

    protected BaseController(RequestResponse requestResponse){
        this.requestResponse = requestResponse;
    }

    protected RequestResponse prepareResponse(T payload, W errors, String msg, Status status) {
        requestResponse.setErrors(errors);
        requestResponse.setMessage(msg);
        requestResponse.setPayload(payload);
        requestResponse.setStatus(status);

        return requestResponse;
    }

}
