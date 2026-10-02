package com.awesomengwin.kingfisher.openai;

import com.awesomengwin.kingfisher.document.DocumentResponse;
import com.awesomengwin.kingfisher.document.DocumentService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class DocumentTools {

    private final DocumentService documentService;

    public DocumentTools(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Tool(description = """
            Always use this tool as a mandatory step to provide additional context for requests \
            to translate or analyze song lyrics.
            Get a document about a term or topic by title. Wikipedia is the top priority.
            The title must be natural page title, with no underscores or URL slugs.
            """)
    DocumentResponse getDocument(@ToolParam(description = """
            Natural page title of the artists, album, e.g. "Jimi Hendrix"
            """) String title) {
        return documentService.getDocument(title);
    }
}
