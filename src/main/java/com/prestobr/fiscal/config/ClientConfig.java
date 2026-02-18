package com.prestobr.fiscal.config;

import com.prestobr.fiscal.client.DataLakeClient;
import com.prestobr.fiscal.client.NfStockClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Arrays;
import java.util.List;

// Classe de configuração responsável por criar e registrar os clientes HTTP/S3 como beans do Spring

@Configuration
public class ClientConfig {
    @Value("${s3.bucket-name}")
    private String bucketName;

    @Value("${datalake.silver-documents-xml-base-prefix}")
    private String silverXmlBasePrefix;

    @Value("${datalake.valid-document-types}")
    private String validTypes;

    @Value("${nfstock.base-url}")
    private String nfStockBaseUrl;

    @Value("${nfstock.token}")
    private String nfStockToken;

    @Value("${nfstock.codigo-crm}")
    private String nfStockCodigoCrm;

    @Value("${nfstock.cpf-cnpj}")
    private String nfStockCpfCnpj;

    //Cria o bean do DataLakeClient (cliente para buscar XMLs no S3).
    @Bean
    public DataLakeClient dataLakeClient(S3Client s3Client) {

        List<String> types = Arrays.stream(validTypes.split(","))
                .map(String::trim)
                .toList();

        return new DataLakeClient(
                s3Client,
                bucketName,
                silverXmlBasePrefix,
                types
        );
    }

    //Cria o bean do WebClient configurado para o NFStock.
    @Bean
    public WebClient nfStockWebClient() {
        return WebClient.builder()
                .baseUrl(nfStockBaseUrl)
                .build();
    }

    @Bean
    public NfStockClient nfStockClient(WebClient nfStockWebClient) {

        return new NfStockClient(
                nfStockWebClient,
                nfStockToken,
                nfStockCodigoCrm,
                nfStockCpfCnpj
        );
    }
}
