package com.awesomengwin.kingfisher.nguyensplyrics;

import com.awesomengwin.kingfisher.lyricsv2.LyricsLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NguyenSpLyricsLyricsLineMapper {

    LyricsLine toLine(NguyenSpLyricsLyricsLine line);

}
