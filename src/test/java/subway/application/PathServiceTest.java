package subway.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import subway.dao.SectionDao;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.GraphPath;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.ShortPathResponse;

class PathServiceTest {

    @Mock
    private SectionDao sectionDao;

    @Mock
    private GraphPath graphPath;

    @InjectMocks
    private PathService pathService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 출발역과_도착역까지의_경로와_거리_가격을_반환한다() {
        // given
        String startStationName = "조앤";
        String endStationName = "후추";
        Sections allSectionInfo = new Sections(List.of(
                new Section(new Station(1L, "후추"), new Station(2L, "디노"), 6),
                new Section(new Station(2L, "디노"), new Station(3L, "조앤"), 7),
                new Section(new Station(2L, "조앤"), new Station(3L, "후추"), 14)));
        List<String> shortestPath = List.of("조앤", "디노", "후추");
        Distance shortestDistance = new Distance(13);
        Fare fare = Fare.of(new Distance(13));
        ShortPathResponse expectedResponse = new ShortPathResponse(shortestPath, shortestDistance, fare);

        when(sectionDao.findAllSectionInfo()).thenReturn(allSectionInfo);
        when(graphPath.getShortestPath(allSectionInfo, startStationName, endStationName)).thenReturn(shortestPath);
        when(graphPath.getShortestDistance(allSectionInfo, startStationName, endStationName)).thenReturn(
                shortestDistance);

        // when
        ShortPathResponse actualResponse = pathService.findShortestPathInfo(startStationName, endStationName);

        // then
        assertEquals(expectedResponse, actualResponse);
        verify(sectionDao).findAllSectionInfo();
        verify(graphPath).getShortestPath(allSectionInfo, startStationName, endStationName);
        verify(graphPath).getShortestDistance(allSectionInfo, startStationName, endStationName);
    }

}

