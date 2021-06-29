package com.tsystems.simplepusher.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class ResourceMetadata {
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
    @JsonSerialize(using = RepresentationsToJson.class)
    @JsonDeserialize(using = JsonToRepresentation.class)
    private Map<UUID, ResourceRepresentation> representations;

    private static class RepresentationsToJson extends
            JsonSerializer<Map<UUID, ResourceRepresentation>> {

        @Override
        public void serialize(Map<UUID, ResourceRepresentation> value, JsonGenerator gen,
                              SerializerProvider provider) {
            try {
                gen.writeObject(value.values());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private static class JsonToRepresentation extends
            JsonDeserializer<Map<UUID, ResourceRepresentation>> {

        @Override
        public Map<UUID, ResourceRepresentation> deserialize(JsonParser p,
                                                             DeserializationContext ctx) {
            try {
                var node = p.readValueAsTree();
                final var objectMapper = new ObjectMapper();

                var representations = IntStream.range(0, node.size()).boxed()
                        .map(i -> {
                            try {
                                return objectMapper
                                        .readValue(node.get(i).toString(), ResourceRepresentation.class);
                            } catch (IOException e) {
                                throw new RuntimeException();
                            }
                        }).collect(Collectors.toList());

                var output = new HashMap<UUID, ResourceRepresentation>();
                for (var representation : representations) {
                    output.put(representation.getUuid() == null ? UUID.randomUUID() : representation.getUuid(), representation);
                }

                return output;
            } catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }
        }
    }
}
