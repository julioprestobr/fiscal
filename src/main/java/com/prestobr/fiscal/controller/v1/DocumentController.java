package com.prestobr.fiscal.controller.v1;

import com.prestobr.fiscal.dto.response.DocumentResponse;
import com.prestobr.fiscal.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/* Controller REST responsável por expor os endpoints de documentos fiscais. Recebe as requisições HTTP, delega o
 trabalho para o Service, e devolve a resposta ao cliente.
*/

@RestController
@RequestMapping("/v1/documents")
@Tag(name = "Documentos Fiscais")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(summary = "Busca o XML de um documento fiscal pela chave de acesso")
    @GetMapping("/xml/{document}")
    public DocumentResponse getXmlByDocument(
            @Parameter(description = "Identificador do documento fiscal. Pode ser chave de acesso NF-e, NFC-e, NFS-e, CT-e e CF-e.")
            @PathVariable("document") String document,

            @Parameter(description = "Fonte de consulta. Valores válidos: 'datalake' ou 'nfstock'.")
            @RequestParam(value = "source", required = false) String source
    ) {
        return documentService.getXmlByDocument(document, source);
    }
}
