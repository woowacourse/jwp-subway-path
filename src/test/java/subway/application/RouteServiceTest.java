package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private LineDao lineDao;
    @Mock
    private SectionDao sectionDao;
    @Mock
    private StationDao stationDao;
    private RouteService routeService;

    @BeforeEach
    void setUp() {
        routeService = new RouteService(lineDao, sectionDao, stationDao);
    }

    @DisplayName("")
    @Test
    void findShortestRoute() {
//        Mockito.when(stationDao.findById(1L)).thenReturn(Opt);
    }
}
