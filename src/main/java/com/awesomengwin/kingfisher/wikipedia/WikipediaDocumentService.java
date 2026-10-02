package com.awesomengwin.kingfisher.wikipedia;

import com.awesomengwin.kingfisher.document.DocumentNotFoundException;
import com.awesomengwin.kingfisher.document.DocumentResponse;
import com.awesomengwin.kingfisher.document.DocumentService;
import org.springframework.stereotype.Service;

@Service
public class WikipediaDocumentService implements DocumentService {

    private final WikipediaClient client;

    public WikipediaDocumentService(WikipediaClient client) {
        this.client = client;
    }

    @Override
    public DocumentResponse getDocument(String title) {
        WikipediaSearchResponse searchResponse = client.search(title);

        long pageId = searchResponse.getPageId()
                .orElseThrow(() -> new DocumentNotFoundException(title));

        WikipediaPageExtractResponse pageExtractContentResponse =
                client.getPageExtractContent(String.valueOf(pageId));

        WikipediaPageResult pageResult = pageExtractContentResponse.getPageResult(pageId);

        return new DocumentResponse(pageResult.extract());
    }
}
