package com.zia.magiccard.Bean;

import java.io.Serializable;

/**
 * Created by zia on 17-8-25.
 */

public class MarkdownData implements Serializable {
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
