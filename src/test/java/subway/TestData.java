package subway;

import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import java.util.List;

public class TestData {

    public static final Line 신림선 = new Line(1L, "신림선", "신림선");
    public static final Station 신림역 = new Station(1L, "신림");
    public static final Station 당곡역 = new Station(2L, "당곡");
    public static final Station 보라매병원역 = new Station(3L, "보라매병원");
    public static final Station 보라매역 = new Station(4L, "보라매");
    public static final Station 서울지방병무청역 = new Station(5L, "서울지방병무청");
    public static final Station 서원역 = new Station(6L, "서원");

    public static final Line _2호선 = new Line(2L, "2호선", "2호선");
    public static final Station 봉천역 = new Station(7L, "봉천");
    public static final Station 서울대입구역 = new Station(8L, "서울대입구");
    public static final Station 사당역 = new Station(9L, "사당");
    public static final Station 방배역 = new Station(10L, "방배");

    public static final Line _4호선 = new Line(3L, "4호선", "4호선");
    public static final Station 총신대입구역 = new Station(11L, "총신대입구");
    public static final Station 동작역 = new Station(12L, "동작");
    public static final Station 남태령역 = new Station(13L, "남태령");

    public static final Line _7호선 = new Line(4L, "7호선", "7호선");
    public static final Station 내방역 = new Station(14L, "내방");
    public static final Station 남성역 = new Station(15L, "남성");
    public static final Station 상도역 = new Station(16L, "상도");
    public static final Station 장승배기역 = new Station(17L, "장승배기");
    public static final Station 신풍역 = new Station(18L, "신풍");

    public static final Section 신림선_당곡_신림_3 = new Section(신림선, 당곡역, 신림역, new Distance(3));
    public static final Section 신림선_보라매병원_당곡_4 = new Section(신림선, 보라매병원역, 당곡역, new Distance(4));
    public static final Section 신림선_보라매_보라매병원_3 = new Section(신림선, 보라매역, 보라매병원역, new Distance(3));
    public static final Section 신림선_서울지방병무청_보라매_11 = new Section(신림선, 서울지방병무청역, 보라매역, new Distance(11));
    public static final Section 신림선_신림_서원_7 = new Section(신림선, 신림역, 서원역, new Distance(7));

    public static final Section _2호선_신림_봉천_7 = new Section(_2호선, 신림역, 봉천역, new Distance(7));
    public static final Section _2호선_봉천_서울대입구_10 = new Section(_2호선, 봉천역, 서울대입구역, new Distance(10));
    public static final Section _2호선_서울대입구_사당_6 = new Section(_2호선, 서울대입구역, 사당역, new Distance(6));
    public static final Section _2호선_사당_방배_9 = new Section(_2호선, 사당역, 방배역, new Distance(9));

    public static final Section _4호선_총신대입구_사당_5 = new Section(_4호선, 총신대입구역, 사당역, new Distance(5));
    public static final Section _4호선_동작_총신대입구_5 = new Section(_4호선, 동작역, 총신대입구역, new Distance(5));
    public static final Section _4호선_사당_남태령_3 = new Section(_4호선, 사당역, 남태령역, new Distance(3));

    public static final Section _7호선_총신대입구_내방_13 = new Section(_7호선, 총신대입구역, 내방역, new Distance(13));
    public static final Section _7호선_남성_총신대입구_2 = new Section(_7호선, 남성역, 총신대입구역, new Distance(2));
    public static final Section _7호선_상도_남성_5 = new Section(_7호선, 상도역, 남성역, new Distance(5));
    public static final Section _7호선_장승배기_상도_4 = new Section(_7호선, 장승배기역, 상도역, new Distance(4));
    public static final Section _7호선_보라매_장승배기_13 = new Section(_7호선, 보라매역, 장승배기역, new Distance(13));
    public static final Section _7호선_신풍_보라매_6 = new Section(_7호선, 신풍역, 보라매역, new Distance(6));

    public static List<Station> getAllStations() {
        return List.of(
                신림역, 당곡역, 보라매병원역, 보라매역, 서울지방병무청역, 서원역,
                봉천역, 서울대입구역, 사당역, 방배역,
                총신대입구역, 동작역, 남태령역,
                내방역, 남성역, 상도역, 장승배기역, 신풍역
        );
    }

    public static List<Line> getAllLines() {
        return List.of(신림선, _2호선, _4호선, _7호선);
    }

    public static Sections getAllSections() {
        return new Sections(List.of(
                신림선_당곡_신림_3, 신림선_보라매병원_당곡_4, 신림선_보라매_보라매병원_3, 신림선_서울지방병무청_보라매_11, 신림선_신림_서원_7,
                _2호선_신림_봉천_7, _2호선_봉천_서울대입구_10, _2호선_서울대입구_사당_6, _2호선_사당_방배_9,
                _4호선_총신대입구_사당_5, _4호선_동작_총신대입구_5, _4호선_사당_남태령_3,
                _7호선_총신대입구_내방_13, _7호선_남성_총신대입구_2, _7호선_상도_남성_5, _7호선_장승배기_상도_4, _7호선_보라매_장승배기_13, _7호선_신풍_보라매_6
        ));
    }
}
