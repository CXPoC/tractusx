package com.tsystems.simplepusher.model.ids;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Data
public class IdsResourceMetadata {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("keywords")
    private List<String> keywords;

    @JsonProperty("policy")
    private String policy;

    @JsonProperty("owner")
    private URI owner;

    @JsonProperty("license")
    private URI license;

    @JsonProperty("version")
    private String version;

    @JsonProperty("endpointDocumentation")
    private URI endpointDocumentation;

    @NotNull
    @JsonProperty("representations")
    private List<IdsResourceRepresentation> representations;
}
