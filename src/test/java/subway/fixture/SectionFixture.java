package subway.fixture;

import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.남영역;
import static subway.fixture.StationFixture.사당역;
import static subway.fixture.StationFixture.서울역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.수서역;
import static subway.fixture.StationFixture.시청역;
import static subway.fixture.StationFixture.신사역;
import static subway.fixture.StationFixture.용산역;
import static subway.fixture.StationFixture.종각역;
import static subway.fixture.StationFixture.혜화역;

import subway.domain.line.Distance;
import subway.domain.line.Section;

@SuppressWarnings("NonAsciiCharacters")
public class SectionFixture {

    // 1호선
    public static Section 남영역_서울역_10 = new Section(1L, 남영역, 서울역, new Distance(10));
    public static Section 서울역_시청역_5 = new Section(2L, 서울역, 시청역, new Distance(5));
    public static Section 시청역_종각역_10 = new Section(3L, 시청역, 종각역, new Distance(10));

    // 2호선
    public static Section 강남역_용산역_10 = new Section(3L, 강남역, 용산역, new Distance(10));
    public static Section 용산역_시청역_5 = new Section(4L, 용산역, 시청역, new Distance(5));
    public static Section 시청역_선릉역_5 = new Section(5L, 시청역, 선릉역, new Distance(5));

    // 3호선
    public static Section 신사역_수서역_7 = new Section(6L, 신사역, 수서역, new Distance(7));

    // 4호선
    public static Section 서울역_용산역_5 = new Section(7L, 서울역, 용산역, new Distance(5));
    public static Section 용산역_사당역_10 = new Section(8L, 용산역, 사당역, new Distance(10));
    public static Section 사당역_혜화역_10 = new Section(9L, 사당역, 혜화역, new Distance(10));
}
