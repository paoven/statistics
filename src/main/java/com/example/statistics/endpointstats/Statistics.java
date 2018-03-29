package com.example.statistics.endpointstats;

public class Statistics {

    private double sum = 0.0d;
    private double max = 0.0d;
    private double min = 0.0d;
    private long count = 0l;

    private Statistics() {

    }

    static Statistics of(double amount) {
        final Statistics stats = new Statistics();
        stats.sum = amount;
        stats.max = amount;
        stats.min = amount;
        stats.count = 1;
        return stats;
    }

    public static Statistics zeroedStats() { // to be used for testing pourposes
        return new Statistics();
    }

    @Override
    public Statistics clone() throws CloneNotSupportedException {
        final Statistics stats = new Statistics();
        stats.sum = sum;
        stats.max = max;
        stats.min = min;
        stats.count = count;
        return stats;
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return count > 0 ? sum / count : 0.0d;
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

    void reduce(Statistics toBeAccumulated) {
        sum = sum + toBeAccumulated.sum;
        max = Math.max(max, toBeAccumulated.max);
        min = Math.min(min, toBeAccumulated.min);
        count = count + toBeAccumulated.count;
    }

    void reset() {
        count = 0;
        sum = 0.0d;
        max = 0.0d;
        min = 0.0d;
    }

    @Override
    public String toString() {
        return "Statistics{" + "sum=" + sum + ", max=" + max + ", min=" + min + ", count=" + count + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.sum) ^ (Double.doubleToLongBits(this.sum) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.max) ^ (Double.doubleToLongBits(this.max) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.min) ^ (Double.doubleToLongBits(this.min) >>> 32));
        hash = 53 * hash + (int) (this.count ^ (this.count >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Statistics other = (Statistics) obj;
        if (Double.doubleToLongBits(this.sum) != Double.doubleToLongBits(other.sum)) {
            return false;
        }
        if (Double.doubleToLongBits(this.max) != Double.doubleToLongBits(other.max)) {
            return false;
        }
        if (Double.doubleToLongBits(this.min) != Double.doubleToLongBits(other.min)) {
            return false;
        }
        if (this.count != other.count) {
            return false;
        }
        return true;
    }

}
