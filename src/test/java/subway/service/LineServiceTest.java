package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.StationEdge;
import subway.exception.DuplicatedLineNameException;
import subway.exception.NoSuchStationException;
import subway.exception.StationAlreadyExistsException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.dto.service.CreateLineServiceCommand;
import subway.dto.service.InsertStationServiceCommand;

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
        CreateLineServiceCommand lineRequest = createLineRequest();

        //when
        Long createdId = lineService.create(lineRequest);

        //then
        assertThat(createdId).isNotNull();
    }

    private CreateLineServiceCommand createLineRequest() {
        final String name = "8호선";
        final String color = "분홍색";
        final Long upStationId = stationRepository.create(new Station("잠실"));
        final Long downStationId = stationRepository.create(new Station("건대"));
        final int distance = 7;

        CreateLineServiceCommand lineRequest = new CreateLineServiceCommand(name, color, upStationId, downStationId, distance);
        return lineRequest;
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 노선을 생성한다.")
    void createLineWithDuplicatedName() {
        //given
        lineService.create(createLineRequest());
        CreateLineServiceCommand lineRequest = createLineRequest();

        //when
        //then
        assertThatThrownBy(() -> lineService.create(lineRequest))
                .isInstanceOf(DuplicatedLineNameException.class);
    }

    @Test
    @DisplayName("노선에 역을 추가한다.")
    void insertStation() {
        //given
        CreateLineServiceCommand lineRequest = createLineRequest();
        Long lineId = lineService.create(lineRequest);

        Long stationId = stationRepository.create(new Station("강남"));

        // upstation 에서 1 떨어진 곳에 추가....
        InsertStationServiceCommand stationInsertRequest = new InsertStationServiceCommand(stationId, lineId,
                lineRequest.getUpStationId(), "DOWN",
                1);
        //when
        lineService.insertStation(stationInsertRequest);

        //then
        Line line = lineRepository.findByName(lineRequest.getName()).get();
        assertThat(line.getStationEdges().get(1).getDownStationId()).isEqualTo(stationId);

        StationEdge updatedStationEdge = line.getStationEdges().stream()
                .filter(stationEdge -> stationEdge.getDownStationId().equals(lineRequest.getDownStationId()))
                .findFirst().get();
        assertThat(updatedStationEdge.getDistance().getValue()).isEqualTo(6);
    }

    @Test
    @DisplayName("존재하지 않는 역을 추가한다.")
    void insertNullStation() {
        //given
        CreateLineServiceCommand lineRequest = createLineRequest();
        Long generatedLineId = lineService.create(lineRequest);

        //when
        //then
        assertThatThrownBy(() -> lineService.insertStation(
                new InsertStationServiceCommand(1000L, generatedLineId, lineRequest.getUpStationId(), "DOWN", 1)))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("이미 등록된 역을 노선에 추가한다.")
    void insertAlreadyExistingStation() {
        //given
        CreateLineServiceCommand lineRequest = createLineRequest();
        Long generatedLineId = lineService.create(lineRequest);
        Long stationId = stationRepository.create(new Station("강남"));
        InsertStationServiceCommand stationInsertRequest = new InsertStationServiceCommand(stationId, generatedLineId,
                lineRequest.getUpStationId(), "DOWN",
                1);
        lineService.insertStation(stationInsertRequest);

        //when
        //then
        assertThatThrownBy(() -> lineService.insertStation(stationInsertRequest))
                .isInstanceOf(StationAlreadyExistsException.class);
    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void deleteStation() {
        //given up - 강남 - down
        CreateLineServiceCommand lineRequest = createLineRequest();
        Long lineId = lineService.create(lineRequest);
        Long stationId = stationRepository.create(new Station("강남"));
        InsertStationServiceCommand stationInsertRequest = new InsertStationServiceCommand(stationId, lineId,
                lineRequest.getUpStationId(), "DOWN",
                1);
        lineService.insertStation(stationInsertRequest);

        //when
        lineService.deleteStation(lineId, stationId);

        //then
        Line line = lineRepository.findById(lineId).get();
        StationEdge downEndStationEdge = line.getStationEdges().get(1);
        assertSoftly(softly -> {
            softly.assertThat(downEndStationEdge.getDownStationId()).isEqualTo(lineRequest.getDownStationId());
            softly.assertThat(downEndStationEdge.getDistance().getValue()).isEqualTo(lineRequest.getDistance());
        });
    }

    @Test
    @DisplayName("등록되어있지 않은 역을 노선에서 삭제한다.")
    void deleteNotExistingStation() {
        //given
        CreateLineServiceCommand lineRequest = createLineRequest();
        Long lineId = lineService.create(lineRequest);
        Long stationId = stationRepository.create(new Station("강남"));
        InsertStationServiceCommand stationInsertRequest = new InsertStationServiceCommand(stationId, lineId,
                lineRequest.getUpStationId(), "DOWN",
                1);
        lineService.insertStation(stationInsertRequest);

        Long notExistingStationId = stationRepository.create(new Station("혜화"));

        //when
        //then
        assertThatThrownBy(() -> lineService.deleteStation(lineId, notExistingStationId))
                .isInstanceOf(NoSuchStationException.class);
    }

    @Test
    @DisplayName("역이 2개인 노선에서 역을 제거한다.")
    void deleteStationWithTwoStation() {
        //given
        CreateLineServiceCommand lineRequest = createLineRequest();
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
        CreateLineServiceCommand lineRequest = createLineRequest();
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
        CreateLineServiceCommand lineRequest = createLineRequest();
        Long firstLineId = lineService.create(lineRequest);

        Long secondLineId = lineService.create(new CreateLineServiceCommand(
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