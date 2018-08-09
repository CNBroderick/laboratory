package org.bklab.tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


/**
 * 汉字转化为拼音的工具类
 *
 * @author Broderick
 */
public class PinyinTools {
    private HanyuPinyinOutputFormat format;
    private boolean firstCase = false;
    private boolean extractingCapitalLetters = false;
    private boolean extractingCapitalLettersAndNumbers = false;
    private boolean extractingCapitalLettersNumbersSeparator = false;


    public PinyinTools() {
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    public String toPinYin(String str) throws BadHanyuPinyinOutputFormatCombination {
        return toPinYin(str, "");
    }

    /**
     * 根据本类的各项配置信息将汉字转化成拼音。
     * 如： 明天 转换成 MINGTIAN
     *
     * @param str：要转化的汉字
     * @param separator：转化结果的分割符
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public String toPinYin(String str, String separator) throws BadHanyuPinyinOutputFormatCombination {
        if (str == null || str.trim().length() == 0)
            return "";

        String py = "";
        String temp;
        String[] t;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((int) c <= 128)
                py += c;
            else {
                t = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (t == null)
                    py += c;
                else {
                    temp = t[0];
                    if (firstCase)
                        temp = t[0].toUpperCase().charAt(0) + temp.substring(1);
                    py += temp + (i == str.length() - 1 ? "" : separator);
                }
            }
        }
        if (extractingCapitalLetters) return extractingCapitalLetters(py.trim());
        if (extractingCapitalLettersAndNumbers) return extractingCapitalLettersAndNumbers(py.trim());
        if (extractingCapitalLettersNumbersSeparator) return extractingCapitalLettersNumbersSeparator(py.trim());
        return py.trim();
    }

    private String extractingCapitalLetters(String pinyin) {
        StringBuffer buffer = new StringBuffer();
        char[] ch = pinyin.toCharArray();
        // 得到大写字母
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] >= 'A' && ch[i] <= 'Z') {
                buffer.append(ch[i]);
            }
        }
        return buffer.toString();
    }

    private String extractingCapitalLettersAndNumbers(String pinyin) {
        StringBuffer buffer = new StringBuffer();
        char[] ch = pinyin.toCharArray();
        // 得到大写字母
        for (int i = 0; i < ch.length; i++) {
            if ((ch[i] >= 'A' && ch[i] <= 'Z') || (ch[i] >= '0' && ch[i] <= '9')) {
                buffer.append(ch[i]);
            }
        }
        return buffer.toString();
    }

    private String extractingCapitalLettersNumbersSeparator(String pinyin) {
        StringBuffer buffer = new StringBuffer();
        char[] ch = pinyin.toCharArray();
        // 得到大写字母
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] >= 'a' && ch[i] <= 'z') continue;
            buffer.append(ch[i]);
        }
        return buffer.toString();
    }

    public PinyinTools setWithToneNumber() {
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        return this;
    }

    public PinyinTools setWithoutToneNumber() {
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        return this;
    }

    public PinyinTools setUppercase() {
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        return this;
    }

    public PinyinTools setLowercase() {
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        return this;
    }

    public PinyinTools setEnableFirstCase() {
        setLowercase();
        firstCase = true;
        return this;
    }

    public PinyinTools setDisableFirstCase() {
        firstCase = false;
        return this;
    }

    public PinyinTools setEnableExtractingCapitalLetters() {
        this.extractingCapitalLetters = true;
        return this;
    }

    public PinyinTools setDisableExtractingCapitalLetters() {
        this.extractingCapitalLetters = false;
        return this;
    }

    public PinyinTools setEnableExtractingCapitalLettersAndNumbers() {
        this.extractingCapitalLettersAndNumbers = true;
        return this;
    }

    public PinyinTools setDisableExtractingCapitalLettersAndNumbers() {
        this.extractingCapitalLettersAndNumbers = false;
        return this;
    }

    public PinyinTools setEnableExtractingCapitalLettersNumbersSeparator() {
        this.extractingCapitalLettersNumbersSeparator = true;
        return this;
    }

    public PinyinTools setDisableExtractingCapitalLettersNumbersSeparator() {
        this.extractingCapitalLettersNumbersSeparator = false;
        return this;
    }
}