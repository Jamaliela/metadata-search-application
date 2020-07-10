package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;

public class EscapedQueryStrategyImpl implements QueryStrategy {

    private final QueryStrategy queryStrategy;

    public EscapedQueryStrategyImpl(QueryStrategy queryStrategy) {

        this.queryStrategy = queryStrategy;
    }

    @Override
    public SolrDocumentList query(String queryText) throws IOException, SolrServerException {

        queryText = ClientUtils.escapeQueryChars(queryText);
        return this.queryStrategy.query(queryText);
    }
}
