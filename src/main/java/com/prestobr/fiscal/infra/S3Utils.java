package com.prestobr.fiscal.infra;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária com métodos auxiliares para operações no AWS S3.
 *
 * O pacote "infra" (infraestrutura) é onde ficam classes de suporte técnico
 * que não contêm regras de negócio, mas que são necessárias para a aplicação funcionar
 * (acesso a banco de dados, filas, sistemas externos, etc.).
 *
 * Esta classe é estática (todos os métodos são static), ou seja, não precisa ser
 * instanciada — é usada diretamente como S3Utils.listKeys(...).
 */
public class S3Utils {

    /**
     * Lista todas as chaves (caminhos de arquivos) de um bucket S3 que começam com um determinado prefixo.
     *
     * O S3 não tem uma estrutura de pastas real — todos os arquivos são objetos com uma "chave" (key)
     * que é o caminho completo. Ex: "silver/fiscal/nfe/subpasta/arquivo.xml"
     * O prefixo funciona como um filtro: só retorna objetos cuja chave começa com ele.
     *
     * Por que paginação?
     *   O S3 retorna no máximo 1000 objetos por chamada. Se o bucket tiver mais,
     *   é preciso fazer múltiplas chamadas (paginação). O listObjectsV2Paginator()
     *   faz isso automaticamente, iterando página por página até buscar todos os objetos.
     *
     * @param s3Client cliente AWS configurado com credenciais e região
     * @param bucket   nome do bucket S3 (ex: "prestobr-repromaq-dev-datalake")
     * @param prefix   prefixo para filtrar os objetos (ex: "silver/fiscal/nfe")
     * @return lista com todas as chaves (paths completos) dos objetos encontrados
     */
    public static List<String> listKeys(S3Client s3Client, String bucket, String prefix) {

        List<String> keys = new ArrayList<>();

        // Monta a requisição de listagem com o bucket e o prefixo desejados
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix)
                .build();

        // listObjectsV2Paginator retorna um iterável de páginas.
        // Para cada página, itera sobre os objetos e adiciona a chave na lista.
        for (ListObjectsV2Response page : s3Client.listObjectsV2Paginator(request)) {
            for (S3Object obj : page.contents()) {
                keys.add(obj.key()); // obj.key() retorna o caminho completo do arquivo no S3
            }
        }

        return keys;
    }
}
