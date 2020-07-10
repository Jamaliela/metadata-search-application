package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import edu.ucar.cisl.sagesiparcsmetadatasearch.model.Metadata;
import edu.ucar.cisl.sagesiparcsmetadatasearch.model.MetadataSearchResults;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class MetadataSearchRepository {

    private final SolrClient solrClient;
    private final Collection<QueryStrategy> queryStrategies;

    @Autowired
    public MetadataSearchRepository(SolrClient solrClient, Collection<QueryStrategy> queryStrategies) {

        this.solrClient = solrClient;
        this.queryStrategies = queryStrategies;
    }

    public MetadataSearchResults getQueryResults(String queryText) {

        SolrDocumentList solrDocumentList = null;

        for (QueryStrategy strategy : this.queryStrategies) {

            try {

                solrDocumentList = strategy.query(queryText);

                if (!solrDocumentList.isEmpty()) {

                    break;
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        return createMetadataSearchResults(solrDocumentList);
    }

    private MetadataSearchResults createMetadataSearchResults(SolrDocumentList solrDocumentList) {

        MetadataSearchResults metadataSearchResults = new MetadataSearchResults();
        metadataSearchResults.setNumFound(solrDocumentList.getNumFound());
        List<Metadata> metadataList = setMetadataResults(solrDocumentList);
        metadataSearchResults.setMetadataResultList(metadataList);

        return metadataSearchResults;
    }

    private List<Metadata> setMetadataResults(SolrDocumentList results) {

        List<Metadata> metadataList = new ArrayList<>();
        for (SolrDocument result : results) {

            Metadata solrItem = new Metadata();
            solrItem.setTitle((String) result.get("title"));
            solrItem.setDescription((String) result.get("description"));
            solrItem.setResourceType((String) result.get("resource_type"));
            solrItem.setDoi((String) result.get("doi"));
            metadataList.add(solrItem);
        }

        return metadataList;
    }
}
