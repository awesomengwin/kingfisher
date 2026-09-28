package com.awesomengwin.kingfisher.nguyensplyrics;

import com.awesomengwin.kingfisher.lyrics.LyricsLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NguyenSpLyricsLyricsLineMapper {

    LyricsLine toLine(NguyenSpLyricsLyricsLine line);

}
