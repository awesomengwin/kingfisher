package com.awesomengwin.kingfisher.lyrics.infrastructure;

import com.awesomengwin.kingfisher.lyrics.LyricsLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LyricsLineValueObjectMapper {

    LyricsLine toLyricsLine(LyricsLineValueObject valueObject);

    LyricsLineValueObject toValueObject(LyricsLine lines);

}
