package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DoiQueryStrategyImpl implements QueryStrategy {

    private final QueryStrategy queryStrategy;
    private final Collection<String> fields;

    public DoiQueryStrategyImpl(QueryStrategy queryStrategy) {

        this.queryStrategy = queryStrategy;
        this.fields = new ArrayList<>();
        this.fields.add("https://");
        this.fields.add("http://");
    }

    @Override
    public SolrDocumentList query(String queryText) throws IOException, SolrServerException {

        if (startsWithUrl(queryText)) {

          queryText = String.format("doi:\"%s\"", queryText);

        }
        else {

            queryText = String.format("doi:%s", queryText);

        }
        return this.queryStrategy.query(queryText);
    }

    private boolean startsWithUrl(String queryText) {

        return this.fields.stream().anyMatch(field -> queryText.startsWith(field));
    }
}
