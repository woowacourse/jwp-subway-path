package subway.integration.fixtures;

import subway.dto.request.StationRequest;

public class StationFixtures {
    public static final StationRequest 역삼역 = new StationRequest("역삼");
    public static final StationRequest 강남역 = new StationRequest("강남");
    public static final StationRequest 교대역 = new StationRequest("교대");
    public static final StationRequest 바뀐역 = new StationRequest("바뀐");
    public static final int 존재하지_않는_역_아이디 = 5959;


    public static final StationRequest 시청역 = new StationRequest("시청");
    public static final StationRequest 서울역 = new StationRequest("서울역");
    public static final StationRequest 용산역 = new StationRequest("용산");
    public static final StationRequest 노량진역 = new StationRequest("노량진");
}
