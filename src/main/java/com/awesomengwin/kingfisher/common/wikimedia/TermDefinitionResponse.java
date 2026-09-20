package com.awesomengwin.kingfisher.common.wikimedia;

import java.util.List;

public record TermDefinitionResponse(
        UsageDescription en
) {
    public record UsageDescription(
            String partOfSpeech,
            List<Definition> definitions
    ) {
        public record Definition(
                String definition,
                List<String> examples
        ) {
        }
    }
}
