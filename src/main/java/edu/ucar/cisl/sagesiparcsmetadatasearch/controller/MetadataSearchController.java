package edu.ucar.cisl.sagesiparcsmetadatasearch.controller;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Collection;

@Controller
public class MetadataSearchController {

    private MetadataSearchConfig metadataSearchConfig;
    private String solrUrl;


    public MetadataSearchController(MetadataSearchConfig metadataSearchConfig, @Value("${spring.data.solr.host}") String solrUrl) {
        this.metadataSearchConfig = metadataSearchConfig;
        this.solrUrl = solrUrl;
    }

    @RequestMapping("/")
    public String SearchHomePage() {
        return "SearchHomePage";
    }

    @PostMapping("/result.html")
    public ModelAndView results(@RequestParam("query") String query) throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        solrQuery.setRows(10);
        QueryRequest queryRequest = new QueryRequest(solrQuery);
        QueryResponse solrResponse = queryRequest.process(this.metadataSearchConfig.getSolrClient(this.solrUrl));
        SolrDocumentList solrDocument = solrResponse.getResults();
        ModelAndView modelAndView = new ModelAndView("result");
        modelAndView.addObject("result", solrResponse.getResults());
        modelAndView.addObject("count", solrResponse.getResults().getNumFound());
        return modelAndView;
    }
}


