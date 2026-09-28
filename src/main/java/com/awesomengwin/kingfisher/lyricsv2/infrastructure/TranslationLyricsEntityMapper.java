package com.awesomengwin.kingfisher.lyricsv2.infrastructure;

import com.awesomengwin.kingfisher.lyricsv2.TranslationLyrics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TranslationLyricsLineValueObjectMapper.class)
public interface TranslationLyricsEntityMapper {

    TranslationLyrics toDomain(TranslationLyricsEntity entity);

    TranslationLyricsEntity toEntity(TranslationLyrics lyrics);

}
