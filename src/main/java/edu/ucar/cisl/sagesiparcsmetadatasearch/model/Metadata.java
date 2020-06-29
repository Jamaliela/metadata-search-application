package edu.ucar.cisl.sagesiparcsmetadatasearch.model;

import org.apache.solr.client.solrj.beans.Field;

public class Metadata {

    @Field("id")
    private String id;

    @Field("title")
    private String title;

    @Field("description")
    private String descriptionHighlighted;

    @Field("description")
    private String description;

    @Field("doi")
    private String doi;

    @Field("resource_type")
    private String resourceType;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescriptionHighlighted() { return descriptionHighlighted; }

    public void setDescriptionHighlighted (String titleHighlighted) {

        this.descriptionHighlighted = titleHighlighted;
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

    public String getResourceType() {return resourceType; }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}




