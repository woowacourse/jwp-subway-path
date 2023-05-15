package subway.application;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static subway.domain.SectionFixture.SECTION_MIDDLE_1;
import static subway.domain.SectionFixture.SECTION_START;
import static subway.domain.StationFixture.FIXTURE_STATION_1;
import static subway.domain.StationFixture.FIXTURE_STATION_2;
import static subway.domain.StationFixture.FIXTURE_STATION_3;
import static subway.domain.StationFixture.FIXTURE_STATION_4;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.SectionDirection;
import subway.dto.SectionRequest;
import subway.dto.SectionStations;

// TODO SubwayTest로 옮길 것 옮기기
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


    @DisplayName("빈 노선에 새로운 두 역의 노선을 저장할 수 있다.")
    @Test
    void addStations_init_success() {
        when(sectionDao.findByLineId(1L)).thenReturn(new ArrayList<>());
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);

        SectionStations sectionStations = new SectionStations(1L, 2L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations, new SectionDirection("down"));

        assertThatCode(() -> sectionService.addStations(sectionRequest))
                .doesNotThrowAnyException();
    }

    @DisplayName("비어있지 않은 노선에 두 역의 노선의 구간을 추가할 수 있다.")
    @Test
    void addStations_normal_success() {
        when(sectionDao.findByLineId(1L)).thenReturn(List.of(SECTION_START));
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);
        when(stationDao.findById(3L)).thenReturn(FIXTURE_STATION_3);

        SectionStations sectionStations = new SectionStations(2L, 3L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations, new SectionDirection("down"));

        assertThatCode(() -> sectionService.addStations(sectionRequest))
                .doesNotThrowAnyException();
    }

    @DisplayName("비어있지 않은 노선의 앞 쪽에 두 역의 노선의 구간을 추가할 수 있다.")
    @Test
    void addStations_firstInsert_success() {
        when(sectionDao.findByLineId(1L)).thenReturn(List.of(SECTION_START));
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(3L)).thenReturn(FIXTURE_STATION_3);

        SectionStations sectionStations = new SectionStations(1L, 3L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations, new SectionDirection("up"));

        assertThatCode(() -> sectionService.addStations(sectionRequest))
                .doesNotThrowAnyException();
    }

    @DisplayName("빈 노선에 동일한 두 역의 노선을 저장할 수 없다.")
    @Test
    void addStations_sameStation_fail() {
        when(sectionDao.findByLineId(1L)).thenReturn(new ArrayList<>());
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);

        SectionStations sectionStations = new SectionStations(1L, 1L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations, new SectionDirection("down"));

        assertThatThrownBy(() -> sectionService.addStations(sectionRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("기준 역과 등록할 역은 동일할 수 없습니다.");
    }

    @DisplayName("빈 노선에 동일한 두 역의 노선을 저장할 수 없다.")
    @Test
    void addStations_allExistingStation_fail() {
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START));
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);

        SectionStations sectionStations = new SectionStations(1L, 2L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations, new SectionDirection("down"));

        assertThatThrownBy(() -> sectionService.addStations(sectionRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 역이 노선에 이미 존재합니다.");
    }

    @DisplayName("등록되지 않은 역과 등록된 역의 순서로 입력한 경우 두 역의 노선을 저장할 수 없다.")
    @Test
    void addStations_afterExistingStation_fail() {
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START));
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);

        SectionStations sectionStations = new SectionStations(1L, 2L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations, new SectionDirection("down"));

        assertThatThrownBy(() -> sectionService.addStations(sectionRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 역이 노선에 이미 존재합니다.");
    }

    @DisplayName("삽입할 거리가 기존 역 사이의 길이보다 크거나 같은 경우 두 역의 노선을 저장할 수 없다.")
    @Test
    void addStations_SameOrOverDistance_fail() {
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START));
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(3L)).thenReturn(FIXTURE_STATION_3);

        SectionStations sectionStations = new SectionStations(1L, 3L, 10);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations, new SectionDirection("down"));

        assertThatThrownBy(() -> sectionService.addStations(sectionRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리는 양의 정수여야 합니다.");
    }

    @DisplayName("양 쪽에 연결된 역을 삭제할 수 있다.")
    @Test
    void deleteStationInBetween() {
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START, SECTION_MIDDLE_1));
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);

        assertThatCode(() -> sectionService.deleteStation(1L, 2L))
                .doesNotThrowAnyException();
    }

    @DisplayName("왼쪽에 연결된 역을 삭제할 수 있다.")
    @Test
    void deleteStationLeft() {
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START, SECTION_MIDDLE_1));
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);

        assertThatCode(() -> sectionService.deleteStation(1L, 1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("오른쪽에 연결된 역을 삭제할 수 있다.")
    @Test
    void deleteStationRight() {
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START, SECTION_MIDDLE_1));
        when(stationDao.findById(3L)).thenReturn(FIXTURE_STATION_3);

        assertThatCode(() -> sectionService.deleteStation(1L, 3L))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 역을 삭제할 수 없다.")
    @Test
    void deleteStationNonExisting() {
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START, SECTION_MIDDLE_1));
        when(stationDao.findById(4L)).thenReturn(FIXTURE_STATION_4);

        assertThatThrownBy(() -> sectionService.deleteStation(1L, 4L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 역이 노선에 존재하지 않습니다.");
    }

    @DisplayName("노선에 역이 두 개 있으면 모두 삭제한다.")
    @Test
    void deleteStationOnlyTwoExisting() {
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START));
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);

        assertThatCode(() -> sectionService.deleteStation(1L, 2L))
                .doesNotThrowAnyException();
    }
}
