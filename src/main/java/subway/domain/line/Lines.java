package subway.domain.line;

import java.util.ArrayList;
import java.util.List;

public class Lines {

    private final List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public static Lines from(List<Line> lines) {
        return new Lines(lines);
    }

    public void add(Line line) {
        validateDuplicatedName(line);
        validateDuplicatedColor(line);
        lines.add(line);
    }

    private void validateDuplicatedName(Line line) {
        boolean result = lines.stream().anyMatch(each -> each.isSameName(line));
        if (result) {
            throw new IllegalArgumentException("[ERROR] 중복되는 이름으로 노선을 생성할 수 없습니다.");
        }
    }

    private void validateDuplicatedColor(Line line) {
        boolean result = lines.stream().anyMatch(each -> each.isSameColor(line));
        if (result) {
            throw new IllegalArgumentException("[ERROR] 중복되는 색상으로 노선을 생성할 수 없습니다.");
        }
    }

    public int findMostExpensiveExtraFare() {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .getAsInt();
    }
}
