package subway.domain;

import java.util.List;

public class Fixture {
	// 거리
	public static final Distance DISTANCE = new Distance(10);
	public static final Distance NEW_DISTANCE = new Distance(5);

	// 기존 노선의 역
	public static final Station DEPARTURE = new Station(1L, "Departure Station");
	public static final Station UP_LINE_MIDDLE_STATION = new Station(2L, "Up Line Middle Station");
	public static final Station DOWN_LINE_MIDDLE_STATION = new Station(4L, "Down Line Middle Station");
	public static final Station ARRIVAL = new Station(5L, "Arrival Station");

	// 기존 노선의 구간
	public static final Section UP_LINE_TERMINAL = new Section(1L, DEPARTURE, UP_LINE_MIDDLE_STATION, DISTANCE);
	public static final Section MIDDLE = new Section(2L, UP_LINE_MIDDLE_STATION, DOWN_LINE_MIDDLE_STATION, DISTANCE);
	public static final Section DOWN_LINE_TERMINAL = new Section(4L, DOWN_LINE_MIDDLE_STATION, ARRIVAL, DISTANCE);

	// 신규 추가 역 및 구간
	public static final Station NEW_DEPARTURE = new Station(5L, "New Departure Station");
	public static final Station NEW_ARRIVAL = new Station(6L, "New Arrival Station");
	public static final Section NEW_SECTION = new Section(11L, NEW_DEPARTURE, NEW_ARRIVAL, DISTANCE);

	// 2호선
	public static final Station 잠실역 = new Station(6L, "잠실");
	public static final Station 잠실새내역 = new Station(7L, "잠실새내");
	public static final Station 종합운동장역 = new Station(8L, "종합운동장");
	public static final Station 삼성역 = new Station(9L, "삼성");
	public static final Station 선릉역 = new Station(10L, "선릉");

	public static final Section 상행_종점_2호선 = new Section(1L, 잠실역, 잠실새내역, DISTANCE);
	public static final Section 상행_경유_2호선 = new Section(2L, 잠실새내역, 종합운동장역, DISTANCE);
	public static final Section 하행_경유_2호선 = new Section(3L, 종합운동장역, 삼성역, DISTANCE);
	public static final Section 하행_종점_2호선 = new Section(4L, 삼성역, 선릉역, DISTANCE);

	public static final List<Section> LINE_NUMBER_2 = List.of(상행_종점_2호선, 상행_경유_2호선, 하행_경유_2호선, 하행_종점_2호선);

	// 9호선

	public static final Station 선정릉역 = new Station(11L, "선정릉");
	public static final Station 봉은사역 = new Station(12L, "봉은사");
	public static final Station 석촌역 = new Station(13L, "석촌");
	public static final Station 올림픽공원역 = new Station(13L, "올림픽공원");

	public static final Section 상행_종점_9호선 = new Section(5L, 선정릉역, 봉은사역, new Distance(10));
	public static final Section 상행_경유_9호선 = new Section(6L, 봉은사역, 종합운동장역, new Distance(5));
	public static final Section 하행_경유_9호선 = new Section(7L, 종합운동장역, 석촌역, new Distance(4));
	public static final Section 하행_종점_9호선 = new Section(8L, 석촌역, 올림픽공원역, new Distance(12));

	public static final List<Section> LINE_NUMBER_9 = List.of(상행_종점_9호선, 상행_경유_9호선, 하행_경유_9호선, 하행_종점_9호선);

}
