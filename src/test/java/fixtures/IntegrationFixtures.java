package fixtures;

import subway.dto.LineFindResponse;
import subway.dto.StationRequest;

import java.util.List;


public class IntegrationFixtures {

    public static final long LINE2_ID = 1L;
    public static final long LINE7_ID = 3L;
    public static final String LINE2_NAME = "2호선";
    public static final String LINE7_NAME = "7호선";
    public static final String LINE8_NAME = "8호선";
    public static final Long STATION_LINE2_선릉역_ID = 1L;
    public static final Long STATION_LINE2_잠실역_ID = 2L;
    public static final Long STATION_LINE2_건대역_ID = 3L;
    public static final String STATION_선릉역_NAME = "선릉역";
    public static final String STATION_잠실역_NAME = "잠실역";
    public static final String STATION_건대역_NAME = "건대역";
    public static final String STATION_강변역_NAME = "강변역";
    public static final String STATION_대림역_NAME = "대림역";
    public static final String STATION_성수역_NAME = "성수역";
    public static final String STATION_암사역_NAME = "암사역";
    public static final String STATION_온수역_NAME = "온수역";
    public static final String STATION_철산역_NAME = "철산역";

    public static final int DISTANCE_강변역_TO_건대역 = 7;
    public static final int DISTANCE_대림역_TO_선릉역 = 5;
    public static final int DISTANCE_건대역_TO_성수역 = 15;
    public static final int DISTANCE_잠실역_TO_성수역 = 25;
    public static final int DISTANCE_온수역_TO_철산역 = 11;

    public static final StationRequest REQUEST_강변역_TO_건대역 = new StationRequest(STATION_강변역_NAME, STATION_건대역_NAME, DISTANCE_강변역_TO_건대역, LINE2_NAME);
    public static final StationRequest REQUEST_대림역_TO_선릉역 = new StationRequest(STATION_대림역_NAME, STATION_선릉역_NAME, DISTANCE_대림역_TO_선릉역, LINE2_NAME);
    public static final StationRequest REQUEST_건대역_TO_성수역 = new StationRequest(STATION_건대역_NAME, STATION_성수역_NAME, DISTANCE_건대역_TO_성수역, LINE2_NAME);
    public static final StationRequest REQUEST_잠실역_TO_성수역 = new StationRequest(STATION_잠실역_NAME, STATION_성수역_NAME, DISTANCE_잠실역_TO_성수역, LINE2_NAME);
    public static final StationRequest REQUEST_온수역_TO_철산역 = new StationRequest(STATION_온수역_NAME, STATION_철산역_NAME, DISTANCE_온수역_TO_철산역, LINE7_NAME);


    public static final LineFindResponse LINE2_노선도 = new LineFindResponse(LINE2_NAME, List.of(STATION_선릉역_NAME, STATION_잠실역_NAME, STATION_건대역_NAME));
    public static final LineFindResponse LINE7_노선도_AFTER_INITIAL_INSERT = new LineFindResponse(LINE7_NAME, List.of(STATION_온수역_NAME, STATION_철산역_NAME));
    public static final LineFindResponse LINE2_노선도_AFTER_INSERT_대림역 = new LineFindResponse(LINE2_NAME, List.of(STATION_대림역_NAME, STATION_선릉역_NAME, STATION_잠실역_NAME, STATION_건대역_NAME));
    public static final LineFindResponse LINE2_노선도_AFTER_INSERT_성수역 = new LineFindResponse(LINE2_NAME, List.of(STATION_선릉역_NAME, STATION_잠실역_NAME, STATION_건대역_NAME, STATION_성수역_NAME));
    public static final LineFindResponse LINE2_노선도_AFTER_INSERT_강변역 = new LineFindResponse(LINE2_NAME, List.of(STATION_선릉역_NAME, STATION_잠실역_NAME, STATION_강변역_NAME, STATION_건대역_NAME));
    public static final LineFindResponse LINE2_노선도_AFTER_DELETE_선릉역 = new LineFindResponse(LINE2_NAME, List.of(STATION_잠실역_NAME, STATION_건대역_NAME));
    public static final LineFindResponse LINE2_노선도_AFTER_DELETE_건대역 = new LineFindResponse(LINE2_NAME, List.of(STATION_선릉역_NAME, STATION_잠실역_NAME));
    public static final LineFindResponse LINE2_노선도_AFTER_DELETE_잠실역 = new LineFindResponse(LINE2_NAME, List.of(STATION_선릉역_NAME, STATION_건대역_NAME));
    public static final LineFindResponse LINE8_노선도 = new LineFindResponse(LINE8_NAME, List.of(STATION_잠실역_NAME, STATION_암사역_NAME));
    public static final List<LineFindResponse> ALL_LINE_노선도 = List.of(LINE2_노선도, LINE8_노선도);
}
