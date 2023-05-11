package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.남위례역;
import static subway.fixture.StationFixture.복정역;
import static subway.fixture.StationFixture.산성역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.이호선;
import static subway.fixture.StationFixture.이호선_역들;
import static subway.fixture.StationFixture.잠실역;
import static subway.fixture.StationFixture.팔호선;
import static subway.fixture.StationFixture.팔호선_역들;

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
            .thenReturn(Optional.of(이호선));
        when(sectionDao.findByLineId(any()))
            .thenReturn(Collections.emptyList());
        when(stationDao.findById(any()))
            .thenReturn(Optional.of(잠실역));
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
            .thenReturn(Optional.of(이호선));
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

    @Test
    @DisplayName("입력받은 끝점이 상행 종점일 때 새로운 정보를 삽입한다.")
    void saveSection_isTargetUpward_test() {
        // given
        when(lineDao.findById(any()))
            .thenReturn(Optional.of(이호선));
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
        when(sectionDao.insert(any()))
            .thenReturn(3L);
        final SectionRequest sectionRequest = new SectionRequest(1L, 4L, 1L, 3);

        // when
        sectionService.saveSection(sectionRequest);

        // then
        verify(sectionDao, times(1)).insert(any());
    }

    @Test
    @DisplayName("입력받은 시작점이 하행 종점일 때 새로운 정보를 삽입한다.")
    void saveSection_isSourceDownward_test() {
        // given
        when(lineDao.findById(any()))
            .thenReturn(Optional.of(이호선));
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
        when(lineDao.findById(any()))
            .thenReturn(Optional.of(이호선));
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
        when(lineDao.findById(any()))
            .thenReturn(Optional.of(이호선));
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
        when(lineDao.findById(any()))
            .thenReturn(Optional.of(이호선));
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
        when(lineDao.findById(any()))
            .thenReturn(Optional.of(이호선));
        when(sectionDao.findByLineId(any()))
            .thenReturn(이호선_역들);

        doReturn(Optional.of(잠실역))
            .when(stationDao).findById(1L);
        doReturn(Optional.of(선릉역))
            .when(stationDao).findById(2L);
        doReturn(Optional.of(강남역))
            .when(stationDao).findById(3L);

        // when
        final LineResponse lineResponse = sectionService.getStationsByLineId(이호선.getId());

        // then
        assertThat(lineResponse)
            .extracting("name", "color")
            .containsExactly("이호선", "bg-green-600");

        // 잠실-신림-선릉-강남
        final List<StationResponse> stationResponses = lineResponse.getStationResponses();
        assertThat(stationResponses)
            .extracting(StationResponse::getName)
            .containsExactly("잠실역", "선릉역", "강남역");
    }
    
    @Test
    @DisplayName("모든 노선의 포함된 역 정보들을 조회한다.")
    void getAllStations_test() {
        when(lineDao.findAll())
            .thenReturn(List.of(이호선, 팔호선));

        doReturn(Optional.of(이호선))
            .when(lineDao).findById(1L);
        doReturn(Optional.of(팔호선))
            .when(lineDao).findById(2L);

        doReturn(이호선_역들)
            .when(sectionDao).findByLineId(1L);
        doReturn(팔호선_역들)
            .when(sectionDao).findByLineId(2L);

        doReturn(Optional.of(잠실역))
            .when(stationDao).findById(1L);
        doReturn(Optional.of(선릉역))
            .when(stationDao).findById(2L);
        doReturn(Optional.of(강남역))
            .when(stationDao).findById(3L);

        doReturn(Optional.of(복정역))
            .when(stationDao).findById(4L);
        doReturn(Optional.of(남위례역))
            .when(stationDao).findById(5L);
        doReturn(Optional.of(산성역))
            .when(stationDao).findById(6L);

        // when
        final List<LineResponse> responses = sectionService.getAllStations();

        // then
        assertThat(responses)
            .extracting(LineResponse::getName, LineResponse::getColor)
            .containsExactly(
                tuple("이호선", "bg-green-600")
                ,tuple("팔호선", "bg-pink-600")
            );
        assertThat(responses)
            .flatExtracting(LineResponse::getStationResponses)
            .extracting(StationResponse::getName)
            .containsExactly(
                "잠실역", "선릉역", "강남역", "복정역", "남위례역", "산성역"
            );
    }
}
