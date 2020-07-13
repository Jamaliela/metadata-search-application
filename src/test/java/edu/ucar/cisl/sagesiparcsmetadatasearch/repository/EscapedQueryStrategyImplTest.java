package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class EscapedQueryStrategyImplTest {

    private EscapedQueryStrategyImpl escapedQueryStrategy;
    private MockQueryStrategyImpl mockQueryStrategy;

    @Before
    public void setup() {

        this.mockQueryStrategy = new MockQueryStrategyImpl();
        this.escapedQueryStrategy = new EscapedQueryStrategyImpl(this.mockQueryStrategy);
    }

    @Test
    public void testEscapedQueryStrategy() throws IOException, SolrServerException {

        assertQueryEscaped("/10.5065/d69g5jv8", "\\/10.5065\\/d69g5jv8");
    }

    private void assertQueryEscaped(String queryText, String escapedQueryText) throws IOException, SolrServerException {

        SolrDocumentList outputIsNull = this.escapedQueryStrategy.query(queryText);
        assertEquals(escapedQueryText, this.mockQueryStrategy.getQueryText());
    }

    private class MockQueryStrategyImpl implements QueryStrategy {

        private String queryText;

        @Override
        public SolrDocumentList query(String queryText) {

            this.queryText = queryText;
            return null;
        }

        public String getQueryText() {

            return this.queryText;
        }
    }
}
