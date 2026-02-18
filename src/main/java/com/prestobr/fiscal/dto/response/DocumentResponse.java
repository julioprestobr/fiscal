package com.prestobr.fiscal.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prestobr.fiscal.domain.enums.DocumentSource;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

// DTO que representa a resposta da API ao buscar um documento fiscal

@Getter // gera automaticamente os getters de cada atributo da classe
@AllArgsConstructor // gera automaticamente um construtor com todos os campos como parâmetro
@Schema(description = "Resposta com o XML do documento fiscal")
public class DocumentResponse {

    /** @JsonProperty("documento") → define que no JSON o campo se chamará "documento" em vez de "document" (nome do
     *                               atributo). Isso permite usar nomes em português na resposta da API sem mudar o nome
     *                               da variável Java.
     */
    @Schema(
            description = "Identificador do documento (NF-e, NFC-e, NFS-e, CT-e e CF-e)",
            example = "58460212495678000156550010000012358678901484"
    )
    //@JsonProperty("documento")
    private String document;

    @Schema(
            description = "Fonte/origem do XML do documento",
            example = "datalake"
    )
    private DocumentSource source;

    @Schema(
            description = "Conteúdo XML completo do documento em formato string",
            example = "<NFe>...</NFe>"
    )
    private String xml;
}
