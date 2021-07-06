package com.tsystems.simplepusher.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RouteNames {
    PROVIDER_CONNECTOR("ids-connector-provider");

    private final String value;
}
