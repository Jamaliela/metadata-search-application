package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import edu.ucar.cisl.sagesiparcsmetadatasearch.controller.MetadataSearchConfig;
import edu.ucar.cisl.sagesiparcsmetadatasearch.model.Metadata;
import edu.ucar.cisl.sagesiparcsmetadatasearch.model.MetadataSearchResults;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MetadataSearchRepository {

    private MetadataSearchConfig metadataSearchConfig;
    private String solrUrl;

    @Autowired
    public MetadataSearchRepository(MetadataSearchConfig metadataSearchConfig, @Value("${spring.data.solr.host}") String solrUrl) {
        this.metadataSearchConfig = metadataSearchConfig;
        this.solrUrl = solrUrl;
    }

    public MetadataSearchResults getQueryResults(String queryText) throws IOException, SolrServerException {

        MetadataSearchResults metadataSearchResults = new MetadataSearchResults();
        SolrQuery query = new SolrQuery();
        query.setQuery(queryText);
        QueryResponse response = this.metadataSearchConfig.getSolrClient(this.solrUrl).query(query);
        SolrDocumentList results = response.getResults();
        metadataSearchResults.setNumFound(results.getNumFound());
        List<Metadata> solrItems = new ArrayList<>();
        for (SolrDocument result : results) {

            Metadata solrItem = new Metadata();

            solrItem.setTitle((String) result.get("title"));
            solrItem.setDescription((String) result.get("description"));
            solrItem.setResourceType((String) result.get("resource_type"));
            solrItem.setDoi((String) result.get("doi"));
            solrItems.add(solrItem);
        }

        metadataSearchResults.setMetadataResultList(solrItems);


        return metadataSearchResults;

    }
}
