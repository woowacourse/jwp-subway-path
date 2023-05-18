package subway.repository.dao;

import subway.entity.LineEntity;
import subway.entity.StationEntity;

@SuppressWarnings("NonAsciiCharacters")
public class DaoFixtures {

    public static LineEntity LINE_NO_1 = new LineEntity("1호선");
    public static LineEntity LINE_NO_2 = new LineEntity("2호선");

    public static StationEntity YONGSAN_STATION = new StationEntity("용산역");
    public static StationEntity NAMYOUNG_STATION = new StationEntity("남영역");
    public static StationEntity SEOUL_STATION = new StationEntity("서울역");

    public static StationEntity GANGNAM_STATION = new StationEntity("강남역");
    public static StationEntity YEOKSAM_STATION = new StationEntity("역삼역");
    public static StationEntity SEOLLEUNG_STATION = new StationEntity("선릉역");
    public static StationEntity SAMSEONG_STATION = new StationEntity("삼성역");


}
