package subway.business.domain.direction;

import java.util.Arrays;
import java.util.List;
import subway.business.domain.line.Section;
import subway.business.domain.line.Station;

public enum Direction {
    //TODO Presentation 계층에서 Mapping하는 책임 가지고, text 없애도록 수정
    UPWARD("상행", new UpwardLineModifyStrategy()),
    DOWNWARD("하행", new DownwardLineModifyStrategy());

    private final String text;
    private final LineModifyStrategy lineModifyStrategy;

    Direction(String text, LineModifyStrategy lineModifyStrategy) {
        this.text = text;
        this.lineModifyStrategy = lineModifyStrategy;
    }

    public static Direction from(String text) {
        return Arrays.stream(Direction.values())
                .filter(direction -> direction.text.equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Direction은 상행 또는 하행만 입력할 수 있습니다. " +
                                "(입력한 Direction : %s)", text)
                ));
    }

    public void addTerminus(Station station, List<Section> sections, int distance) {
        lineModifyStrategy.addTerminus(station, sections, distance);
    }

    public void addMiddleStation(Station station, Station neighborhoodStation, List<Section> sections, int distance) {
        lineModifyStrategy.addMiddleStation(station, neighborhoodStation, sections, distance);
    }

    public Station getTerminus(List<Section> sections) {
        return lineModifyStrategy.getTerminus(sections);
    }
}
