package edu.ucar.cisl.sagesiparcsmetadatasearch.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class MetadataErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = Integer.parseInt(status.toString());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("statusCode",statusCode);

        return modelAndView;
    }

    @Override
    public String getErrorPath() {

        return "/error";
    }
}
