package org.bklab.vaadin.csdn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadSingleLineNumber {

    public ArrayList<Double> read() {
        ArrayList<Double> numbers = new ArrayList<>();
        BufferedReader br;
        String filePath = "C:\\Users\\Broderick\\Desktop\\num.txt";
        try {
            br = new BufferedReader(new FileReader(filePath));
            String s = br.readLine();
            while (s != null){
                try {
                    numbers.add(Double.parseDouble(s));
                } catch (NumberFormatException ignore){}
                s = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(numbers != null && numbers.size() > 0) {
            numbers.forEach(d -> System.out.println(d));
        }


        return numbers;
    }

    public static void main(String[] args) {
        System.out.println( new ReadSingleLineNumber().test2());
    }

    int[] test1(int[] arrays, int add_length) {
        System.out.println(System.getProperty("os.name"));
        return Arrays.copyOf(arrays, arrays.length + add_length);
    }


    MySqlLog test2(){
        String log = "2018-03-21T13:46:01.185376Z 0 [Warning] TIMESTAMP with implicit DEFAULT value is deprecated. Please use --explicit_defaults_for_timestamp server option (see documentation for more details).";
        String[] head = log.substring(0, log.indexOf(']') + 1).split(" ");
        return new MySqlLog(head[0].trim(), Integer.parseInt(head[1].trim()), head[2].trim().substring(head[2].trim().indexOf('[') + 1, head[2].trim().indexOf(']')), log.substring(log.indexOf(']') + 2).trim());
    }

    class MySqlLog{
        String time;
        int index;
        String level;
        String msg;
        public MySqlLog(String time, int index, String level, String msg) {
            this.time = time;
            this.index = index;
            this.level = level;
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "MySqlLog{" +
                    "time='" + time + '\'' +
                    ", index=" + index +
                    ", level='" + level + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

}
