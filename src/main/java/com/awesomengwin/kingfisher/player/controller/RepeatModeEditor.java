package com.awesomengwin.kingfisher.player.controller;

import com.awesomengwin.kingfisher.player.RepeatMode;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;

public class RepeatModeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isBlank(text)) {
            setValue(null);
        } else {
            setValue(EnumUtils.getEnum(RepeatMode.class, text.toUpperCase()));
        }
    }
}
