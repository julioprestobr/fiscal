package com.prestobr.fiscal.client;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.prestobr.fiscal.dto.response.NfStockXmlResponse;

// Cliente responsável por buscar XMLs de documentos fiscais na API externa NFStock.

public class NfStockClient {

    private final WebClient webClient;
    private final String token;
    private final String codigoCrm;
    private final String cpfCnpj;

    public NfStockClient(
            WebClient webClient,
            String token,
            String codigoCrm,
            String cpfCnpj
    ) {
        this.webClient = webClient;
        this.token = token;
        this.codigoCrm = codigoCrm;
        this.cpfCnpj = cpfCnpj;
    }

    public String getXmlByDocument(String document) {

        // Monta o path da URL com os dados da empresa e o identificador do documento
        String url = "/api/v1/%s/%s/documentos/%s/chave".formatted(codigoCrm, cpfCnpj, document);

        try {
            NfStockXmlResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(url)
                            .queryParam("xml", "true")
                            .build()
                    )
                    .header("Authorization", "Bearer " + token) // autenticação JWT
                    .retrieve()
                    .bodyToMono(NfStockXmlResponse.class)
                    .block(); // aguarda a resposta sincronamente ( WebFlux é assíncrono por padrão)

            if (response == null) {
                return null;
            }

            return response.getXml();

        } catch (WebClientResponseException.NotFound e) {
            // em vez de lançar exceção, retorna null para que o service possa tentar outra fonte
            return null;
        }
    }
}
