package main;

import org.bklab.tools.DifferenceFinder;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DifferenceFinderT {

    @Test
    public void test() {
        File f1 = new File(System.getenv("USERPROFILE") + "/desktop/t1.txt");
        File f2 = new File(System.getenv("USERPROFILE") + "/desktop/t2.txt");
        File f3 = new File(System.getenv("USERPROFILE") + "/desktop/out.txt");
        List<String> differences = DifferenceFinder.create().startCompare(f1, f2);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f3));
            StringBuffer buffer = new StringBuffer();
            if (differences != null)
                differences.forEach(s -> buffer.append(s).append("\r\n"));
            else
                buffer.append("Same to " + f1.getName() + " and " + f2.getName());
            writer.write(buffer.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void test2() {
        int i = 0;
        System.out.println(String.format("difference %d:\r\nLine %d: %s\r\nLine %d: %s\r\n", ++i, 23, "/DESKTOP/new.txt", 23, "/DESKTOP/new.txt"));
        System.out.println(String.format("difference %d:\r\nLine %d: %s\r\nLine %d: %s\r\n", ++i, 23, "/DESKTOP/new.txt", 23, "/DESKTOP/new.txt"));
        System.out.println(String.format("difference %d:\r\nLine %d: %s\r\nLine %d: %s\r\n", ++i, 23, "/DESKTOP/new.txt", 23, "/DESKTOP/new.txt"));
        System.out.println(String.format("difference %d:\r\nLine %d: %s\r\nLine %d: %s\r\n", ++i, 23, "/DESKTOP/new.txt", 23, "/DESKTOP/new.txt"));
        System.out.println(String.format("difference %d:\r\nLine %d: %s\r\nLine %d: %s\r\n", ++i, 23, "/DESKTOP/new.txt", 23, "/DESKTOP/new.txt"));
        System.out.println(String.format("difference %d:\r\nLine %d: %s\r\nLine %d: %s\r\n", ++i, 23, "/DESKTOP/new.txt", 23, "/DESKTOP/new.txt"));
        System.out.println(String.format("difference %d:\r\nLine %d: %s\r\nLine %d: %s\r\n", ++i, 23, "/DESKTOP/new.txt", 23, "/DESKTOP/new.txt"));
        System.out.println(String.format("difference %d:\r\nLine %d: %s\r\nLine %d: %s\r\n", ++i, 23, "/DESKTOP/new.txt", 23, "/DESKTOP/new.txt"));
        System.out.println(String.format("difference %d:\r\nLine %d: %s\r\nLine %d: %s\r\n", ++i, 23, "/DESKTOP/new.txt", 23, "/DESKTOP/new.txt"));

    }


    @Test
    public void test3() {
        int  time = 1000000000;
        long start1 = new Date().getTime();
        double k;
        for (int i = 1; i <= time; i++) {
            k = Math.sqrt(i);
        }
        System.out.println(new Date().getTime() - start1);

        start1 = new Date().getTime();
        double j;
        for (int i = 1; i <= time; i++) {
            j = Math.sqrt(i);
        }
        System.out.println(new Date().getTime() - start1);

    }

    @Test
    public void test4() {
        String s1 = "mystery, with an invitation to \"come and find out\" what they have to offer.";
        String s2 = "Broderick Labs, with an invitation to \"come and find out\" what they have to offer.";

        List<String> temp = new ArrayList<>();
        int pointer = 0;        String s3 = "";

        String s4 = "";
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = i; j <= s1.length(); j++) {
                s3 = s1.substring(i, j);
                if(s2.indexOf(s3) > 0) {
                    System.out.println(s3);
                }
            }
        }
        System.out.println(s3);
    }

    @Test
    public void test5() {
        File f1 = new File(System.getenv("USERPROFILE") + "/desktop/t1.txt");
        File f2 = new File(System.getenv("USERPROFILE") + "/desktop/t2.txt");
        String s1 = getContentByFile(f1, "UTF-8");
        String s2 = getContentByFile(f2, "UTF-8");



    }


    private String getContentByFile(File f, String charset) {
        if(f == null && f.length() == 0) {
            return null;
        }

        String line;
        BufferedReader reader;
        StringBuilder builder = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), charset));
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
