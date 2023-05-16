package subway.domain.fee;

import subway.domain.Section;

import java.util.List;

public interface FeePolicy {

    int calculateFee(List<Section> sections);
}
