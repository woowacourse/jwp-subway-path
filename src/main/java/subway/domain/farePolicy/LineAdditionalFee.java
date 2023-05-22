package subway.domain.farePolicy;

import subway.domain.general.Money;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum LineAdditionalFee {
    LINE_ONE(1, 100),
    LINE_TWO(2, 200),
    LINE_THREE(3, 300),
    LINE_FOUR(4, 400),
    LINE_FIVE(5, 500);

    private final Long id;
    private final int fee;

    LineAdditionalFee(long id, int fee) {
        this.id = id;
        this.fee = fee;
    }

    public static LineAdditionalFee getFromId(Long id) {
        return Arrays.stream(LineAdditionalFee.values())
                .filter(lineAdditionalFee -> lineAdditionalFee.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }

    public static Money getBiggestFee(List<Long> lineIds) {
        List<Integer> fees = new ArrayList<>();
        for (Long lineId : lineIds) {
            fees.add(getFromId(lineId).getFee());
        }
        return Money.of(Collections.max(fees));
    }

    public long getId() {
        return id;
    }

    public int getFee() {
        return fee;
    }
}
