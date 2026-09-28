package com.awesomengwin.kingfisher.lyricsv2.infrastructure;

import com.awesomengwin.kingfisher.lyricsv2.TranslationLyricsLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TranslationLyricsLineValueObjectMapper {

    TranslationLyricsLineValueObject toValueObject(TranslationLyricsLine lyricsLine);

    TranslationLyricsLine toLyricsLine(TranslationLyricsLineValueObject valueObject);
}
