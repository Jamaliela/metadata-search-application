package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;

public interface QueryStrategy {

    SolrDocumentList query(String queryText) throws IOException, SolrServerException;
}
