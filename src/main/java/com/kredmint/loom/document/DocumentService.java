package com.kredmint.loom.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private MongoTemplate mongoTemplate;

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

    public Document updateDocument(String id, Document updatedDocument) {

        Document existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        Document document = new Document(
                existingDocument.getId(),
                updatedDocument.getCreatedBy(),
                updatedDocument.getUrl(),
                updatedDocument.getEmplyoeeId(),
                updatedDocument.getName(),
                updatedDocument.getDocumentType(),
                updatedDocument.getUrlExpiryDate(),
                updatedDocument.getLenderId(),
                updatedDocument.getComment(),
                updatedDocument.getStatus(),
                updatedDocument.getDocPassword(),
                existingDocument.getUploadedAt());
        return documentRepository.save(document);

    }

    public void deleteDocument(String id) {

        if (!documentRepository.existsById(id)) {
            throw new RuntimeException("Document not found");
        }

        documentRepository.deleteById(id);
    }

    public List<Document> getDocuments(Document.Status status,
                                       Document.DocumentType documentType){
        Query query = new Query();

        if(status !=null){
            query.addCriteria(Criteria.where("status").is(status));
        }

        if(documentType!=null){
            query.addCriteria(Criteria.where("documentType").is(documentType));
        }
        return mongoTemplate.find(query, Document.class);
    }
}


