package com.tsystems.simplepusher.model.ids;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdsResourceRepresentation {
    @JsonProperty("ids:uuid")
    private UUID uuid;
    @JsonProperty("ids:type")
    private String type;
    @JsonProperty("ids:byteSize")
    private Long byteSize;
    @JsonProperty("ids:name")
    private String name;
    @JsonProperty("ids:source")
    private IdsDataSource source;
}
