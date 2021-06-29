package com.tsystems.simplepusher.model.ids;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@NoArgsConstructor
public class IdsAccessURL {
    @JsonProperty("@id")
    public URI id;
}
