package subway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Station;
import subway.domain.section.EmptySections;
import subway.domain.section.Section;
import subway.domain.section.Sections;

public class TestFixture {

    public static final Line LINE_2 = new Line(1L, new LineName("2호선"));
    public static final Line LINE_3 = new Line(2L, new LineName("3호선"));
    public static final Line LINE_4 = new Line(3L, new LineName("4호선"));

    public static final Station JAMSIL = new Station(1L, "잠실");
    public static final Station GANGNAM = new Station(2L, "강남");
    public static final Station SEONGLENUG = new Station(3L, "선릉");
    public static final Station SAMSUNG = new Station(4L, "삼성");

    public static final Section SECTION_1 = new Section(1L, GANGNAM, SEONGLENUG, new Distance(5));
    public static final Section SECTION_2 = new Section(2L, SEONGLENUG, SAMSUNG, new Distance(5));

    public static final Sections SECTIONS = new EmptySections()
            .addSection(SECTION_1)
            .addSection(SECTION_2);

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String jsonSerialize(final Object request) {
        try {
            return OBJECT_MAPPER.writeValueAsString(request);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException();
        }
    }
}
