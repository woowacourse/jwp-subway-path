package subway.ui;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subway.fixture.FixtureForSubwayMapTest.동작;
import static subway.fixture.FixtureForSubwayMapTest.봉천;
import static subway.fixture.FixtureForSubwayMapTest.사당_2호선;
import static subway.fixture.FixtureForSubwayMapTest.사당_4호선;
import static subway.fixture.FixtureForSubwayMapTest.사호선;
import static subway.fixture.FixtureForSubwayMapTest.서울대입구;
import static subway.fixture.FixtureForSubwayMapTest.이호선;
import static subway.fixture.FixtureForSubwayMapTest.총신대입구;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.business.domain.line.Stations;
import subway.business.domain.subwaymap.Money;
import subway.business.service.SubwayMapService;
import subway.business.service.dto.LinePathDto;
import subway.business.service.dto.SubwayPathResponse;

@WebMvcTest(PathController.class)
class PathControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubwayMapService subwayMapService;

    @DisplayName("최단 거리 경로과 요금을 조회한다.")
    @Test
    void shouldFindFareAndShortestPathWhenRequest() throws Exception {
        Stations stationsOfLine2 = new Stations(List.of(
                서울대입구, 봉천, 사당_2호선
        ));
        Stations stationsOfLine4 = new Stations(List.of(
                사당_4호선, 총신대입구, 동작
        ));
        SubwayPathResponse subwayPathResponse = new SubwayPathResponse(Money.from("1550"), List.of(
                LinePathDto.of(이호선, stationsOfLine2),
                LinePathDto.of(사호선, stationsOfLine4)
        ));
        given(subwayMapService.findPath(2L, 15L)).willReturn(subwayPathResponse);

        mockMvc.perform(get("/paths?sourceStationId=2&targetStationId=15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fare").value(1_550))
                .andExpect(jsonPath("$.linePathDtos[0].lineId").value("1"))
                .andExpect(jsonPath("$.linePathDtos[0].lineName").value("2호선"))
                .andExpect(jsonPath("$.linePathDtos[0].linePath[0].name").value("서울대입구"))
                .andExpect(jsonPath("$.linePathDtos[1].lineId").value("3"))
                .andExpect(jsonPath("$.linePathDtos[1].lineName").value("4호선"))
                .andExpect(jsonPath("$.linePathDtos[1].linePath[2].name").value("동작"));
    }
}

