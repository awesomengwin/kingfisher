package com.awesomengwin.kingfisher.spotify;

import com.awesomengwin.kingfisher.spotify.valueobject.Image;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SpotifyUser implements OAuth2User {

    private final OAuth2User delegate;
    private final List<Image> images;
    private final String displayName;

    public SpotifyUser(OAuth2User delegate) {
        this.delegate = delegate;

        Map<String, Object> attributes = delegate.getAttributes();

        // Images parsing
        Object imagesObj = attributes.get("images");

        if (!(imagesObj instanceof List<?> imagesList)) {
            this.images = List.of();
        } else {
            this.images = imagesList.stream()
                    .filter(Map.class::isInstance)
                    .map(Map.class::cast)
                    .map(imageMap -> {
                        String url = (String) imageMap.get("url");
                        int height = (Integer) imageMap.get("height");
                        int width = (Integer) imageMap.get("width");

                        return new Image(url, height, width);
                    })
                    .toList();
        }

        // Display name parsing
        this.displayName = (String) attributes.get("display_name");
    }

    public List<Image> getImages() {
        return images;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    @NullMarked
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    @NullMarked
    public String getName() {
        return delegate.getName();
    }
}
