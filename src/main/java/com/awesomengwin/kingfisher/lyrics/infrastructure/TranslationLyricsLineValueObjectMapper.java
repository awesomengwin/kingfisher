package com.awesomengwin.kingfisher.lyrics.infrastructure;

import com.awesomengwin.kingfisher.lyrics.TranslationLyricsLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TranslationLyricsLineValueObjectMapper {

    TranslationLyricsLineValueObject toValueObject(TranslationLyricsLine lyricsLine);

    TranslationLyricsLine toLyricsLine(TranslationLyricsLineValueObject valueObject);
}
