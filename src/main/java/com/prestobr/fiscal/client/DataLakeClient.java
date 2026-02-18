package com.prestobr.fiscal.client;

import com.prestobr.fiscal.infra.S3Utils;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;

// Cliente responsável por buscar XMLs de documentos fiscais no DataLake (AWS S3).

public class DataLakeClient {

    private final S3Client s3Client;
    private final String bucketName;
    private final String basePrefix;
    private final List<String> validTypes;

    public DataLakeClient(
            S3Client s3Client,
            String bucketName,
            String basePrefix,
            List<String> validTypes
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.basePrefix = basePrefix.replaceAll("^/|/$", ""); // remove barras extras das bordas
        this.validTypes = validTypes;
    }

    /* Busca o XML de um documento fiscal pelo seu identificador (ex: chave de acesso da NF-e).

     * Funcionamento:
     *   1. Itera sobre cada tipo de documento válido (nfe, nfce, cte, etc.)
     *   2. Para cada tipo, lista todos os arquivos do S3 naquele prefixo
     *   3. Verifica se algum arquivo tem o nome "{document}.xml"
     *   4. Se encontrar, baixa e retorna o conteúdo do arquivo como String
     */
    public String getXmlByDocument(String document) {

        for (String type : validTypes) {

            // Monta o prefixo completo para o tipo atual
            String prefix = (basePrefix + "/" + type).replaceAll("^/|/$", "");

            // Lista todas as chaves dentro desse prefixo no S3
            List<String> keys = S3Utils.listKeys(s3Client, bucketName, prefix);

            for (String key : keys) {

                // Extrai apenas o nome do arquivo a partir do caminho completo
                String fileName = key.substring(key.lastIndexOf("/") + 1);

                // Verifica se o nome do arquivo corresponde ao documento buscado
                if (fileName.equals(document + ".xml")) {

                    // Monta a requisição de download do objeto S3
                    GetObjectRequest getRequest = GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build();

                    // Baixa o arquivo e converte os bytes para String UTF-8
                    ResponseBytes<?> bytes = s3Client.getObjectAsBytes(getRequest);
                    return bytes.asString(StandardCharsets.UTF_8);
                }
            }
        }

        // Não encontrou em nenhum tipo de documento
        return null;
    }
}
