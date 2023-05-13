package subway.domain;

import subway.exception.GlobalException;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private List<Line> lines = new ArrayList<>();

    public Line addNewLine(String lineName, Sections sections) {
        validateDuplicate(lineName);
        Line line = new Line(lineName,sections);
        lines.add(line);
        return line;
    }

    private void validateDuplicate(String lineName) {
        if(lines.stream().anyMatch(it -> it.getName().equals(lineName))){
            throw new GlobalException("중복된 line 이름입니다.");
        }
    }
}
