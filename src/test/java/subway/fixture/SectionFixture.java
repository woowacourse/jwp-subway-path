package subway.fixture;

import subway.domain.Distance;
import subway.domain.Section;
import subway.dto.SectionResponse;

import static subway.fixture.LineFixture.호선2_응답;
import static subway.fixture.LineFixture.호선8_응답;
import static subway.fixture.StationFixture.*;

public class SectionFixture {

    public static final Distance DISTANCE1 = new Distance(10);
    public static final Distance DISTANCE2 = new Distance(15);

    public static final Section 잠실새내_잠실 = new Section(1L, 잠실새내, 잠실, DISTANCE1);
    public static final Section 잠실_잠실나루 = new Section(2L, 잠실, 잠실나루, DISTANCE2);
    public static final Section 몽촌토성_잠실나루 = new Section(3L, 몽촌토성, 잠실나루, DISTANCE1);
    public static final Section 잠실나루_석촌 = new Section(4L, 잠실나루, 석촌, DISTANCE2);

    public static final SectionResponse 잠실새내_잠실_응답 = new SectionResponse(잠실새내_응답, 잠실_응답, 호선2_응답, 10);
    public static final SectionResponse 잠실_잠실나루_응답 = new SectionResponse(잠실_응답, 잠실나루_응답, 호선2_응답, 15);
    public static final SectionResponse 몽촌토성_잠실나루_응답 = new SectionResponse(몽촌토성_응답, 잠실나루_응답, 호선8_응답, 10);
    public static final SectionResponse 잠실나루_석촌_응답 = new SectionResponse(잠실나루_응답, 석촌_응답, 호선8_응답, 15);
}
