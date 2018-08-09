package main;

import org.bklab.math.Question;
import org.junit.Test;

public class MathTest {
    @Test
    public void test1() {
        Question q =  new Question();
        String result;
        int max = 0;
        for (int i = 2; i < 1000; i++) {
            result = q.getMaxCommonDivisor(i);
            if(result.split(" ").length == 3) {
                if(max < Integer.parseInt(result.split(" ")[2]))  max = Integer.parseInt(result.split(" ")[2]);
            }
        }

        for (int i = 2; i < 1000; i++) {
            result = q.getMaxCommonDivisor(i);
            if(result.split(" ").length == 3) {
                if(max == Integer.parseInt(result.split(" ")[2])){
                    System.out.println(i + ":" + result);
                }
            }
        }
    }
}
