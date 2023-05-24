package subway.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static subway.fixture.SectionFixture.SECTION_ST1_ST2;
import static subway.fixture.SectionFixture.SECTION_ST2_ST3;
import static subway.fixture.StationFixture.FIXTURE_STATION_1;
import static subway.fixture.StationFixture.FIXTURE_STATION_2;
import static subway.fixture.StationFixture.FIXTURE_STATION_3;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.entity.Section;
import subway.domain.exception.RequestDataNotFoundException;
import subway.domain.vo.Distance;
import subway.dto.SectionRequest;
import subway.dto.SectionStations;

@DisplayName("지하철 구간 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationDao stationDao;

    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        sectionService = new SectionService(sectionDao, stationDao);
    }

    @DisplayName("역 등록 시 DB의 해당 노선에 대한 구간 정보를 갱신한다")
    @Test
    void addStations() {
        when(sectionDao.findByLineId(1L)).thenReturn(List.of(SECTION_ST1_ST2));
        when(stationDao.findById(1L)).thenReturn(Optional.of(FIXTURE_STATION_1));
        when(stationDao.findById(3L)).thenReturn(Optional.of(FIXTURE_STATION_3));

        sectionService.addStations(1L, new SectionRequest(new SectionStations(1L, 3L, 6), "down"));

        InOrder inOrder = inOrder(sectionDao);
        inOrder.verify(sectionDao).deleteByLineId(1L);
        inOrder.verify(sectionDao)
                .insertAllByLineId(1L, List.of(
                        new Section(FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(6)),
                        new Section(FIXTURE_STATION_3, FIXTURE_STATION_2,
                                SECTION_ST1_ST2.getDistance().minus(new Distance(6)))
                ));
    }

    @DisplayName("역 등록 시 전달받은 기준 역이 존재하지 않으면 예외를 발생한다")
    @Test
    void addStationsFailNotValidBaseStationId() {
        when(stationDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> sectionService.addStations(1L, new SectionRequest(new SectionStations(1L, 3L, 6), "down")))
                .isInstanceOf(RequestDataNotFoundException.class)
                .hasMessageContaining("기준 역: 해당 Id를 가진 역 정보가 존재하지 않습니다.");
    }

    @DisplayName("역 등록 시 전달받은 다음 역이 존재하지 않으면 예외를 발생한다")
    @Test
    void addStationsFailNotValidNextStationId() {
        when(stationDao.findById(1L)).thenReturn(Optional.of(FIXTURE_STATION_1));
        when(stationDao.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> sectionService.addStations(1L, new SectionRequest(new SectionStations(1L, 3L, 6), "down")))
                .isInstanceOf(RequestDataNotFoundException.class)
                .hasMessageContaining("다음 역: 해당 Id를 가진 역 정보가 존재하지 않습니다.");
    }

    @DisplayName("역 삭제 시 DB의 해당 노선에 대한 구간 정보를 갱신한다")
    @Test
    void deleteStation() {
        when(sectionDao.findByLineId(1L)).thenReturn(List.of(SECTION_ST1_ST2, SECTION_ST2_ST3));
        when(stationDao.findById(1L)).thenReturn(Optional.of(FIXTURE_STATION_1));

        sectionService.deleteStation(1L, 1L);

        InOrder inOrder = inOrder(sectionDao);
        inOrder.verify(sectionDao).deleteByLineId(1L);
        inOrder.verify(sectionDao)
                .insertAllByLineId(1L, List.of(SECTION_ST2_ST3));
    }

    @DisplayName("역 삭제 시 전달받은 역이 존재하지 않으면 예외를 발생한다")
    @Test
    void deleteStationFailNotValidStationId() {
        when(sectionDao.findByLineId(1L)).thenReturn(List.of(SECTION_ST1_ST2, SECTION_ST2_ST3));
        when(stationDao.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sectionService.deleteStation(1L, 3L))
                .isInstanceOf(RequestDataNotFoundException.class)
                .hasMessageContaining("해당 Id를 가진 역 정보가 존재하지 않습니다.");
    }
}
