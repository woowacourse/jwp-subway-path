package subway.domain.path;

import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class TestDomainData {

    private final List<Station> stations;
    private final List<Section> sections;

    private TestDomainData(final List<Station> stations, final List<Section> sections) {
        this.stations = stations;
        this.sections = sections;
    }

    public static TestDomainData create() {
        final List<Station> stations = makeStations();
        final List<Section> sections = makeSections(stations);
        return new TestDomainData(stations, sections);
    }

    private static List<Station> makeStations() {
        return List.of(
                new Station(1L, "종각"), new Station(2L, "시청"), new Station(3L, "서울역"), new Station(4L, "남영"),
                new Station(5L, "용산"), new Station(6L, "노량진"), new Station(7L, "대방"), new Station(8L, "신길"),
                new Station(9L, "영등포"), new Station(10L, "신도림"), new Station(11L, "구로"), new Station(12L, "을지로입구"),
                new Station(13L, "충정로"), new Station(14L, "아현"), new Station(15L, "이대"), new Station(16L, "신촌"),
                new Station(17L, "홍대입구"), new Station(18L, "합정"), new Station(19L, "당산"), new Station(20L, "영등포구청"),
                new Station(21L, "문래"), new Station(22L, "대림")
        );
    }

    private static List<Section> makeSections(final List<Station> stations) {
        final Line line1 = new Line(1L, "1호선", "bg-color-blue");
        final Line line2 = new Line(2L, "2호선", "bg-color-green");

        final Distance distance = new Distance(1);

        final List<Section> sections = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final Station previous = stations.get(i);
            final Station next = stations.get(i + 1);
            sections.add(new Section(line1, previous, next, distance));
        }
        final List<Station> stationsOfLine2 = new ArrayList<>(stations.subList(12, 21));
        stationsOfLine2.addAll(0, List.of(stations.get(11),stations.get(1)));
        stationsOfLine2.addAll(11, List.of(stations.get(9), stations.get(21)));
        for (int i = 0; i < stationsOfLine2.size() - 1; i++) {
            final Station previous = stationsOfLine2.get(i);
            final Station next = stationsOfLine2.get(i + 1);
            sections.add(new Section(line2, previous, next, distance));
        }
        return sections;
    }

    public Station getStationByName(final String name) {
        return stations.stream()
                .filter(station -> name.equals(station.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 도메인에 없는 역을 입력하셨습니다."));
    }

    public List<Section> getSections() {
        return sections;
    }
}
