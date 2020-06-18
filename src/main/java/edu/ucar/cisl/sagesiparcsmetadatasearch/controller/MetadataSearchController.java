package edu.ucar.cisl.sagesiparcsmetadatasearch.controller;

import edu.ucar.cisl.sagesiparcsmetadatasearch.repository.MetadataSearchRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MetadataSearchController {

    private MetadataSearchRepository metadataSearchRepository;

    public MetadataSearchController(MetadataSearchRepository metadataSearchRepository) {
        this.metadataSearchRepository = metadataSearchRepository;
    }

    @RequestMapping("/")
    public String SearchHomePage() {
        return "SearchHomePage";
    }

    @PostMapping("/result.html")
    public ModelAndView results(@RequestParam("query") String query) {

        ModelAndView modelAndView = new ModelAndView("result");
        modelAndView.addObject("results", this.metadataSearchRepository.getQueryResults(query));
        return modelAndView;
    }
}


