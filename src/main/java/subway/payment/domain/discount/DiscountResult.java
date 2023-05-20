package subway.payment.domain.discount;

import java.util.LinkedHashMap;
import java.util.Map;

public class DiscountResult {

    private final Map<String, Integer> feeByDiscountInfo;

    public DiscountResult() {
        this.feeByDiscountInfo = new LinkedHashMap<>();
    }

    public void add(final String discountInfo, final int fee) {
        this.feeByDiscountInfo.put(discountInfo, fee);
    }

    public Map<String, Integer> feeByDiscountInfo() {
        return feeByDiscountInfo;
    }
}
