package org.bklab.tools;

import java.io.*;
import java.security.MessageDigest;
import java.util.*;

public class DifferenceFinder {

    public static DifferenceFinder create() {
        return new DifferenceFinder();
    }


    /**
     * Compared and return two files difference.
     *
     * @param f1 needs to compared source file;
     * @param f2 needs to compared target file;
     * @return
     */
    public List<String> startCompare(File f1, File f2) {
        if(md5(f1).equalsIgnoreCase(md5(f2))) { return null;}

        String file1Name = f1.getName();
        String file2Name = f2.getName();

        Map<Integer, String> file1 = getLinesByFile(f1);
        Map<Integer, String> file2 = getLinesByFile(f2);

        if(file1 == null && file2 == null) {
            return null;
        }

        Iterator<Map.Entry<Integer, String>> it1 = file1.entrySet().iterator();
        Iterator<Map.Entry<Integer, String>> it2 = file2.entrySet().iterator();

        List<String> differences = new ArrayList<>();

        int i = 0;
        boolean empty;

        while (true) {
            int k1 = 0, k2 = 0;
            String v1 = "", v2 = "";

            empty = true;
            Map.Entry<Integer, String> entry;
            if(it1.hasNext()) {
                empty = false;
                entry= it1.next();
                v1 = entry.getValue();
                k1 = entry.getKey();
            }

            if(it2.hasNext()) {
                empty = false;
                entry = it2.next();
                k2 = entry.getKey();
                v2 = entry.getValue();
            }

            if(!v1.equals(v2)) {
                differences.add(
                        String.format("difference %d:\tFile:%s:%d\tFile:%s:%d\r\n%s",
                                ++i, file1Name, k1, file2Name, k2, findDifferenceBy2String(v1, v2)));
            }

            if(empty) break;
        }

        return differences;
    }

    private String findDifferenceBy2String(String s1, String s2) {
        if(s1 == null && s2 == null) {return null;}
        if((s1 != null || !s1.isEmpty()) && (s2 == null || s2.isEmpty())) { return String.format("\r\n%d\tcolumn %d: %s\r\n\tcolumn %d: %s\r\n\r\n", 1, 0, s1.trim(), 0, "");};
        if((s1 == null || s1.isEmpty()) && (s2 != null || !s2.isEmpty())) { return String.format("\r\n%d\tcolumn %d: %s\r\n\tcolumn %d: %s\r\n\r\n", 1, 0, "", 0, s2.trim());};
        if(s1.equals(s2)) return null;

        String[] arr1 = s1.split(" +|\t+");
        String[] arr2 = s2.split(" +|\t+");

        StringBuffer differences = new StringBuffer();

        int length = Math.min(arr1.length, arr2.length);
        int column1 = 0, column2 = 0, continuous1 = 0, continuous2 = 0, index = 0;
        for (int i = 0; i < length; i++) {
            if(arr1[i].equals(arr2[i])){
                if(continuous1 > 0 && continuous2 > 0) {
                    differences.append(String.format("\r\n%d\tcolumn %d: %s\r\n\tcolumn %d: %s\r\n\r\n",
                            ++index, column1, s1.substring(column1, continuous1).trim(), column2, s2.substring(column2, continuous2).trim()));
                    continuous1 = 0;
                    continuous2 = 0;
                }
                column1 = s1.indexOf(arr1[i], column1) + arr1[i].length();
                column2 = s2.indexOf(arr2[i], column2) + arr2[i].length();
            } else {
                if(continuous1 == 0 && continuous2 == 0) {
                    column1 = s1.indexOf(arr1[i], column1);
                    column2 = s2.indexOf(arr2[i], column2);
                    if(i + 1 >= length) {
                        continuous1 = s1.length();
                        continuous2 = s2.length();
                    } else {
                        continuous1 = s1.indexOf(arr1[i + 1], column1) - 1;
                        continuous2 = s2.indexOf(arr1[i + 1], column2) - 1;
                    }
                } else {
                    if(i + 1 >= length) {
                        continuous1 = s1.length();
                        continuous2 = s2.length();
                    } else {
                        continuous1 = s1.indexOf(arr1[i + 1], column1 + continuous1) - 1;
                        continuous2 = s2.indexOf(arr2[i + 1], column2 + continuous2) - 1;
                    }
                }
            }
        }
        if(continuous1 > 0 && continuous2 > 0) {
            String sub1 = s1.substring(column1, continuous1).trim();
            String sub2 = s2.substring(column2, continuous2).trim();



            differences.append(String.format("\r\n%d\tcolumn %d: %s\r\n\tcolumn %d: %s\r\n\r\n",
                    ++index, column1, s1.substring(column1, continuous1).trim(), column2, s2.substring(column2, continuous2).trim()));
        }

        return differences.toString();
    }

    /**
     * Read the file and place it in the map by row.
     *
     * @param f worked file
     * @return Map<Integer Line number, String Content>
     */
    private Map<Integer, String> getLinesByFile(File f) {
        return getLinesByFile(f, "UTF-8");
    }

    private Map<Integer, String> getLinesByFile(File f, String charset) {
        if(f == null && f.length() == 0) {
            return null;
        }

        Map<Integer, String> lines = new LinkedHashMap<>();
        String line;
        int index = 0;
        BufferedReader reader;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), charset));
            while ((line = reader.readLine()) != null){
                ++index;
                if(!line.trim().isEmpty()) {
                    lines.put(index, line);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    private String md5(File file) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            @SuppressWarnings("resource")
            FileInputStream fis = new FileInputStream(file);
            byte[] arr = new byte[1024 * 8];
            int len = 0;
            while ((len = fis.read(arr)) != -1) {
                md5.update(arr, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrayToHex(md5.digest()).toLowerCase();
    }

    private String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

}
