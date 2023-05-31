package subway;

import org.springframework.test.context.ActiveProfiles;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@ActiveProfiles("test")
public class SubwayFixture {
    //1호선
    public static final Station SEOULYEOK = new Station(1L, "서울역");
    public static final Station SINDORIM = new Station(2L, "신도림");
    public static final Station SICHUNG = new Station(3L, "시청");
    public static final Station JONGGAK = new Station(4L, "종각");
    public static final Station JONGROSAMGA = new Station(5L, "종로3가");
    public static final Section section1_1 = new Section(1L, SEOULYEOK, SINDORIM, new Distance(30));
    public static final Section section1_2 = new Section(1L, SINDORIM, SICHUNG, new Distance(30));
    public static final Section section1_3 = new Section(1L, SICHUNG, JONGGAK, new Distance(30));
    public static final Section section1_4 = new Section(1L, JONGGAK, JONGROSAMGA, new Distance(30));

    //2호선
    public static final Station JAMSIL = new Station(6L, "잠실");
    public static final Station GANGBYEON = new Station(7L, "강변");
    public static final Station GUUI = new Station(8L, "구의");
    public static final Station GUNDAE = new Station(9L, "건대입구");
    public static final Section section2_1 = new Section(2L, JAMSIL, SINDORIM, new Distance(5));
    public static final Section section2_2 = new Section(2L, SINDORIM, GANGBYEON, new Distance(5));
    public static final Section section2_3 = new Section(2L, GANGBYEON, GUUI, new Distance(5));
    public static final Section section2_4 = new Section(2L, GUUI, GUNDAE, new Distance(5));

    //3호선
    public static final Station CHOONGMOORO = new Station(10L, "충무로");
    public static final Station YAKSOO = new Station(11L, "약수");
    public static final Station OKSOO = new Station(12L, "옥수");
    public static final Station APGUJEONG = new Station(13L, "압구정");
    public static final Section section3_1 = new Section(3L, CHOONGMOORO, GUUI, new Distance(5));
    public static final Section section3_2 = new Section(3L, GUUI, YAKSOO, new Distance(5));
    public static final Section section3_3 = new Section(3L, YAKSOO, OKSOO, new Distance(5));
    public static final Section section3_4 = new Section(3L, OKSOO, APGUJEONG, new Distance(5));

    //4호선
    public static final Station SADANG = new Station(14L, "사당");
    public static final Station ISOO = new Station(15L, "이수");
    public static final Station DONGJAK = new Station(16L, "동작");

    private static final Section section4_1 = new Section(4L, SADANG, JONGGAK, new Distance(5));
    private static final Section section4_2 = new Section(4L, JONGGAK, ISOO, new Distance(5));
    private static final Section section4_3 = new Section(4L, ISOO, OKSOO, new Distance(5));
    private static final Section section4_4 = new Section(4L, OKSOO, DONGJAK, new Distance(5));

    public static List<Station> testStations = List.of(SEOULYEOK, SINDORIM, SICHUNG, JONGGAK, JONGROSAMGA, JAMSIL,
            GANGBYEON, GUUI, GUNDAE, CHOONGMOORO, YAKSOO, OKSOO, APGUJEONG, SADANG, ISOO, DONGJAK);

    public static List<Section> testSections = List.of(section1_1, section1_2, section1_3, section1_4,
            section2_1, section2_2, section2_3, section2_4,
            section3_1, section3_2, section3_3, section3_4,
            section4_1, section4_2, section4_3, section4_4);

    public static List<SectionEntity> testSectionEntities = testSections.stream()
            .map(section -> new SectionEntity(
                    section.getLindId(),
                    section.getUpper().getId(),
                    section.getLower().getId(),
                    section.getDistance().getValue()))
            .collect(Collectors.toList());
}


