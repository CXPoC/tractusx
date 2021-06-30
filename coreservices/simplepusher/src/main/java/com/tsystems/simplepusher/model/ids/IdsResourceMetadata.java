package com.tsystems.simplepusher.model.ids;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Data
public class IdsResourceMetadata {
    @JsonProperty("ids:title")
    private String title;

    @JsonProperty("ids:description")
    private String description;

    @JsonProperty("ids:keywords")
    private List<String> keywords;

    @JsonProperty("ids:policy")
    private String policy;

    @JsonProperty("ids:owner")
    private URI owner;

    @JsonProperty("ids:license")
    private URI license;

    @JsonProperty("ids:version")
    private String version;

    @JsonProperty("ids:endpointDocumentation")
    private URI endpointDocumentation;

    @NotNull
    @JsonProperty("ids:representations")
    private List<IdsResourceRepresentation> representations;
}
