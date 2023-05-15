package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LineServiceTest {
    @Autowired
    private LineService lineService;
    @MockBean
    private StationDao stationDao;
    @MockBean
    private LineDao lineDao;
    @MockBean
    private SectionDao sectionDao;

    @BeforeEach
    void setup() {
        final var line7 = new Line(1L, "7호선", "green");
        final var lineBD = new Line(2L, "분당선", "yellow");

        Mockito.when(lineDao.findAll())
                .thenReturn(List.of(
                        line7,
                        lineBD
                ));

        Mockito.when(sectionDao.findAllByLine(line7))
                .thenReturn(List.of(
                        new Section(1L, line7, new Station(1L, "반포역"), new Station(2L, "논현역"), Distance.of(3)),
                        new Section(2L, line7, new Station(2L, "논현역"), new Station(3L, "학동역"), Distance.of(4)),
                        new Section(3L, line7, new Station(3L, "학동역"), new Station(4L, "강남구청"), Distance.of(3)),
                        new Section(4L, line7, new Station(4L, "강남구청"), new Station(5L, "청담역"), Distance.of(4)),
                        new Section(5L, line7, new Station(5L, "청담역"), new EmptyStation(), new EmptyDistance())
                ));

        Mockito.when(sectionDao.findAllByLine(lineBD))
                .thenReturn(List.of(
                        new Section(6L, lineBD, new Station(4L, "강남구청"), new Station(6L, "압구정로데오"), Distance.of(4)),
                        new Section(7L, lineBD, new Station(6L, "압구정로데오"), new Station(7L, "서울숲"), Distance.of(5)),
                        new Section(8L, lineBD, new Station(7L, "서울숲"), new EmptyStation(), new EmptyDistance())
                ));

        Mockito.when(stationDao.findByName("반포역")).thenReturn(new Station(1L, "반포역"));
        Mockito.when(stationDao.findByName("청담역")).thenReturn(new Station(5L, "청담역"));
        Mockito.when(stationDao.findByName("논현역")).thenReturn(new Station(2L, "논현역"));
        Mockito.when(stationDao.findByName("서울숲")).thenReturn(new Station(7L, "서울숲"));
    }

    @Test
    @DisplayName("같은 노선 위에 있는 두 역의 최단거리를 구할 수 있다.")
    void shortestDistance() {
        assertThat(lineService.findShortestDistance("반포역", "청담역"))
                .isEqualTo(14);
    }

    @Test
    @DisplayName("서로 다른 노선 위에 있는 두 역의 최단거리를 구할 수 있다.")
    void shortestDistanceOnDifferentLine() {
        assertThat(lineService.findShortestDistance("논현역", "서울숲"))
                .isEqualTo(16);
    }
}