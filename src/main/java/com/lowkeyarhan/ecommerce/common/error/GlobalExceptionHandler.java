package com.lowkeyarhan.ecommerce.common.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(annotations=Controller.class)
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView notFound(ResourceNotFoundException ex) { return error(HttpStatus.NOT_FOUND, "Record not found", ex.getMessage()); }
    @ExceptionHandler({BusinessRuleException.class, IllegalArgumentException.class})
    public ModelAndView badRequest(RuntimeException ex) { return error(HttpStatus.BAD_REQUEST, "Action could not be completed", ex.getMessage()); }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView conflict(DataIntegrityViolationException ex) { return error(HttpStatus.CONFLICT, "Data conflict", "A unique value or linked record already exists. Review the record and try again."); }
    private ModelAndView error(HttpStatus status, String title, String message) {
        ModelAndView view = new ModelAndView("error/error"); view.setStatus(status); view.addObject("status", status.value());
        view.addObject("title", title); view.addObject("message", message == null ? "The request could not be completed." : message); return view;
    }
}
