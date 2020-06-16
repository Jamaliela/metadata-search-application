package edu.ucar.cisl.sagesiparcsmetadatasearch.model;

import org.apache.solr.client.solrj.beans.Field;

public class Metadata {

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("doi")
    private String doi;

    @Field("resource_type")
    private String resourceType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}




