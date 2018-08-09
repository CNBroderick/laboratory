package org.bklab.vaadin.vertical;

import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

/**
 * Vertical label using css
 *
 * @author Broderick
 * @version 1.0.0 release
 * @date Thursday March 1, 2018 CST
 */
public class VerticalLabel extends Label {

    /**
     * vertical label with 12px size font and line-height is 100%
     * you can customize set font size and line-height using other constructor or set method.
     */
    public VerticalLabel() {
        this.getState().text = "<p style=\"font-size: " + 12 + "px;line-height:100%; word-wrap: break-word; \">" + "" + "</p>";
        this.getState().contentMode = ContentMode.HTML;
        this.setWidth(12, Unit.PIXELS);
    }

    /**
     * Customize size vertical labels
     *
     * @param text      to vertical shown text
     * @param font_size text font size
     * @return
     */
    public VerticalLabel(String text, int font_size) {
        this.getState().text = "<p style=\"font-size: " + font_size + "px;line-height:100%; word-wrap: break-word; \">" + text + "</p>";
        this.getState().contentMode = ContentMode.HTML;
        this.setWidth(font_size, Unit.PIXELS);
    }

    /**
     * Customize spacing and size vertical labels
     *
     * @param text                to vertical shown text
     * @param font_size           text font size
     * @param line_height_percent recommend 100~120
     * @return Vertical Labels
     */
    public VerticalLabel(String text, int font_size, int line_height_percent) {
        Label label = new Label("<p style=\"font-size: " + font_size + "px;line-height:" + line_height_percent + "%; word-wrap: break-word; \">" + text + "</p>", ContentMode.HTML);
        label.setWidth(font_size, Sizeable.Unit.PIXELS);
    }

    /**
     * Change or set label's value
     *
     * @param value new value for this label
     */
    @Override
    public void setValue(String value) {
        value = value == null ? "" : value;
        String labelValue = this.getValue();
        if(value.indexOf('<') <= 0) {
            super.setValue("<p style=\"font-size: 12px;line-height:100%; word-wrap: break-word; \">" + value + "</p>");
            return;
        }
        super.setValue(labelValue.substring(0, labelValue.indexOf('>') + 1) + value + labelValue.substring(labelValue.lastIndexOf("</")));
    }

    /**
     * Get font size for this label
     *
     * @return int: font size, the unit is px, like word...
     */
    public int getFontSize() {
        String labelValue = this.getValue();
        int location = labelValue.indexOf("font-size:") + "font-size:".length();
        return Integer.parseInt(labelValue.substring(location, labelValue.indexOf("px", location)).trim());
    }

    /**
     * Change or set font size for this label
     *
     * @param fontSize new font size for this label
     */
    public void setFontSize(int fontSize) {
        String labelValue = this.getValue();
        int location = labelValue.indexOf("font-size:", labelValue.indexOf("style=")) + "font-size:".length();
        super.setValue(labelValue.substring(0, location) + fontSize + labelValue.substring(labelValue.indexOf("px", location)));
    }

    /**
     * Get line height in every word for this label
     *
     * @return String a percent value, like 100%, 120%...
     */
    public String getLineHeight() {
        String labelValue = this.getValue();
        int location = labelValue.indexOf("line-height:") + "line-height:".length();
        return Integer.parseInt(labelValue.substring(location, labelValue.indexOf("%", location)).trim()) + "%";
    }

    /**
     * Change or set line height in every word for this label
     *
     * @param lineHeight set new line spacing, the format is a percent, recommend 100 ~ 120;
     */
    public void setLineHeight(int lineHeight) {
        String labelValue = this.getValue();
        int location = labelValue.indexOf("line-height:", labelValue.indexOf("style=")) + "line-height:".length();
        super.setValue(labelValue.substring(0, location) + lineHeight + labelValue.substring(labelValue.indexOf("%", location)));
    }

    /**
     * Get untagged value for this label.
     *
     * @return String untagged value.
     */
    public String getValueWithoutHtmlLabel() {
        String labelValue = this.getValue();
        return labelValue.substring(labelValue.indexOf('>') + 1, labelValue.lastIndexOf("</"));
    }
}
