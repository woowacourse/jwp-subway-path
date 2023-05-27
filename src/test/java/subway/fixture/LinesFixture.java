package subway.fixture;

import subway.domain.subway.Lines;

import java.util.List;

import static subway.fixture.LineFixture.createLine;

public class LinesFixture {

	public static Lines createLines() {
		return new Lines(List.of(createLine()));
	}
}
