package subway.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.exception.DuplicatedLineNameException;
import subway.repository.LineRepository;
import subway.repository.SimpleLineRepository;
import subway.repository.SimpleStationRepository;
import subway.repository.StationRepository;

class LineServiceTest {

    private LineService lineService;

    private LineRepository lineRepository;

    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        lineRepository = new SimpleLineRepository();
        stationRepository = new SimpleStationRepository();
        lineService = new LineService(lineRepository, stationRepository);
    }

    @Test
    @DisplayName("노선을 생성한다.")
    void createLine() {
        //given
        final String name = "8호선";
        final String color = "분홍색";
        final Long upStationId = stationRepository.create(new Station("잠실"));;
        final Long downStationId = stationRepository.create(new Station("건대"));
        final int distance = 7;

        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);

        //when
        Long createdId = lineService.create(lineRequest);

        //then
        Assertions.assertThat(createdId).isNotNull();
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 노선을 생성한다.")
    void createLineWithDuplicatedName() {
        //given
        final String name = "8호선";
        final String color = "분홍색";
        final Long upStationId = stationRepository.create(new Station("잠실"));;
        final Long downStationId = stationRepository.create(new Station("건대"));
        final int distance = 7;
        lineService.create(new LineRequest(name, color, upStationId, downStationId, distance));

        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);
        //when
        //then
        assertThatThrownBy(() -> lineService.create(lineRequest))
                .isInstanceOf(DuplicatedLineNameException.class);
    }
}