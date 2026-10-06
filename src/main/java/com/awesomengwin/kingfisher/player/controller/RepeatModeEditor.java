package com.awesomengwin.kingfisher.player.controller;

import com.awesomengwin.kingfisher.player.RepeatMode;

import java.beans.PropertyEditorSupport;

public class RepeatModeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text.isBlank()) {
            setValue(null);
        } else {
            setValue(RepeatMode.valueOf(text.toLowerCase()));
        }
    }
}
