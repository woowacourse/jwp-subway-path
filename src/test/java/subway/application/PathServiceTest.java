package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.fee.FeePolicy;
import subway.domain.path.JgraphtPathGenerator;
import subway.domain.path.PathGenerator;
import subway.dto.PathAndFee;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    private List<Station> stations;
    private List<Section> sections;

    @Mock
    private StationDao stationDao;

    @Mock
    private SectionDao sectionDao;

    @Mock
    private FeePolicy feePolicy;

    @Mock
    private PathGenerator pathGenerator;

    @InjectMocks
    private PathService pathService;

    @BeforeEach
    void setUp() {
        Long lineId = 1L;

        stations = List.of(
                new Station(1L, "잠실"),
                new Station(2L, "잠실새내"),
                new Station(3L, "종합운동장")
        );

        sections = List.of(
                new Section(1L, stations.get(0).getId(), stations.get(1).getId(), lineId, 10),
                new Section(2L, stations.get(1).getId(), stations.get(2).getId(), lineId, 20)
        );
    }

    @DisplayName("sourceStationId, targetStationId 로 경로를 반환한다.")
    @Test
    void getShortestPathAndFee() {
        // given
        when(stationDao.findById(1L))
                .thenReturn(Optional.of(new Station(1L, "잠실")));
        when(stationDao.findById(3L))
                .thenReturn(Optional.of(new Station(3L, "종합운동장")));
        when(stationDao.findAll())
                .thenReturn(stations);
        when(sectionDao.findAll())
                .thenReturn(sections);
        when(pathGenerator.generate())
                .thenReturn(new JgraphtPathGenerator().generate());

        // when
        PathAndFee pathAndFee = pathService.findShortestPathAndFee(1L, 3L);

        // then
        assertThat(pathAndFee.getStations()).containsExactlyElementsOf(stations);
    }
}
