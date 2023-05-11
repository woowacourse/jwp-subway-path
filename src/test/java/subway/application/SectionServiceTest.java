package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.StationResponse;


@ExtendWith(MockitoExtension.class)
class SectionServiceTest {


    @Mock
    private SectionDao sectionDao;

    @Mock
    private LineDao lineDao;

    @Mock
    private StationDao stationDao;

    @InjectMocks
    private SectionService sectionService;

    @Test
    @DisplayName("저장할 때의 노선이 존재하지 않으면 예외가 발생한다.")
    void saveSection_non_exists_line_test() {
        when(lineDao.findById(any()))
            .thenReturn(Optional.empty());

        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 3);

        // expected
        assertThatThrownBy(() -> sectionService.saveSection(sectionRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당하는 노선이 없습니다.");
    }

    @Test
    @DisplayName("노선이 비어있을 때에는 상행 종점으로 저장한다.")
    void saveSection_first_upward_success_test() {
        // given
        when(lineDao.findById(any()))
            .thenReturn(Optional.of(new Line("1호선", "blue")));
        when(sectionDao.findByLineId(any()))
            .thenReturn(Collections.emptyList());
        when(stationDao.findById(any()))
            .thenReturn(Optional.of(new Station("잠실역")));
        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 3);


        // when
        sectionService.saveSection(sectionRequest);

        // then
        verify(sectionDao, times(1)).insert(any());
    }


    @Test
    @DisplayName("해당하는 역이 없으면 예외가 발생한다.")
    void saveSection_non_exists_station_error_test() {
        // given
        when(lineDao.findById(any()))
            .thenReturn(Optional.of(new Line("1호선", "blue")));
        when(sectionDao.findByLineId(any()))
            .thenReturn(Collections.emptyList());
        when(stationDao.findById(any()))
            .thenReturn(Optional.empty());
        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 3);

        // expected
        assertThatThrownBy(() -> sectionService.saveSection(sectionRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당하는 역이 없습니다.");
    }

    @ParameterizedTest(name = "구간 타입 업데이트 시 업데이트 한 행 횟수가 1이 아니면 오류가 발생한다.")
    @ValueSource(ints = {0, 2})
    void saveSection_update_count_exception_test(final int updatedCount) {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");

        when(lineDao.findById(any()))
            .thenReturn(Optional.of(신분당선));

        final Station 잠실역 = new Station(1L,"잠실역");
        final Station 선릉역 = new Station(2L, "선릉역");
        final Station 강남역 = new Station(3L, "강남역");
        final Station 신림역 = new Station(4L, "신림역");

        final SectionEntity 잠실_선릉 = new SectionEntity(1L, 1L, 2L, 10, "UPWARD");
        final SectionEntity 선릉_강남 = new SectionEntity(1L, 2L, 3L, 10, "NORMAL");
        final List<SectionEntity> 이호선_역들 = List.of(잠실_선릉, 선릉_강남);
        when(sectionDao.findByLineId(any()))
            .thenReturn(이호선_역들);

        doReturn(Optional.of(잠실역))
            .when(stationDao).findById(1L);
        doReturn(Optional.of(선릉역))
            .when(stationDao).findById(2L);
        doReturn(Optional.of(강남역))
            .when(stationDao).findById(3L);
        doReturn(Optional.of(신림역))
            .when(stationDao).findById(4L);

        when(sectionDao.updateSectionTypeByLineIdAndSourceStationId(any(), any(), any()))
            .thenReturn(updatedCount);

        final SectionRequest sectionRequest = new SectionRequest(1L, 4L, 1L, 3);

        // expected
        assertThatThrownBy(() -> sectionService.saveSection(sectionRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("DB 업데이트 중 예외가 발생했습니다.");
    }

    @Test
    @DisplayName("입력받은 끝점이 상행 종점일 때 구간 타입을 업데이트하고, 새로운 정보를 삽입한다.")
    void saveSection_isTargetUpward_test() {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");

        when(lineDao.findById(any()))
            .thenReturn(Optional.of(신분당선));

        final Station 잠실역 = new Station(1L,"잠실역");
        final Station 선릉역 = new Station(2L, "선릉역");
        final Station 강남역 = new Station(3L, "강남역");
        final Station 신림역 = new Station(4L, "신림역");

        final SectionEntity 잠실_선릉 = new SectionEntity(1L, 1L, 2L, 10, "UPWARD");
        final SectionEntity 선릉_강남 = new SectionEntity(1L, 2L, 3L, 10, "NORMAL");
        final List<SectionEntity> 이호선_역들 = List.of(잠실_선릉, 선릉_강남);
        when(sectionDao.findByLineId(any()))
            .thenReturn(이호선_역들);

        doReturn(Optional.of(잠실역))
            .when(stationDao).findById(1L);
        doReturn(Optional.of(선릉역))
            .when(stationDao).findById(2L);
        doReturn(Optional.of(강남역))
            .when(stationDao).findById(3L);
        doReturn(Optional.of(신림역))
            .when(stationDao).findById(4L);

        when(sectionDao.updateSectionTypeByLineIdAndSourceStationId(any(), any(), any()))
            .thenReturn(1);
        when(sectionDao.insert(any()))
            .thenReturn(3L);
        final SectionRequest sectionRequest = new SectionRequest(1L, 4L, 1L, 3);

        // when
        sectionService.saveSection(sectionRequest);

        // then
        verify(sectionDao, times(1)).updateSectionTypeByLineIdAndSourceStationId(any(), any(), any());
        verify(sectionDao, times(1)).insert(any());
    }

    @Test
    @DisplayName("입력받은 시작점이 하행 종점일 때 구간 타입을 업데이트하고, 새로운 정보를 삽입한다.")
    void saveSection_isSourceDownward_test() {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");

        when(lineDao.findById(any()))
            .thenReturn(Optional.of(신분당선));

        final Station 잠실역 = new Station(1L,"잠실역");
        final Station 선릉역 = new Station(2L, "선릉역");
        final Station 강남역 = new Station(3L, "강남역");
        final Station 신림역 = new Station(4L, "신림역");

        final SectionEntity 잠실_선릉 = new SectionEntity(1L, 1L, 2L, 10, "UPWARD");
        final SectionEntity 선릉_강남 = new SectionEntity(1L, 2L, 3L, 10, "NORMAL");
        final List<SectionEntity> 이호선_역들 = List.of(잠실_선릉, 선릉_강남);
        when(sectionDao.findByLineId(any()))
            .thenReturn(이호선_역들);

        doReturn(Optional.of(잠실역))
            .when(stationDao).findById(1L);
        doReturn(Optional.of(선릉역))
            .when(stationDao).findById(2L);
        doReturn(Optional.of(강남역))
            .when(stationDao).findById(3L);
        doReturn(Optional.of(신림역))
            .when(stationDao).findById(4L);
        final SectionRequest sectionRequest = new SectionRequest(1L, 3L, 4L, 3);

        // when
        sectionService.saveSection(sectionRequest);

        // then
        verify(sectionDao, times(1)).insert(any());
    }

    @ParameterizedTest(name = "기존 구간 제거 시 제거한 행 횟수가 1이 아니면 예외가 발생한다.")
    @ValueSource(ints = {0, 2})
    void saveSection_delete_count_exception_test(final int deletedCount) {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");

        when(lineDao.findById(any()))
            .thenReturn(Optional.of(신분당선));

        final Station 잠실역 = new Station(1L,"잠실역");
        final Station 선릉역 = new Station(2L, "선릉역");
        final Station 강남역 = new Station(3L, "강남역");
        final Station 신림역 = new Station(4L, "신림역");

        final SectionEntity 잠실_선릉 = new SectionEntity(1L, 1L, 2L, 10, "UPWARD");
        final SectionEntity 선릉_강남 = new SectionEntity(1L, 2L, 3L, 10, "NORMAL");
        final List<SectionEntity> 이호선_역들 = List.of(잠실_선릉, 선릉_강남);
        when(sectionDao.findByLineId(any()))
            .thenReturn(이호선_역들);

        doReturn(Optional.of(잠실역))
            .when(stationDao).findById(1L);
        doReturn(Optional.of(선릉역))
            .when(stationDao).findById(2L);
        doReturn(Optional.of(강남역))
            .when(stationDao).findById(3L);
        doReturn(Optional.of(신림역))
            .when(stationDao).findById(4L);

        when(sectionDao.deleteByLineIdAndSourceStationId(any(), any()))
            .thenReturn(deletedCount);

        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 4L, 3);

        // expected
        assertThatThrownBy(() ->  sectionService.saveSection(sectionRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("DB 삭제가 정상적으로 진행되지 않았습니다.");
    }

    @Test
    @DisplayName("입력받은 시작점이 노선 구간의 시작점에 존재할 때, 기존 구간을 제거하고, 새로운 구간을 삽입한다")
    void saveSection_updateExistedSourceSection_test() {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");

        when(lineDao.findById(any()))
            .thenReturn(Optional.of(신분당선));

        final Station 잠실역 = new Station(1L,"잠실역");
        final Station 선릉역 = new Station(2L, "선릉역");
        final Station 강남역 = new Station(3L, "강남역");
        final Station 신림역 = new Station(4L, "신림역");

        final SectionEntity 잠실_선릉 = new SectionEntity(1L, 1L, 2L, 10, "UPWARD");
        final SectionEntity 선릉_강남 = new SectionEntity(1L, 2L, 3L, 10, "NORMAL");
        final List<SectionEntity> 이호선_역들 = List.of(잠실_선릉, 선릉_강남);
        when(sectionDao.findByLineId(any()))
            .thenReturn(이호선_역들);

        doReturn(Optional.of(잠실역))
            .when(stationDao).findById(1L);
        doReturn(Optional.of(선릉역))
            .when(stationDao).findById(2L);
        doReturn(Optional.of(강남역))
            .when(stationDao).findById(3L);
        doReturn(Optional.of(신림역))
            .when(stationDao).findById(4L);

        when(sectionDao.deleteByLineIdAndSourceStationId(any(), any()))
            .thenReturn(1);

        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 4L, 3);

        // when
        sectionService.saveSection(sectionRequest);

        // then
        verify(sectionDao, times(2)).insert(any());
        verify(sectionDao, times(1)).deleteByLineIdAndSourceStationId(any(), any());
    }

    @Test
    @DisplayName("입력받은 끝점이 노선 구간의 끝점에 존재할 때, 기존 구간을 제거하고, 새로운 구간을 삽입한다")
    void saveSection_updateExistedTargetSection_test() {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");

        when(lineDao.findById(any()))
            .thenReturn(Optional.of(신분당선));

        final Station 잠실역 = new Station(1L,"잠실역");
        final Station 선릉역 = new Station(2L, "선릉역");
        final Station 강남역 = new Station(3L, "강남역");
        final Station 신림역 = new Station(4L, "신림역");

        final SectionEntity 잠실_선릉 = new SectionEntity(1L, 1L, 2L, 10, "UPWARD");
        final SectionEntity 선릉_강남 = new SectionEntity(1L, 2L, 3L, 10, "NORMAL");
        final List<SectionEntity> 이호선_역들 = List.of(잠실_선릉, 선릉_강남);
        when(sectionDao.findByLineId(any()))
            .thenReturn(이호선_역들);

        doReturn(Optional.of(잠실역))
            .when(stationDao).findById(1L);
        doReturn(Optional.of(선릉역))
            .when(stationDao).findById(2L);
        doReturn(Optional.of(강남역))
            .when(stationDao).findById(3L);
        doReturn(Optional.of(신림역))
            .when(stationDao).findById(4L);

        when(sectionDao.deleteByLineIdAndSourceStationId(any(), any()))
            .thenReturn(1);

        final SectionRequest sectionRequest = new SectionRequest(1L, 4L, 2L, 3);

        // when
        sectionService.saveSection(sectionRequest);

        // then
        verify(sectionDao, times(2)).insert(any());
        verify(sectionDao, times(1)).deleteByLineIdAndSourceStationId(any(), any());
    }

    @Test
    @DisplayName("노선에 포함된 역을 순서대로 조회한다.")
    void getStationsByLineId() {
        // given
        final Line 신분당선 = new Line("신분당선", "bg-red-600");

        when(lineDao.findById(any()))
            .thenReturn(Optional.of(신분당선));

        final Station 잠실역 = new Station(1L,"잠실역");
        final Station 선릉역 = new Station(2L, "선릉역");
        final Station 강남역 = new Station(3L, "강남역");

        final SectionEntity 잠실_선릉 = new SectionEntity(1L, 1L, 2L, 10, "UPWARD");
        final SectionEntity 선릉_강남 = new SectionEntity(1L, 2L, 3L, 10, "NORMAL");
        final List<SectionEntity> 이호선_역들 = List.of(잠실_선릉, 선릉_강남);
        when(sectionDao.findByLineId(any()))
            .thenReturn(이호선_역들);

        doReturn(Optional.of(잠실역))
            .when(stationDao).findById(1L);
        doReturn(Optional.of(선릉역))
            .when(stationDao).findById(2L);
        doReturn(Optional.of(강남역))
            .when(stationDao).findById(3L);

        // when
        final LineResponse lineResponse = sectionService.getStationsByLineId(신분당선.getId());

        // then
        assertThat(lineResponse)
            .extracting("name", "color")
            .containsExactly("신분당선", "bg-red-600");

        // 잠실-신림-선릉-강남
        final List<StationResponse> stationResponses = lineResponse.getStationResponses();
        assertThat(stationResponses)
            .extracting(StationResponse::getName)
            .containsExactly("잠실역", "선릉역", "강남역");
    }
}
