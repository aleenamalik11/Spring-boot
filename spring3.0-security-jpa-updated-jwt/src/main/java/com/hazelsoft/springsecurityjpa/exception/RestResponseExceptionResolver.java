package com.hazelsoft.springsecurityjpa.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelsoft.springsecurityjpa.enums.Status;
import com.hazelsoft.springsecurityjpa.model.RequestResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import java.io.IOException;
@Component
public class RestResponseExceptionResolver extends AbstractHandlerExceptionResolver {
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
                                              Object handler, Exception ex) {

        try{
            if (ex instanceof CustomException) {
                return handleException(ex.getMessage(), (CustomException) ex, response);
            }
            else if(ex instanceof ExpiredJwtException){
                return handleException(ex.getMessage(), (ExpiredJwtException)ex, response);
            }
            else if(ex instanceof SignatureException){
                return handleException(ex.getMessage(), (SignatureException)ex, response);
            }

        } catch (Exception handlerException) {
            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
        }
       return null;
    }

    private <T> ModelAndView handleException(String msg, T ex, HttpServletResponse response) throws IOException {
        RequestResponse errorResponse = new RequestResponse(Status.ERROR,
                msg, null, ex);
        response.setContentType("application/json");
        response.setStatus(500);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        return new ModelAndView();
    }
}
