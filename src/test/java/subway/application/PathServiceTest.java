package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dto.PathRequest;
import subway.dto.PathResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static subway.fixture.LineFixture.LINES;
import static subway.fixture.SectionFixture.*;
import static subway.fixture.StationFixture.석촌;
import static subway.fixture.StationFixture.잠실새내;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    StationRepository stationRepository;
    @Mock
    LineRepository lineRepository;
    @InjectMocks
    PathService pathService;


    @DisplayName("출발역과 도착역에 대한 요청을 받아서 거리, 요금, 경로에 대한 값을 반환한다")
    @Test
    void 출발_도착_요청을_받아_거리_요금_경로를_반환한다() {
        //given
        PathRequest pathRequest = new PathRequest(1L, 5L);
        when(stationRepository.findById(1L)).thenReturn(잠실새내);
        when(stationRepository.findById(5L)).thenReturn(석촌);
        when(lineRepository.findAll()).thenReturn(LINES);
        //when
        PathResponse pathResponse = pathService.calculatePath(pathRequest);
        //then
        assertThat(pathResponse).usingRecursiveComparison()
                .isEqualTo(new PathResponse(40, 1850, List.of(잠실새내_잠실_응답, 잠실_잠실나루_응답, 잠실나루_석촌_응답)));
    }
}
