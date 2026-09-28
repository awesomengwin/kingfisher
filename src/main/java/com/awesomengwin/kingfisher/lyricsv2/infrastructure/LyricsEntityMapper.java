package com.awesomengwin.kingfisher.lyricsv2.infrastructure;

import com.awesomengwin.kingfisher.lyricsv2.Lyrics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = LyricsLineValueObjectMapper.class)
public interface LyricsEntityMapper {

    Lyrics toDomain(LyricsEntity entity);

    LyricsEntity toEntity(Lyrics domain);

}
