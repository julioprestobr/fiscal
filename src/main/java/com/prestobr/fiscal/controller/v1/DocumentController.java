package com.prestobr.fiscal.controller.v1;

import com.prestobr.fiscal.dto.response.DocumentResponse;
import com.prestobr.fiscal.service.DocumentService;
import org.springframework.web.bind.annotation.*;

/* Controller REST responsável por expor os endpoints de documentos fiscais. Recebe as requisições HTTP, delega o
 trabalho para o Service, e devolve a resposta ao cliente.
*/

@RestController
@RequestMapping("/v1/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/xml/{document}")
    public DocumentResponse getXmlByDocument(
            @PathVariable("document") String document,
            @RequestParam(value = "source", required = false) String source
    ) {
        return documentService.getXmlByDocument(document, source);
    }
}
