package com.awesomengwin.kingfisher.document;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String title) {
        super("Document with title \"%s\" could not be found".formatted(title));
    }
}
