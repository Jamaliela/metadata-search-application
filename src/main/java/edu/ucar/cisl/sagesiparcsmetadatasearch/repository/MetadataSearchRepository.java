package edu.ucar.cisl.sagesiparcsmetadatasearch.repository;

import edu.ucar.cisl.sagesiparcsmetadatasearch.model.Metadata;
import edu.ucar.cisl.sagesiparcsmetadatasearch.model.MetadataSearchResults;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MetadataSearchRepository {

    private SolrClient solrClient;

    @Autowired
    public MetadataSearchRepository(SolrClient solrClient) {

        this.solrClient = solrClient;
    }

    public MetadataSearchResults getQueryResults(String queryText) {

        MetadataSearchResults metadataSearchResults;

        try {

            SolrDocumentList solrDocumentList = tryGetQueryResults(queryText);
            metadataSearchResults = createMetadataSearchResults(solrDocumentList);

        } catch (Exception e) {

            try {

                metadataSearchResults = tryGetDoiQueryResults(queryText);

            } catch (Exception error) {

                try {

                    metadataSearchResults = tryGetQueryResultsEscaped(queryText);

                } catch (Exception Error) {

                    throw new RuntimeException(Error);
                }
            }
        }

        return metadataSearchResults;
    }

    private MetadataSearchResults tryGetDoiQueryResults(String queryText) throws IOException, SolrServerException {

        StringBuffer doiQuery = new StringBuffer(queryText);
        doiQuery.insert(0, "doi:\"");
        doiQuery.insert(doiQuery.length(),"\"");
        SolrDocumentList solrDocumentList = tryGetQueryResults(doiQuery.toString());
        MetadataSearchResults metadataSearchResults = createMetadataSearchResults(solrDocumentList);
        return metadataSearchResults;
    }

    private MetadataSearchResults tryGetQueryResultsEscaped(String queryText) throws IOException, SolrServerException {

        queryText = ClientUtils.escapeQueryChars(queryText);
        SolrDocumentList solrDocumentList = tryGetQueryResults(queryText);
        MetadataSearchResults metadataSearchResults = createMetadataSearchResults(solrDocumentList);
        return metadataSearchResults;
    }

    private SolrDocumentList tryGetQueryResults(String queryText) throws IOException, SolrServerException {

        SolrQuery query = new SolrQuery();
        query.setQuery(queryText);
        query.setRows(10000);
        QueryResponse response = this.solrClient.query(query);
        return response.getResults();

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

    private List<String> getFieldNamesFromSolrSchema() throws IOException, SolrServerException {

        SchemaRequest.Fields request = new SchemaRequest.Fields();
        SchemaResponse.FieldsResponse response = request.process(this.solrClient);
        List<Map<String, Object>> fields = response.getFields();
        List<String> fieldNames = new ArrayList<>();
        for (Map<String, Object> map : fields) {

            String value = map.get("name").toString();
            fieldNames.add(value);
        }
        return fieldNames;
    }
}
