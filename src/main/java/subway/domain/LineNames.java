package subway.domain;

import subway.exception.DuplicateLineNameException;
import subway.exception.LineNotFoundException;

import java.util.List;
import java.util.Objects;

public class LineNames {

    private final List<String> lineNames;

    public LineNames(List<String> lineNames) {
        this.lineNames = lineNames;
    }

    public void validateLineExist(String inputLineName) {
        if (!hasLineOfName(inputLineName)) {
            throw new LineNotFoundException("존재하지 않는 노선입니다");
        }
    }

    public void validateLineNotExist(String inputLineName) {
        if (hasLineOfName(inputLineName)) {
            throw new DuplicateLineNameException("이미 존재하는 노선입니다");
        }
    }

    private boolean hasLineOfName(String lineName) {
        return lineNames.stream()
                .anyMatch(name -> Objects.equals(name, lineName));
    }
}
