package com.awesomengwin.kingfisher.nguyensplyrics;

import com.awesomengwin.kingfisher.lyrics.Lyrics;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = NguyenSpLyricsLyricsLineMapper.class)
public interface NguyenSpLyricsLyricsMapper {

    Lyrics toLyrics(NguyenSpLyricsLyrics nguyenSpLyricsLyrics);
}
