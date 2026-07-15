package com.kredmint.loom.document;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentRepository extends MongoRepository<Document, String> {
    List<Document> findByEmplyoeeId(String employeeId);

    List<Document> findByStatus(Document.Status status);

    List<Document> findByDocumentType(Document.DocumentType documentType);
}
