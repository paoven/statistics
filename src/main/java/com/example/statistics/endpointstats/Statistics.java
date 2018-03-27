package com.example.statistics.endpointstats;

public class Statistics {

    private double sum = 0.0d;
    private double max = 0.0d;
    private double min = 0.0d;
    private long count = 0l;

    static Statistics of(double amount) {
        final Statistics stats = new Statistics();
        stats.sum = amount;
        return stats;
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return count>0? sum/count : 0.0d;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public long getCount() {
        return count;
    }

    void accumulate(Statistics toBeAccumulated) {
        sum = sum + toBeAccumulated.sum;
        max = count>0? Math.max(max, toBeAccumulated.sum) : toBeAccumulated.sum;
        min = count>0? Math.min(min, toBeAccumulated.sum) : toBeAccumulated.sum;
        count++;
    }
    
    void reset() {
        count=0;
        sum = 0.0d;
        max = 0.0d;
        min = 0.0d;
    }

    @Override
    public String toString() {
        return "Statistics{" + "sum=" + sum + ", max=" + max + ", min=" + min + ", count=" + count + '}';
    }


}
