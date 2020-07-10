package edu.ucar.cisl.sagesiparcsmetadatasearch;

import edu.ucar.cisl.sagesiparcsmetadatasearch.repository.*;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Component
public class MetadataSearchConfig {

    @Bean
    public SolrClient getSolrClient(@Value("${spring.data.solr.host}") String solrUrl) {

        HttpSolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();
        solrClient.setParser(new XMLResponseParser());
        return solrClient;
    }

    @Bean
    public Collection<QueryStrategy> getQueryStrategies(SolrClient solrClient, @Qualifier("fieldNames")Collection<String> fieldNames) {

        QueryStrategy executeQueryStrategy = new ExecuteQueryStrategyImpl(solrClient);

        QueryStrategy escapeQueryStrategy = new EscapedQueryStrategyImpl(executeQueryStrategy);

        QueryStrategy doiQueryStrategy = new DoiQueryStrategyImpl(executeQueryStrategy);

        QueryStrategy solrFieldStrategy = new SolrFieldQueryStrategyImpl(executeQueryStrategy,doiQueryStrategy, fieldNames);

        List<QueryStrategy> strategies = new ArrayList<>();
        strategies.add(solrFieldStrategy);
        strategies.add(executeQueryStrategy);
        strategies.add(escapeQueryStrategy);

        return strategies;
    }

    @Bean("fieldNames")
    public List<String> getFieldNamesFromSolrSchema(SolrClient solrClient) throws IOException, SolrServerException {

        SchemaRequest.Fields request = new SchemaRequest.Fields();
        SchemaResponse.FieldsResponse response = request.process(solrClient);
        List<Map<String, Object>> fields = response.getFields();
        List<String> fieldNames = new ArrayList<>();
        for (Map<String, Object> map : fields) {

            String value = map.get("name").toString();
            fieldNames.add(value+":");
        }

        return fieldNames;
    }
}
