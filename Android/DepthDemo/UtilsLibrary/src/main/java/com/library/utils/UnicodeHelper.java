package com.library.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unicode助手
 */
public class UnicodeHelper {


    public static String parser(String unicode) {
        if (unicode == null || unicode.length() == 0) return unicode;

        Pattern pattern = Pattern.compile("[uU]\\+[0-9a-fA-F]+([ ]{1}[0-9a-fA-F]+)*");
        Matcher matcher = pattern.matcher(unicode);
        List<UnicodeData> array = new ArrayList<>();
        StringBuffer SB = new StringBuffer();

        while (matcher.find()) {
            array.add(new UnicodeData(matcher.start(0), matcher.group(0)));
        }

        int start_index = 0, end_index = 0;
        for (UnicodeData disassemble : array) {
            end_index = disassemble.start;
            if (start_index != end_index) {
                SB.append(unicode, start_index, end_index);
            }
            end_index = disassemble.start + disassemble.data.length();
            SB.append(disassemble.decode());
            start_index = end_index;
        }
        if (start_index < unicode.length() - 1) {
            SB.append(unicode, start_index, unicode.length());
        }

        return SB.toString();
    }

    //拆解
    private static final class UnicodeData {
        int start;
        String data;

        public UnicodeData(int start, String data) {
            this.start = start;
            this.data = data;
        }

        public char[] decode() {
            String substring = data.substring(2);

            if (substring.contains(" ")) {

                List<Character> array = new ArrayList<>();
                String[] s = substring.split(" ");
                for (String s1 : s) {
                    char[] chars = Character.toChars(Integer.parseInt(s1, 16));
                    for (char aChar : chars) {
                        array.add(aChar);
                    }
                }

                char[] result = new char[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    result[i] = array.get(i);
                }

                return result;
            }
            return Character.toChars(Integer.parseInt(substring, 16));
        }

    }


}
