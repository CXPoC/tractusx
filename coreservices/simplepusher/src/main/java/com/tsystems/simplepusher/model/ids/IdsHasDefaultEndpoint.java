package com.tsystems.simplepusher.model.ids;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@NoArgsConstructor
public class IdsHasDefaultEndpoint {
    @JsonProperty("@type")
    public String type;
    @JsonProperty("@id")
    public URI id;
    @JsonProperty("ids:accessURL")
    public IdsAccessURL idsAccessURL;
}
