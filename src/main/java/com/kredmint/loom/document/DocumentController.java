package com.kredmint.loom.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/create")
   public Document createDocument(@RequestBody Document document){
        return documentService.createDocument(document);
    }

    @GetMapping
    public List<Document> getAllDocuments(){
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public Document getDocumentById(@PathVariable String id){
        return documentService.getDocumentById(id);
    }

    @PutMapping("/{id}")
    public Document updateDocument(@PathVariable String id, @RequestBody Document document){
        return documentService.updateDocument(id, document);
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable String id){
        documentService.deleteDocument(id);
    }

    @GetMapping("/user/{id}")
    public List<Document> getDocumentsByUserId(@PathVariable String id) {
        return documentService.getEmployeeDocuments(id);
    }

    @GetMapping("/status/{status}")
    public List<Document> getDocumentsByStatus(@PathVariable Document.Status status) {
        return documentService.getDocumentsByStatus(status);
    }

    @GetMapping("/type/{documentType}")
    public List<Document> getDocumentsByDocumentType(
            @PathVariable Document.DocumentType documentType) {
        return documentService.getDocumentsByDocumentType(documentType);
    }




}
