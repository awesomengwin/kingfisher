package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.library.Page;
import org.mapstruct.Mapping;

public interface SpotifyPageMapper<S, T> {

    @Mapping(target = "page", expression = "java(source.offset() / source.limit())")
    @Mapping(target = "size", source = "limit")
    @Mapping(target = "totalPages", expression = "java((source.total() + source.limit() - 1) / source.limit())")
    @Mapping(target = "totalElements", source = "total")
    Page<T> toPage(SpotifyPage<S> source);
}
