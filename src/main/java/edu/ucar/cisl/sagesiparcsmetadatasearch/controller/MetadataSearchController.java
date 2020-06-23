package edu.ucar.cisl.sagesiparcsmetadatasearch.controller;

import edu.ucar.cisl.sagesiparcsmetadatasearch.repository.MetadataSearchRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Controller
public class MetadataSearchController {

    private MetadataSearchRepository metadataSearchRepository;

    public MetadataSearchController(MetadataSearchRepository metadataSearchRepository) {
        this.metadataSearchRepository = metadataSearchRepository;
    }

    @RequestMapping("/")
    public String index() {

        return "redirect:/search.html";
    }

    @RequestMapping(value = "/search.html", method = RequestMethod.GET)
    public String SearchHomePage() {

        return "index";
    }

    @RequestMapping(value = "/search.html", params = "q", method = RequestMethod.GET)
    public ModelAndView results(@RequestParam("q") String query) {

        ModelAndView modelAndView = new ModelAndView("search");
        modelAndView.addObject("results", this.metadataSearchRepository.getQueryResults(query));
        modelAndView.addObject("query", query);
        return modelAndView;
    }
}


