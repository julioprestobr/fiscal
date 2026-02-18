package com.prestobr.fiscal.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

// Enum que representa as fontes possíveis de um documento fiscal

public enum DocumentSource {

    NFSTOCK("nfstock"),
    DATALAKE("datalake");

    private final String value;

    DocumentSource(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
