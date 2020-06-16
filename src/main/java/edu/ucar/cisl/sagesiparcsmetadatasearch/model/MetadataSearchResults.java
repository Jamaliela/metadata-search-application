package edu.ucar.cisl.sagesiparcsmetadatasearch.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetadataSearchResults {

    private List<Metadata> metadataResultList;
    private long numFound;

    public List<Metadata> getMetadataResultList() {
        return metadataResultList;
    }

    public void setMetadataResultList(List<Metadata> metadataResultList) {

        this.metadataResultList = metadataResultList;
    }

    public long getNumFound() {
        return numFound;
    }

    public void setNumFound(long numFound) {
        this.numFound = numFound;
    }
}
