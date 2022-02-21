package com.betimes.fb.review.util;

import java.text.DecimalFormat;

public class FormatUtil {
    public static String format(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormat df = new DecimalFormat("###.#");

        return String.format("%s%c", df.format(count / Math.pow(1000, exp)), "kMGTPE".charAt(exp-1));
    }
}
