package com.awesomengwin.kingfisher.lyricsv2.infrastructure;

import com.awesomengwin.kingfisher.lyricsv2.LyricsLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LyricsLineValueObjectMapper {

    LyricsLine toLyricsLine(LyricsLineValueObject valueObject);

    LyricsLineValueObject toValueObject(LyricsLine lines);

}
