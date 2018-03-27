package com.example.statistics.endpointstats;

public class Statistics {

    private double sum = 0.0d;
    private double max = Double.MIN_VALUE;
    private double min = Double.MAX_VALUE;
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
        count++;
        sum = sum + toBeAccumulated.sum;
        max = Math.max(max, toBeAccumulated.sum);
        min = Math.min(min, toBeAccumulated.sum);
    }
    
    void reset() {
        count=0;
        sum = 0.0d;
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "Statistics{" + "sum=" + sum + ", max=" + max + ", min=" + min + ", count=" + count + '}';
    }


}
