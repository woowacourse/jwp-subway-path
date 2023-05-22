package subway.fixture;

import subway.domain.subway.Section;
import subway.domain.subway.Station;

public class SectionFixture {

	public static Section createSection() {
		return new Section(new Station("잠실역"), new Station("잠실새내역"), 3);
	}

	public static Section createSection2() {
		return new Section(new Station("잠실새내역"), new Station("선릉역"), 5);
	}

	public static Section createSection(final long distance) {
		return new Section(new Station("잠실역"), new Station("잠실새내역"), distance);
	}

	public static Section createSection(final Station stationA, final Station stationB) {
		return new Section(stationA, stationB, 3);
	}
}
