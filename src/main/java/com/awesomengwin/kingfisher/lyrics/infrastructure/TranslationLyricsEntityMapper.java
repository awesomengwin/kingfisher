package com.awesomengwin.kingfisher.lyrics.infrastructure;

import com.awesomengwin.kingfisher.lyrics.TranslationLyrics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TranslationLyricsLineValueObjectMapper.class)
public interface TranslationLyricsEntityMapper {

    TranslationLyrics toDomain(TranslationLyricsEntity entity);

    TranslationLyricsEntity toEntity(TranslationLyrics lyrics);

}
