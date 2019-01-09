package loop.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains utility for creation of charts in the output windows, for example histogram creation.
 * 
 * @author Peter Koepernik
 *
 */
public class ChartUtils {
    
    /**
     * Create a histogram out of the given list of values.
     * 
     * @param <T> the type of values
     * @param values the values out of which a histogram shall be created
     * @return the histogram
     */
    public static synchronized <T> Map<T, Integer> createHistogram(List<T> values) {
        Map<T, Integer> hist = new HashMap<T, Integer>();
        values.forEach(val -> hist.put(val, 0));
        values.forEach(val -> hist.put(val, hist.get(val) + 1));
        return hist;
    }
    
    /**
     * Create a histogram out of the given list of values, where the top and bottom x% of the given values are discarded, where
     * 0% <= x <= 50% is given as cutoff between 0 and 0.5. For that to work, the type {@code T} of given values must be comparable.
     * 
     * @param <T> the type of values
     * @param values the values out of which a histogram shall be created
     * @param cutoff the cutoff
     * @return the histogram
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T extends Comparable<? super T>> Map<T, Integer> createHistogram(List<T> values, double cutoff) {
        if (cutoff < 0.0 || cutoff > 0.5) {
            throw new IllegalArgumentException("cutoff must be between 0 and 0.5");
        }
        
        Object[] sortedValues = values.stream().sorted().toArray();
        T minValue = (T) sortedValues[(int) Math.floor(cutoff * sortedValues.length)];
        T maxValue = (T) sortedValues[sortedValues.length - 1 - (int) Math.floor(cutoff * sortedValues.length)];
        
        Map<T, Integer> hist = createHistogram(values);
        values.stream().filter(val -> val.compareTo(minValue) < 0 || val.compareTo(maxValue) > 0).forEach(val -> hist.remove(val));
        return hist;
    }
    
    /**
     * Creates a histogram out of a given set of integers. The integers will be collected in a specifiable amount of bins of equal size,
     * and each bin will either be labeled with the mean value or the bounds of the interval covered by it.
     * 
     * @param values the values out of which a histogram shall be created
     * @param desiredBinCount the desired amount of bins. The actual amount of bins in the resulting histogram may differ due to round-off errors
     * @param cutoff the cutoff
     * @param showMeanAsBinLabel if this is {@code true}, the mean value of a bins covered interval will be used as its label, otherwise the bounds
     * @return the histogram
     */
    public static synchronized Map<String, Integer> createHistogram(List<Integer> values, int desiredBinCount, double cutoff, boolean showMeanAsBinLabel) {
        Integer[] sortedValues = values.stream().sorted().toArray(Integer[]::new);
        int minValue = sortedValues[(int) Math.floor(cutoff * sortedValues.length)];
        int maxValue = sortedValues[sortedValues.length - 1 - (int) Math.floor(cutoff * sortedValues.length)];
        
        int binWidth = (int) Math.ceil(((double) (maxValue - minValue)) / (double) desiredBinCount);
        int binCount = (int) Math.ceil(((double) (maxValue - minValue + 1)) / (double) binWidth);
        
        //create labels
        String[] binLabels = new String[binCount];
        if (showMeanAsBinLabel) {
            int offset = (int) Math.round(((double) (binWidth - 1)) / 2.0);
            for (int i = 0; i < binCount; i++) {
                binLabels[i] = String.format("%d", minValue + i * binWidth + offset);
            }
        } else {
            for (int i = 0; i < binCount; i++) {
                binLabels[i] = String.format("%d-%d", minValue + i * binWidth, minValue - 1 + (i + 1) * binWidth);
            }
        }
        
        //create histogram
        Map<String, Integer> hist = new HashMap<String, Integer>();
        values.stream().filter(val -> minValue <= val && val <= maxValue).sorted().forEach(val -> {
            String binLabel = binLabels[binIndex(binWidth, val, minValue)];
            hist.putIfAbsent(binLabel, 0);
            hist.put(binLabel, hist.get(binLabel) + 1);
        });
        
        return hist;
    }
    
    private static int binIndex(int binWidth, int value, int minValue) {
        int index = 0;
        while (value > minValue - 1 + (index + 1) * binWidth) index++;
        return index;
    }
    
    /**
     * Creates a histogram out of a given set of doubles. The doubles will be collected in a specifiable amount of bins of equal size,
     * and each bin will either be labeled with the mean value or the bounds of the interval covered by it.
     * 
     * @param values the values out of which a histogram shall be created
     * @param binCount the amount of bins
     * @param cutoff the cutoff
     * @param showMeanAsBinLabel if this is {@code true}, the mean value of a bins covered interval will be used as its label, otherwise the bounds
     * @return the histogram
     */
    public static synchronized Map<String, Integer> createHistogram(List<Double> values, int binCount, double cutoff, boolean showMeanAsBinLabel, int decimalPrecision) {
        Double[] sortedValues = values.stream().sorted().toArray(Double[]::new);
        double minValue = sortedValues[(int) Math.floor(cutoff * sortedValues.length)];
        double maxValue = sortedValues[sortedValues.length - 1 - (int) Math.floor(cutoff * sortedValues.length)];
        
        double binWidth = (maxValue - minValue) / (double) binCount;
        
        //create labels
        String[] binLabels = new String[binCount];
        NumberFormat formatter = decimalFormatter(decimalPrecision);
        if (showMeanAsBinLabel) {
            double offset = binWidth / 2.0;
            for (int i = 0; i < binCount; i++) {
                binLabels[i] = String.format("%s", formatter.format(minValue + i * binWidth + offset));
            }
        } else {
            for (int i = 0; i < binCount; i++) {
                binLabels[i] = String.format("%s-%s", formatter.format(minValue + i * binWidth), formatter.format(minValue + (i + 1) * binWidth));
            }
        }
        
        //create histogram
        Map<String, Integer> hist = new HashMap<String, Integer>();
        values.stream().filter(val -> minValue <= val && val <= maxValue).sorted().forEach(val -> {
            String binLabel = binLabels[binIndex(binWidth, val, minValue)];
            hist.putIfAbsent(binLabel, 0);
            hist.put(binLabel, hist.get(binLabel) + 1);
        });
        
        return hist;
    }
    
    private static int binIndex(double binWidth, double value, double minValue) {
        int index = 0;
        while (value > minValue - 1 + (index + 1) * binWidth) index++;
        return index;
    }
    
    /**
     * Returns a {@link NumberFormat} that formats a given decimal number with the given amount
     * of positions after decimal point.
     * 
     * @param precision the amount of positions after decimal point
     * @return the number formatter
     */
    public static synchronized NumberFormat decimalFormatter(int precision) {
        if (precision == 0) return new DecimalFormat("#0");
        
        String zeros = "";
        for (int i = 0; i < precision; i++) {
            zeros += "0";
        }
        return new DecimalFormat("#0." + zeros);
    }

}
