package subway.repository;

import subway.domain.Section;

@SuppressWarnings("NonAsciiCharacters")
public class DomainFixtures {
    public static Section 박스터_교대_거리2_구간 = new Section("박스터역", "교대역", 2);
    public static Section 교대_강남_거리10_구간 = new Section("교대역", "강남역", 10);
    public static Section 강남_역삼_거리5_구간 = new Section("강남역", "역삼역", 5);

    public static Section 민트_서울_거리5_구간 = new Section("민트역", "서울역", 5);
    public static Section 서울_명동_거리10_구간 = new Section("서울역", "명동역", 10);
    public static Section 명동_광화문_거리7_구간 = new Section("명동역", "광화문역", 7);
    public static Section 광화문_민트_거리5_구간 = new Section("광화문역", "민트역", 5);

}
