package com.prestobr.fiscal.service;

import com.prestobr.fiscal.client.DataLakeClient;
import com.prestobr.fiscal.client.NfStockClient;
import com.prestobr.fiscal.domain.enums.DocumentSource;
import com.prestobr.fiscal.dto.response.DocumentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

// Service responsável pela lógica de negócio de busca de documentos fiscais

@Service
public class DocumentService {

    private final DataLakeClient dataLakeClient;
    private final NfStockClient nfStockClient;
    private final Map<String, DocumentSource> sources;

    public DocumentService(
            DataLakeClient dataLakeClient,
            NfStockClient nfStockClient
    ) {
        this.dataLakeClient = dataLakeClient;
        this.nfStockClient = nfStockClient;

        // LinkedHashMap mantém a ordem de inserção (datalake tem prioridade sobre nfstock)
        this.sources = new LinkedHashMap<>();
        this.sources.put("datalake", DocumentSource.DATALAKE);
        this.sources.put("nfstock", DocumentSource.NFSTOCK);
    }

    public DocumentResponse getXmlByDocument(String document, String source) {

        document = document.trim();

        // --- Fluxo com fonte específica ---
        if (source != null && !source.isBlank()) {

            source = source.toLowerCase().trim();

            // Valida se a fonte informada existe
            if (!sources.containsKey(source)) {
                String validSources = String.join(", ", sources.keySet());

                // Lança exceção que o Spring converte automaticamente em resposta HTTP 400
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid source. Use one of: " + validSources + "."
                );
            }

            String xml = getXmlFromSource(document, source);

            // Se não encontrou na fonte específica, lança 404
            if (xml == null || xml.isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Document not found in " + source + "."
                );
            }

            return new DocumentResponse(document, sources.get(source), xml);
        }

        // --- Fluxo sem fonte específica: tenta todas na ordem definida ---
        for (String sourceName : sources.keySet()) {

            String xml = getXmlFromSource(document, sourceName);

            // Se encontrou em alguma fonte, retorna imediatamente sem tentar as próximas
            if (xml != null && !xml.isBlank()) {
                return new DocumentResponse(document, sources.get(sourceName), xml);
            }
        }

        // Não encontrou em nenhuma fonte
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Document not found."
        );
    }

    private String getXmlFromSource(String document, String source) {

        return switch (source) {
            case "datalake" -> dataLakeClient.getXmlByDocument(document);
            case "nfstock"  -> nfStockClient.getXmlByDocument(document);
            default         -> null;
        };
    }
}
