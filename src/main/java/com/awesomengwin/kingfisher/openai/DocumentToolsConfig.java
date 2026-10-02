package com.awesomengwin.kingfisher.openai;

import com.awesomengwin.kingfisher.document.DocumentService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentToolsConfig {

    @Bean
    ToolCallback documentRetrieval(DocumentService documentService) {
        return FunctionToolCallback.builder("documentRetrieval", documentService::getDocument)
                .description("""
                        Always use this tool as a mandatory step to provide additional context such as artist, album \
                        for requests to translate or analyze song lyrics.
                        Get a document about a term or topic by title. Wikipedia is the top priority.
                        The title must be natural page title, with no underscores or URL slugs.
                        """)
                .inputType(String.class)
                .build();
    }
}
