package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.exception.DuplicatedLineNameException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.ui.dto.LineRequest;
import subway.ui.dto.StationInsertRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@Transactional
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;


    @Test
    @DisplayName("노선을 생성한다.")
    void createLine() {
        //given
        LineRequest lineRequest = createLineRequest();

        //when
        Long createdId = lineService.create(lineRequest);

        //then
        assertThat(createdId).isNotNull();
    }

    private LineRequest createLineRequest() {
        final String name = "8호선";
        final String color = "분홍색";
        final Long upStationId = stationRepository.create(new Station("잠실"));
        final Long downStationId = stationRepository.create(new Station("건대"));
        final int distance = 7;

        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 노선을 생성한다.")
    void createLineWithDuplicatedName() {
        //given
        lineService.create(createLineRequest());
        LineRequest lineRequest = createLineRequest();

        //when
        //then
        assertThatThrownBy(() -> lineService.create(lineRequest))
                .isInstanceOf(DuplicatedLineNameException.class);
    }

    @Test
    @DisplayName("노선에 역을 추가한다.")
    void insertStation() {
        //given
        LineRequest lineRequest = createLineRequest();
        Long lineId = lineService.create(lineRequest);
        Long stationId = stationRepository.create(new Station("강남"));

        StationInsertRequest stationInsertRequest = new StationInsertRequest(
                stationId,
                lineId,
                lineRequest.getUpStationId(),
                "DOWN",
                1);
        //when
        lineService.insertStation(stationInsertRequest);

        //then
        Line line = lineRepository.findByName(lineRequest.getName()).get();
        final List<Long> stationIdsByOrder = line.getStationIdsByOrder();
        assertThat(stationIdsByOrder).containsExactly(lineRequest.getUpStationId(), stationId, lineRequest.getDownStationId());

    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void deleteStation() {
        //given (up - 강남 - down)
        LineRequest lineRequest = createLineRequest();
        Long lineId = lineService.create(lineRequest);
        Long stationId = stationRepository.create(new Station("강남"));
        StationInsertRequest stationInsertRequest = new StationInsertRequest(stationId, lineId,
                lineRequest.getUpStationId(), "DOWN",
                1);
        lineService.insertStation(stationInsertRequest);

        //when
        lineService.deleteStation(lineId, stationId);

        //then
        Line line = lineRepository.findById(lineId).get();
        final List<Long> stationIdsByOrder = line.getStationIdsByOrder();
        assertThat(stationIdsByOrder).containsExactly(lineRequest.getUpStationId(), lineRequest.getDownStationId());
    }

    @Test
    @DisplayName("역이 2개인 노선에서 역을 제거한다.")
    void deleteStationWithTwoStation() {
        //given
        LineRequest lineRequest = createLineRequest();
        Long lineId = lineService.create(lineRequest);

        //when
        lineService.deleteStation(lineId, lineRequest.getDownStationId());

        //then
        assertThat(lineRepository.findById(lineId)).isEmpty();
    }

    @Test
    @DisplayName("id로 노선을 찾는다.")
    void findById() {
        //given
        LineRequest lineRequest = createLineRequest();
        Long lineId = lineService.create(lineRequest);

        //when
        Line createdLine = lineService.findLineById(lineId);

        //then
        assertThat(createdLine.getId()).isEqualTo(lineId);
    }

    @Test
    @DisplayName("전체 노선을 찾는다.")
    void findAll() {
        //given
        LineRequest lineRequest = createLineRequest();
        Long firstLineId = lineService.create(lineRequest);

        Long secondLineId = lineService.create(new LineRequest(
                "2호선",
                "초록색",
                stationRepository.create(new Station("up")),
                stationRepository.create(new Station("down")),
                5
        ));

        //when
        List<Line> lines = lineService.findAll();

        //then
        assertSoftly(softly -> {
            softly.assertThat(lines).hasSize(2);
            softly.assertThat(lines.get(0).getId()).isEqualTo(firstLineId);
            softly.assertThat(lines.get(1).getId()).isEqualTo(secondLineId);
        });

    }
}
