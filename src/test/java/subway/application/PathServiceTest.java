package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static subway.TestFeature.*;

@ExtendWith(MockitoExtension.class)
@Sql("classpath:initializeTestDb.sql")
class PathServiceTest {

    @Mock
    StationDao stationDao;

    @Mock
    SectionDao sectionDao;

    @InjectMocks
    PathService pathService;

    @DisplayName("시작 역 아이디와 끝 역 아이디를 통해 최단 경로를 조회한다")
    @Test
    void findShortestPath() {
        // given
        PathRequest pathRequest = new PathRequest(7L, 3L);
        given(stationDao.findById(7L)).willReturn(Optional.of(STATION_ENTITY_인천역));
        given(stationDao.findById(3L)).willReturn(Optional.of(STATION_ENTITY_낙성대역));
        given(stationDao.findAll()).willReturn(List.of(
                STATION_ENTITY_봉천역, STATION_ENTITY_서울대입구, STATION_ENTITY_사당역, STATION_ENTITY_방배역,
                STATION_ENTITY_인천역, STATION_ENTITY_사당역, STATION_ENTITY_낙성대역, STATION_ENTITY_동인천역
        ));
        given(sectionDao.findAll()).willReturn(List.of(
                SECTION_STATION_MAPPER_봉천_서울대입구,
                SECTION_STATION_MAPPER_서울대입구_사당,
                SECTION_STATION_MAPPER_방배_봉천,
                SECTION_STATION_MAPPER_인천_방배,
                SECTION_STATION_MAPPER_사당_동인천,
                SECTION_STATION_MAPPER_사당_낙성
        ));

        // when
        PathResponse path = pathService.findShortestPath(pathRequest);

        // then
        assertAll(
                () -> assertThat(path.getStations()).isEqualTo(List.of(
                        "인천역", "방배역", "봉천역", "서울대입구역", "사당역", "낙성대역"
                )),
                () -> assertThat(path.getDistance()).isEqualTo(30),
                () -> assertThat(path.getFare()).isEqualTo(1_650)
        );
    }
}
