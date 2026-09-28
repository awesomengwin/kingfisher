package com.awesomengwin.kingfisher.lyricsv2.infrastructure;

import com.awesomengwin.kingfisher.lyricsv2.LyricsLine;
import org.mapstruct.Mapper;

@Mapper
public interface LyricsLineValueObjectMapper {

    LyricsLine toValueObject(LyricsLineValueObject valueObject);

}
