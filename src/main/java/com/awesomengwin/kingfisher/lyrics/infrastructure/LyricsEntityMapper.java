package com.awesomengwin.kingfisher.lyrics.infrastructure;

import com.awesomengwin.kingfisher.lyrics.Lyrics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = LyricsLineValueObjectMapper.class)
public interface LyricsEntityMapper {

    Lyrics toDomain(LyricsEntity entity);

    LyricsEntity toEntity(Lyrics domain);

}
