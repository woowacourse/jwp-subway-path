package subway.factory;

import subway.domain.Section;
import subway.domain.Station;

public class SectionFactory {

    public static Section createSection() {
        return new Section(new Station("잠실역"), new Station("잠실새내역"), 3L);
    }

    public static Section createSection2() {
        return new Section(new Station("잠실새내역"), new Station("종합운동장역"), 5L);
    }

    public static Section createSection(final Long distance) {
        return new Section(new Station("잠실역"), new Station("잠실새내역"), distance);
    }

    public static Section createSection(final Station stationA, final Station stationB) {
        return new Section(stationA, stationB, 3L);
    }
}
