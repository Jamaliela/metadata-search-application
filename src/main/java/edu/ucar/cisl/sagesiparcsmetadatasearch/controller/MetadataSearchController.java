package edu.ucar.cisl.sagesiparcsmetadatasearch.controller;

import edu.ucar.cisl.sagesiparcsmetadatasearch.model.MetadataSearchResults;
import edu.ucar.cisl.sagesiparcsmetadatasearch.repository.MetadataSearchRepository;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class MetadataSearchController {

    private MetadataSearchConfig metadataSearchConfig;
    private String solrUrl;
    private MetadataSearchRepository metadataSearchRepository;


    public MetadataSearchController(MetadataSearchConfig metadataSearchConfig, @Value("${spring.data.solr.host}") String solrUrl,
                                    MetadataSearchRepository metadataSearchRepository) {
        this.metadataSearchConfig = metadataSearchConfig;
        this.solrUrl = solrUrl;
        this.metadataSearchRepository = metadataSearchRepository;
    }

    @RequestMapping("/")
    public String SearchHomePage() {
        return "SearchHomePage";
    }

    @PostMapping("/result.html")
    public ModelAndView results(@RequestParam("query") String query) throws IOException, SolrServerException {

        MetadataSearchResults metadataSearchRepository = new MetadataSearchRepository(this.metadataSearchConfig,this.solrUrl).getQueryResults(query);
        ModelAndView modelAndView = new ModelAndView("result");
        modelAndView.addObject("results", metadataSearchRepository.getMetadataResultList());
        return modelAndView;
    }
}


