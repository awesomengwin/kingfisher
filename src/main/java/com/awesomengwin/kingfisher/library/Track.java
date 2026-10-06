package com.awesomengwin.kingfisher.library;

import java.util.List;

public record Track(String uri, String name, Album album, List<Artist> artists) {
}
