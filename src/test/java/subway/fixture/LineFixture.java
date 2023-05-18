package subway.fixture;

import static subway.fixture.SectionFixture.강남역_용산역;
import static subway.fixture.SectionFixture.남영역_서울역;
import static subway.fixture.SectionFixture.사당역_혜화역;
import static subway.fixture.SectionFixture.서울역_시청역;
import static subway.fixture.SectionFixture.서울역_용산역;
import static subway.fixture.SectionFixture.시청역_선릉역;
import static subway.fixture.SectionFixture.시청역_종각역;
import static subway.fixture.SectionFixture.신사역_수서역;
import static subway.fixture.SectionFixture.용산역_사당역;
import static subway.fixture.SectionFixture.용산역_시청역;

import java.util.LinkedList;
import java.util.List;
import subway.domain.Line;
import subway.domain.Sections;

@SuppressWarnings("NonAsciiCharacters")
public class LineFixture {

    public static Line 일호선 = new Line(1L, "1호선", new Sections(new LinkedList<>(List.of(남영역_서울역, 서울역_시청역, 시청역_종각역))));
    public static Line 이호선 = new Line(2L, "2호선", new Sections(new LinkedList<>(List.of(강남역_용산역, 용산역_시청역, 시청역_선릉역))));
    public static Line 삼호선 = new Line(3L, "3호선", new Sections(new LinkedList<>(List.of(신사역_수서역))));
    public static Line 사호선 = new Line(4L, "4호선", new Sections(new LinkedList<>(List.of(서울역_용산역, 용산역_사당역, 사당역_혜화역))));
}
