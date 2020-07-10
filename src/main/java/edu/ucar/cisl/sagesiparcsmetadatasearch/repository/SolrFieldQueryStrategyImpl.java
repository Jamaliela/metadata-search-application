package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.Collection;

public class SolrFieldQueryStrategyImpl implements QueryStrategy {

    private final QueryStrategy executeQueryStrategy;
    private final QueryStrategy doiQueryStrategy;
    private final Collection<String> fields;

    public SolrFieldQueryStrategyImpl(QueryStrategy executeQueryStrategy, QueryStrategy doiQueryStrategy, Collection<String> fields) {

        this.executeQueryStrategy = executeQueryStrategy;
        this.doiQueryStrategy = doiQueryStrategy;
        this.fields = fields;
    }

    @Override
    public SolrDocumentList query(String queryText) throws IOException, SolrServerException {

        SolrDocumentList solrDocumentList;

        if (this.startsWithField(queryText)) {

            solrDocumentList = this.executeQueryStrategy.query(queryText);
        }
        else {

            solrDocumentList = this.doiQueryStrategy.query(queryText);
        }
        return solrDocumentList;
    }

    private boolean startsWithField(String queryText) {

        return this.fields.stream().anyMatch(field -> queryText.startsWith(field));
    }
}
