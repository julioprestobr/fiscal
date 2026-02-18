package com.prestobr.fiscal.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prestobr.fiscal.domain.enums.DocumentSource;
import lombok.AllArgsConstructor;
import lombok.Getter;

// DTO que representa a resposta da API ao buscar um documento fiscal

@Getter // gera automaticamente os getters de cada atributo da classe
@AllArgsConstructor // gera automaticamente um construtor com todos os campos como parâmetro
public class DocumentResponse {

    /** @JsonProperty("documento") → define que no JSON o campo se chamará "documento" em vez de "document" (nome do
     *                               atributo). Isso permite usar nomes em português na resposta da API sem mudar o nome
     *                               da variável Java.
     */
    //@JsonProperty("documento")
    private String document;

    private DocumentSource source;

    private String xml;
}
