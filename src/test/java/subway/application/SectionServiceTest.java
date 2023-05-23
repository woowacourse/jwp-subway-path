package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.exception.SubwayServiceException;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.LineFixture;
import subway.domain.Section;
import subway.application.dto.SectionRequest;
import subway.application.dto.SectionStations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static subway.domain.LineFixture.FIXTURE_LINE_1;
import static subway.domain.SectionFixture.*;
import static subway.domain.StationFixture.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationDao stationDao;

    @Mock
    private LineDao lineDao;

    private SectionService sectionService;


    @BeforeEach
    void setUp() {
        sectionService = new SectionService(sectionDao, stationDao, lineDao);
    }


    @DisplayName("빈 노선에 새로운 두 역의 노선을 저장할 수 있다.")
    @Test
    void addStations_init_success() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(new ArrayList<>());
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);

        SectionStations sectionStations = new SectionStations(1L, 2L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations);

        sectionService.addSection(sectionRequest);

        assertAll(
                () -> verify(sectionDao).deleteByStationIds(1L, 1L, 2L),
                () -> verify(sectionDao).insert(1L, new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_2, new Distance(3)))
        );
    }

    @DisplayName("비어있지 않은 노선의 뒤 쪽에 두 역의 노선의 구간을 추가할 수 있다.")
    @Test
    void addStations_lastInsert_success() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(List.of(SECTION_START));
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);
        when(stationDao.findById(3L)).thenReturn(FIXTURE_STATION_3);

        SectionStations sectionStations = new SectionStations(2L, 3L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations);

        sectionService.addSection(sectionRequest);

        assertAll(
                () -> verify(sectionDao).deleteByStationIds(1L, 2L, 3L),
                () -> verify(sectionDao).insert(1L, new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_3, new Distance(3)))
        );
    }

    @DisplayName("비어있지 않은 노선의 앞 쪽에 두 역의 노선의 구간을 추가할 수 있다.")
    @Test
    void addStations_firstInsert_success() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(List.of(SECTION_MIDDLE_2));
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(3L)).thenReturn(FIXTURE_STATION_3);

        SectionStations sectionStations = new SectionStations(1L, 3L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations);

        sectionService.addSection(sectionRequest);

        assertAll(
                () -> verify(sectionDao).deleteByStationIds(1L, 1L, 3L),
                () -> verify(sectionDao).insert(1L, new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(3)))
        );
    }

    @DisplayName("비어있지 않은 노선의 중간에 두 역의 노선의 구간을 추가할 수 있다. - 새로운 노선을 왼쪽에 추가")
    @Test
    void addStations_midInsert_success_1() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(List.of(SECTION_MIDDLE_1, SECTION_MIDDLE_2));
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(3L)).thenReturn(FIXTURE_STATION_3);

        SectionStations sectionStations = new SectionStations(1L, 3L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations);

        sectionService.addSection(sectionRequest);

        assertAll(
                () -> verify(sectionDao).deleteByStationIds(1L, 2L, 3L),
                () -> verify(sectionDao).insert(1L, new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(3))),
                () -> verify(sectionDao).insert(1L, new Section(FIXTURE_LINE_1, FIXTURE_STATION_2, FIXTURE_STATION_1, new Distance(7)))
        );
    }

    @DisplayName("비어있지 않은 노선의 중간에 두 역의 노선의 구간을 추가할 수 있다. - 새로운 노선을 오른쪽에 추가")
    @Test
    void addStations_midInsert_success_2() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(List.of(SECTION_MIDDLE_1, SECTION_MIDDLE_2));
        when(stationDao.findById(3L)).thenReturn(FIXTURE_STATION_3);
        when(stationDao.findById(5L)).thenReturn(FIXTURE_STATION_5);

        SectionStations sectionStations = new SectionStations(3L, 5L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations);

        sectionService.addSection(sectionRequest);

        assertAll(
                () -> verify(sectionDao).deleteByStationIds(1L, 3L, 4L),
                () -> verify(sectionDao).insert(1L, new Section(FIXTURE_LINE_1, FIXTURE_STATION_3, FIXTURE_STATION_5, new Distance(3))),
                () -> verify(sectionDao).insert(1L, new Section(FIXTURE_LINE_1, FIXTURE_STATION_5, FIXTURE_STATION_4, new Distance(7)))
        );
    }

    @DisplayName("노선에 존재하는 두 역의 노선을 저장할 수 없다.")
    @Test
    void addStations_allExistingStation_fail() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START));
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);

        SectionStations sectionStations = new SectionStations(1L, 2L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations);

        assertThatThrownBy(() -> sectionService.addSection(sectionRequest))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("노선에 이미 존재하는 두 역을 등록할 수 없습니다.");
    }

    @DisplayName("비어있지 않은 노선에 두 역이 모두 없는 경우 두 역의 노선을 저장할 수 없다.")
    @Test
    void addStations_afterExistingStation_fail() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START));
        when(stationDao.findById(4L)).thenReturn(FIXTURE_STATION_4);
        when(stationDao.findById(5L)).thenReturn(FIXTURE_STATION_5);

        SectionStations sectionStations = new SectionStations(4L, 5L, 3);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations);

        assertThatThrownBy(() -> sectionService.addSection(sectionRequest))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("존재하지 않는 역들과의 구간을 등록할 수 없습니다.");
    }

    @DisplayName("삽입할 거리가 기존 역 사이의 길이보다 크거나 같은 경우 두 역의 노선을 저장할 수 없다.")
    @Test
    void addStations_SameOrOverDistance_fail() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START));
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(3L)).thenReturn(FIXTURE_STATION_3);

        SectionStations sectionStations = new SectionStations(1L, 3L, 10);
        SectionRequest sectionRequest = new SectionRequest(1L, sectionStations);

        assertThatThrownBy(() -> sectionService.addSection(sectionRequest))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("기존 역 사이 길이보다 크거나 같은 길이의 구간을 등록할 수 없습니다.");
    }

    @DisplayName("양 쪽에 연결된 역을 삭제할 수 있다.")
    @Test
    void deleteStationInBetween() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START, SECTION_MIDDLE_1));
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);

        sectionService.deleteStation(1L, 2L);

        assertAll(
                () -> verify(sectionDao).insert(1L, new Section(FIXTURE_LINE_1, FIXTURE_STATION_1, FIXTURE_STATION_3, new Distance(20))),
                () -> verify(sectionDao).deleteByStationId(1L, 2L)
        );
    }

    @DisplayName("왼쪽에만 연결된 역을 삭제할 수 있다.")
    @Test
    void deleteStationLeft() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START, SECTION_MIDDLE_1));
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);

        sectionService.deleteStation(1L, 1L);

        verify(sectionDao).deleteByStationId(1L, 1L);
    }

    @DisplayName("오른쪽에만 연결된 역을 삭제할 수 있다.")
    @Test
    void deleteStationRight() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START, SECTION_MIDDLE_1));
        when(stationDao.findById(3L)).thenReturn(FIXTURE_STATION_3);

        sectionService.deleteStation(1L, 3L);

        verify(sectionDao).deleteByStationId(1L, 3L);
    }

    @DisplayName("노선도에 존재하지 않는 역을 삭제할 수 없다.")
    @Test
    void deleteStationNonExisting() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START, SECTION_MIDDLE_1));
        when(stationDao.findById(4L)).thenReturn(FIXTURE_STATION_4);

        assertThatThrownBy(() -> sectionService.deleteStation(1L, 4L))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("역이 노선에 없습니다.");
    }

    @DisplayName("노선에 역이 두 개 있으면 모두 삭제한다.")
    @Test
    void deleteStationOnlyTwoExisting() {
        when(lineDao.findById(1L)).thenReturn(FIXTURE_LINE_1);
        when(sectionDao.findByLineId(1L)).thenReturn(
                List.of(SECTION_START));
        when(stationDao.findById(2L)).thenReturn(FIXTURE_STATION_2);

        assertThatCode(() -> sectionService.deleteStation(1L, 2L))
                .doesNotThrowAnyException();
    }
}
