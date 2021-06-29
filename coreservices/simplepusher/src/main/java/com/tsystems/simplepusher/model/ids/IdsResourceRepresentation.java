package com.tsystems.simplepusher.model.ids;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdsResourceRepresentation {
    private UUID uuid;
    private String type;
    private Long byteSize;
    private String name;
    private IdsDataSource source;
}
