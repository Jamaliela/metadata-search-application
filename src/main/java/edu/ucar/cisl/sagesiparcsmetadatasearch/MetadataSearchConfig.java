package edu.ucar.cisl.sagesiparcsmetadatasearch.controller;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MetadataSearchConfig {

    @Bean
    public SolrClient getSolrClient(@Value("${spring.data.solr.host}") String solrUrl) {

        HttpSolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();
        solrClient.setParser(new XMLResponseParser());
        return solrClient;
    }
}
