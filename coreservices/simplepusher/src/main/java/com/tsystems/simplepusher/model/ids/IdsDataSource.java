package com.tsystems.simplepusher.model.ids;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdsDataSource {
    private IdsDataSourceTypeEnum type;
    private String url;
    private String username;
    private String password;
}
