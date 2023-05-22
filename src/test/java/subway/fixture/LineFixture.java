package subway.fixture;

import static subway.fixture.SectionFixture.강남역_용산역_10;
import static subway.fixture.SectionFixture.남영역_서울역_10;
import static subway.fixture.SectionFixture.사당역_혜화역_10;
import static subway.fixture.SectionFixture.서울역_시청역_5;
import static subway.fixture.SectionFixture.서울역_용산역_5;
import static subway.fixture.SectionFixture.시청역_선릉역_5;
import static subway.fixture.SectionFixture.시청역_종각역_10;
import static subway.fixture.SectionFixture.신사역_수서역_7;
import static subway.fixture.SectionFixture.용산역_사당역_10;
import static subway.fixture.SectionFixture.용산역_시청역_5;

import java.util.LinkedList;
import java.util.List;
import subway.domain.fare.Fare;
import subway.domain.line.Line;
import subway.domain.line.Sections;

@SuppressWarnings("NonAsciiCharacters")
public class LineFixture {

    public static Line 일호선 = new Line(1L, "1호선", new Fare(0),
            new Sections(new LinkedList<>(List.of(남영역_서울역_10, 서울역_시청역_5,
                    시청역_종각역_10))));
    public static Line 이호선 = new Line(2L, "2호선", new Fare(100),
            new Sections(new LinkedList<>(List.of(강남역_용산역_10, 용산역_시청역_5, 시청역_선릉역_5))));
    public static Line 삼호선 = new Line(3L, "3호선", new Fare(500), new Sections(new LinkedList<>(List.of(신사역_수서역_7))));
    public static Line 사호선 = new Line(4L, "4호선", new Fare(800),
            new Sections(new LinkedList<>(List.of(서울역_용산역_5, 용산역_사당역_10,
                    사당역_혜화역_10))));
}
