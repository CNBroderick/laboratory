package org.bklab.math;

public class Question {
    public String getMaxCommonDivisor(int n){
        int i = n / 2;
        while (n % i != 0) i--;
        for (int j = 2; j <= Math.sqrt(n); j++) {
            for (int k = 2; k <= Math.sqrt(n); k++) {
                if(Math.pow(j, k) == i) return i + " " + j + " " + k;
            }
        }
        return i + "";
    }

}
