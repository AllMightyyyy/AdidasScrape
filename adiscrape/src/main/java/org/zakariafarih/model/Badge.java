package org.zakariafarih.model;

public class Badge {
    private String style;
    private String text;

    // Getters and Setters

    @Override
    public String toString() {
        return "Badge{" +
                "style='" + style + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
