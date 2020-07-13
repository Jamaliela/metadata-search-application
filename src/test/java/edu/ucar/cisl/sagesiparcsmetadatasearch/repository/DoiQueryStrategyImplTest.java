package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DoiQueryStrategyImplTest {

    private DoiQueryStrategyImpl doiQueryStrategy;
    private MockQueryStrategyImpl mockQueryStrategy;

    @Before
    public void setup() {

        this.mockQueryStrategy = new MockQueryStrategyImpl();
        this.doiQueryStrategy = new DoiQueryStrategyImpl(this.mockQueryStrategy);
    }

    @Test
    public void given_query_text__when_query__then_doi_text_not_quoted() throws IOException, SolrServerException {

        assertDoi("/10.5065/d69g5jv8", "doi:/10.5065/d69g5jv8");
    }

    @Test
    public void given_http_or_https_query_text__when_query__then_doi_text_quoted() throws IOException, SolrServerException {

        assertDoi("https://doi.org/10.5065/d69g5jv8", "doi:\"https://doi.org/10.5065/d69g5jv8\"");
        assertDoi("http://doi.org/10.5065/d69g5jv8", "doi:\"http://doi.org/10.5065/d69g5jv8\"");
    }

    public void assertDoi(String queryText, String expectedText) throws IOException, SolrServerException {

        SolrDocumentList outputIsNull = this.doiQueryStrategy.query(queryText);
        assertEquals(expectedText, this.mockQueryStrategy.getQueryText());
    }

    private class MockQueryStrategyImpl implements QueryStrategy {

        private String queryText;

        @Override
        public SolrDocumentList query(String queryText) throws IOException, SolrServerException {

            this.queryText = queryText;
            return null;
        }

        public String getQueryText() {

            return this.queryText;
        }
    }
}
