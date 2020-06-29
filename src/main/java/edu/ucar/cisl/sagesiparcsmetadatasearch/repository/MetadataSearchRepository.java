package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import edu.ucar.cisl.sagesiparcsmetadatasearch.MetadataSearchConfig;
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

import javax.management.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MetadataSearchRepository {

    private MetadataSearchConfig metadataSearchConfig;
    private String solrUrl;

    @Autowired
    public MetadataSearchRepository(MetadataSearchConfig metadataSearchConfig, @Value("${spring.data.solr.host}") String solrUrl) {

        this.metadataSearchConfig = metadataSearchConfig;
        this.solrUrl = solrUrl;
    }

    public MetadataSearchResults getQueryResults(String queryText) {

        MetadataSearchResults metadataSearchResults;

        try {

            SolrDocumentList solrDocumentList = tryGetQueryResults(queryText).getResults();
            metadataSearchResults = createMetadataSearchResults(solrDocumentList, queryText);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        return metadataSearchResults;
    }

    private QueryResponse tryGetQueryResults(String queryText) throws IOException, SolrServerException {

        SolrQuery query = new SolrQuery();
        query.setQuery(queryText);
        query.setRows(10000);
        query.setHighlight(true).setHighlightSnippets(1);
        query.setParam("hl.fl", "description");
//        query.setParam("hl.fragsize", "0");
        query.setParam("hl.simple.pre", "<strong>");
        query.setParam("hl.simple.post", "</strong>");
        QueryResponse response = this.metadataSearchConfig.getSolrClient(this.solrUrl).query(query);
        return response;
    }

    private MetadataSearchResults createMetadataSearchResults(SolrDocumentList solrDocumentList, String queryText) throws IOException, SolrServerException {

        MetadataSearchResults metadataSearchResults = new MetadataSearchResults();
        metadataSearchResults.setNumFound(solrDocumentList.getNumFound());
        List<Metadata> metadataList = setMetadataResults(solrDocumentList, queryText);
        metadataSearchResults.setMetadataResultList(metadataList);

        return metadataSearchResults;
    }

    private List<Metadata> setMetadataResults(SolrDocumentList results, String queryText) throws IOException, SolrServerException {

        QueryResponse queryResponse = tryGetQueryResults(queryText);
        Map<String, Map<String, List<String>>> highlightMap = queryResponse.getHighlighting();
        List<Metadata> metadataList = new ArrayList<>();
        for (SolrDocument result : results) {

            Metadata solrItem = new Metadata();
            solrItem.setId((String) result.get("id"));
            List<String> descriptionList = highlightMap.get(result.get("id")).get("description");
            if (descriptionList != null && descriptionList.size()>0) {

                solrItem.setDescriptionHighlighted(descriptionList.get(0));
            }
            else {

                solrItem.setDescription((String) result.get("description"));
            }
            solrItem.setDescription((String) result.get("description"));
            solrItem.setTitle((String) result.get("title"));
            solrItem.setResourceType((String) result.get("resource_type"));
            solrItem.setDoi((String) result.get("doi"));
            metadataList.add(solrItem);
        }

        return metadataList;
    }
}
