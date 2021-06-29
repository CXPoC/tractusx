package com.tsystems.simplepusher.model.ids;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class IdsMaintainer {
    @JsonProperty("@id")
    public String id;
}
