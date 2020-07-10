package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;

public class ExecuteQueryStrategyImpl implements QueryStrategy {

    private final SolrClient solrClient;

    public ExecuteQueryStrategyImpl(SolrClient solrClient) {

        this.solrClient = solrClient;
    }

    @Override
    public SolrDocumentList query(String queryText) throws IOException, SolrServerException {

        SolrQuery query = new SolrQuery();
        query.setQuery(queryText);
        query.setRows(10000);
        QueryResponse response = this.solrClient.query(query);
        return response.getResults();
    }
}
