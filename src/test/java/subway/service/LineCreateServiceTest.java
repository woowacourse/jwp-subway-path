package subway.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import subway.dao.DbEdgeDao;
import subway.dao.DbLineDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.SubwayGraphs;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.entity.EdgeEntity;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LineCreateServiceTest {

    @Mock
    private StationDao stationDao;

    @Mock
    private DbLineDao dbLineDao;

    @Mock
    private SubwayGraphs subwayGraphs;

    @Mock
    private DbEdgeDao dbEdgeDao;

    @InjectMocks
    private LineCreateService lineCreateService;

    @Test
    void createLine() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest(
                "2호선", "잠실새내역", "잠실역", 5);
        LineEntity lineEntity = new LineEntity(1L, "2호선");
        StationEntity stationEntity1 = new StationEntity(1L, "잠실역");
        StationEntity stationEntity2 = new StationEntity(2L, "잠실새내역");

        when(stationDao.saveStation(any())).thenReturn(stationEntity1, stationEntity2);
        when(dbLineDao.saveLine(any())).thenReturn(lineEntity);

        LineResponse result = lineCreateService.createLine(lineCreateRequest);

        Assertions.assertThat(result.getId()).isEqualTo(1L);
        Assertions.assertThat(result.getName()).isEqualTo("2호선");
        Assertions.assertThat(result.getStations()).containsExactly(
                new StationResponse(1L, "잠실역"),
                new StationResponse(2L, "잠실새내역"));
    }
}
