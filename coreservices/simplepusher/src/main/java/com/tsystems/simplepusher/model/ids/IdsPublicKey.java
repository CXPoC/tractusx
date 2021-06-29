package com.tsystems.simplepusher.model.ids;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdsPublicKey {
    @JsonProperty("@type")
    public String type;
    @JsonProperty("@id")
    public String id;
    @JsonProperty("ids:keyType")
    public IdsKeyType idsKeyType;
    @JsonProperty("ids:keyValue")
    public String idsKeyValue;
}
