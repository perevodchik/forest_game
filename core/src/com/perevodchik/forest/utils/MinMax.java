package com.perevodchik.forest.utils;

public class MinMax {
    final public int min;
    final public int max;

    public MinMax(int min, int max) {
        if(min < max) {
            this.min = min;
            this.max = max;
        }
        else {
            this.min = min;
            this.max = min + 1;
        }
    }
}
