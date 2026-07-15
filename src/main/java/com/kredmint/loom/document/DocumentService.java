package com.kredmint.loom.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document createDocument(Document document){
        if(document.getStatus() == null){
            document.setStatus(Document.Status.ACTIVE);
        }
        document.setUploadedAt(LocalDateTime.now());
        return documentRepository.save(document);
    }

    public List<Document> getAllDocuments(){
        return documentRepository.findAll();
    }

    public Document getDocumentById(String id){
        return  documentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Document not found"));
    }

    public List<Document> getEmployeeDocuments(String id){
        return documentRepository.findByEmplyoeeId(id);
    }

    public List<Document> getDocumentsByStatus(Document.Status status){
        return documentRepository.findByStatus(status);
    }

    public List<Document> getDocumentsByDocumentType(Document.DocumentType documentType) {
        return documentRepository.findByDocumentType(documentType);
    }

    public Document updateDocument(String id, Document updatedDocument) {

        Document existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        existingDocument.setCreatedBy(updatedDocument.getCreatedBy());
        existingDocument.setUrl(updatedDocument.getUrl());
        existingDocument.setEmplyoeeId(updatedDocument.getEmplyoeeId());
        existingDocument.setName(updatedDocument.getName());
        existingDocument.setDocumentType(updatedDocument.getDocumentType());
        existingDocument.setUrlExpiryDate(updatedDocument.getUrlExpiryDate());
        existingDocument.setLenderId(updatedDocument.getLenderId());
        existingDocument.setComment(updatedDocument.getComment());
        existingDocument.setStatus(updatedDocument.getStatus());
        existingDocument.setDocPassword(updatedDocument.getDocPassword());

        return documentRepository.save(existingDocument);
    }

    public void deleteDocument(String id) {

        if (!documentRepository.existsById(id)) {
            throw new RuntimeException("Document not found");
        }

        documentRepository.deleteById(id);
    }
}


