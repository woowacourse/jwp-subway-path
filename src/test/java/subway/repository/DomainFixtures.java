package subway.repository;

import subway.domain.line.Section;
import subway.domain.line.Station;

@SuppressWarnings("NonAsciiCharacters")
public class DomainFixtures {
    public static Section 박스터_교대_거리2_구간 = new Section("박스터역", "교대역", 2);
    public static Section 교대_강남_거리10_구간 = new Section("교대역", "강남역", 10);
    public static Section 강남_역삼_거리5_구간 = new Section("강남역", "역삼역", 5);

    public static Section 민트_서울_거리5_구간 = new Section("민트역", "서울역", 5);
    public static Section 서울_명동_거리10_구간 = new Section("서울역", "명동역", 10);
    public static Section 명동_광화문_거리7_구간 = new Section("명동역", "광화문역", 7);
    public static Section 광화문_민트_거리5_구간 = new Section("광화문역", "민트역", 5);

    // -- 2단계 --

    // 2호선
    public static Station 서초역 = new Station(1L, "서초역");
    public static Station 교대역 = new Station(2L, "교대역");
    public static Station 강남역 = new Station(3L, "강남역");
    public static Station 역삼역 = new Station(4L, "역삼역");
    public static Station 선릉역 = new Station(5L, "선릉역");
    public static Station 신사역 = new Station(6L, "신사역");
    public static Station 잠원역 = new Station(7L, "잠원역");
    public static Station 고속터미널역 = new Station(8L, "고속터미널역");
    public static Station 사평역 = new Station(9L, "사평역");
    public static Station 신논현역 = new Station(10L, "신논현역");
    public static Station 논현역 = new Station(11L, "논현역");

    public static Section 서초_교대_거리7_구간 = new Section(서초역, 교대역, 7);
    public static Section 교대_강남_거리12_구간 = new Section(교대역, 강남역, 12);
    public static Section 강남_역삼_거리8_구간 = new Section(강남역, 역삼역, 8);
    public static Section 역삼_선릉_거리12_구간 = new Section(역삼역, 선릉역, 12);

    // 3호선
    public static Section 신사_잠원_거리15_구간 = new Section(신사역, 잠원역, 15);
    public static Section 잠원_고속터미널_거리9_구간 = new Section(잠원역, 고속터미널역, 9);
    public static Section 고속터미널_교대_거리12_구간 = new Section(고속터미널역, 교대역, 12);

    // 9호선
    public static Section 고속터미널_사평_거리8_구간 = new Section(고속터미널역, 사평역, 8);
    public static Section 사평_신논현_거리11_구간 = new Section(사평역, 신논현역, 11);

    // 신분당선
    public static Section 신사_논현_거리7_구간 = new Section(신사역, 논현역, 7);
    public static Section 논현_신논현_거리8_구간 = new Section(논현역, 신논현역, 8);
    public static Section 신논현_강남_거리9_구간 = new Section(신논현역, 강남역, 9);

}
