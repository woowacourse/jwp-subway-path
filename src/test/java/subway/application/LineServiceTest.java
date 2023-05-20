package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static subway.exception.ErrorCode.LINE_NAME_DUPLICATED;
import static subway.exception.ErrorCode.LINE_NOT_FOUND;
import static subway.fixture.LineFixture.이호선;
import static subway.fixture.LineFixture.이호선_구간을_포함한_응답들;
import static subway.fixture.LineFixture.이호선_팔호선_구간을_포함한_응답들;
import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.잠실역;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.dto.LineRequest;
import subway.application.dto.LineResponse;
import subway.application.dto.SectionRequest;
import subway.application.dto.StationResponse;
import subway.domain.line.LineRepository;
import subway.domain.section.SectionRepository;
import subway.domain.station.StationRepository;
import subway.exception.BadRequestException;
import subway.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @InjectMocks
    private LineService lineService;

    @Test
    @DisplayName("이미 등록된 노선이 있으면 예외가 발생한다.")
    void save_duplicated_fail() {
        // given
        when(lineRepository.existByName(anyString()))
            .thenReturn(true);
        final LineRequest lineRequest = new LineRequest("이호선", "bg-green-600");

        // expect
        assertThatThrownBy(() -> lineService.save(lineRequest))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(LINE_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("중복되는 이름의 노선이 없으면, 노선을 저장할 수 있다.")
    void save_success() {
        // given
        when(lineRepository.existByName(anyString()))
            .thenReturn(false);
        when(lineRepository.insert(any()))
            .thenReturn(1L);
        final LineRequest lineRequest = new LineRequest("이호선", "bg-green-600");

        // expected
        assertThat(lineService.save(lineRequest))
            .isSameAs(1L);
    }

    @Test
    @DisplayName("노선 정보를 수정한다.")
    void update_duplicated_fail() {
        // given
        when(lineRepository.existByName(anyString()))
            .thenReturn(true);
        final LineRequest lineRequest = new LineRequest("이호선", "bg-green-600");

        // expect
        assertThatThrownBy(() -> lineService.update(1L, lineRequest))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(LINE_NAME_DUPLICATED);
    }

    @Test
    @DisplayName("노선 정보 수정 시 중뵉되는 이름의 요청이 들어오면 예외가 발생한다.")
    void update_success() {
        // given
        when(lineRepository.existByName(anyString()))
            .thenReturn(false);
        doNothing().when(lineRepository).updateById(anyLong(), any());
        final LineRequest lineRequest = new LineRequest("이호선", "bg-green-600");

        // expected
        assertDoesNotThrow(() -> lineService.update(1L, lineRequest));
    }

    @Test
    @DisplayName("노선 아이디를 기준으로 삭제한다.")
    void deleteById() {
        // given
        doNothing().when(lineRepository).deleteById(anyLong());

        // expected
        assertDoesNotThrow(() -> lineService.deleteById(1L));
    }

    @Test
    @DisplayName("저장할 때의 노선이 존재하지 않으면 예외가 발생한다.")
    void saveSection_non_exists_line_test() {
        // given
        when(lineRepository.findById(anyLong()))
            .thenThrow(new NotFoundException(LINE_NOT_FOUND, LINE_NOT_FOUND.getMessage()));

        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 3);

        // expected
        assertThatThrownBy(() -> lineService.saveSection(sectionRequest))
            .isInstanceOf(NotFoundException.class)
            .extracting("errorCode", "errorMessage")
            .containsExactly(LINE_NOT_FOUND, "노선 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("노선이 비어있을 때에는 상행 종점으로 저장한다.")
    void saveSection_first_upward_success_test() {
        // given
        when(lineRepository.findById(anyLong()))
            .thenReturn(이호선);
        when(lineRepository.findWithSectionsByLineId(anyLong()))
            .thenReturn(Collections.emptyList());

        doReturn(잠실역)
            .when(stationRepository).findById(1L);
        doReturn(선릉역)
            .when(stationRepository).findById(2L);

        final SectionRequest sectionRequest = new SectionRequest(1L, 1L, 2L, 3);

        // when
        lineService.saveSection(sectionRequest);

        // then
        verify(sectionRepository, times(1)).insert(any());
    }

    @Test
    @DisplayName("입력받은 끝점이 상행 종점일 때 새로운 정보를 삽입한다.")
    void saveSection_isTargetUpward_test() {
        // given
        when(lineRepository.findById(anyLong()))
            .thenReturn(이호선);
        when(lineRepository.findWithSectionsByLineId(anyLong()))
            .thenReturn(이호선_구간을_포함한_응답들());

        doReturn(잠실역)
            .when(stationRepository).findById(1L);
        doReturn(신림역)
            .when(stationRepository).findById(4L);

        when(sectionRepository.insert(any()))
            .thenReturn(3L);

        final SectionRequest sectionRequest = new SectionRequest(1L, 4L, 1L, 3);

        // when
        lineService.saveSection(sectionRequest);

        // then
        verify(sectionRepository, times(1)).insert(any());
    }

    @Test
    @DisplayName("입력받은 시작점이 하행 종점일 때 새로운 정보를 삽입한다.")
    void saveSection_isSourceDownward_test() {
        // given
        when(lineRepository.findById(anyLong()))
            .thenReturn(이호선);
        when(lineRepository.findWithSectionsByLineId(anyLong()))
            .thenReturn(이호선_구간을_포함한_응답들());

        doReturn(강남역)
            .when(stationRepository).findById(3L);
        doReturn(신림역)
            .when(stationRepository).findById(4L);

        final SectionRequest sectionRequest = new SectionRequest(1L, 3L, 4L, 3);

        // when
        lineService.saveSection(sectionRequest);

        // then
        verify(sectionRepository, times(1)).insert(any());
    }

    @Test
    @DisplayName("입력받은 시작점이 노선 구간의 시작점에 존재할 때, 기존 구간을 제거하고, 새로운 구간을 삽입한다")
    void saveSection_updateExistedSourceSection_test() {
        // given
        when(lineRepository.findById(anyLong()))
            .thenReturn(이호선);
        when(lineRepository.findWithSectionsByLineId(anyLong()))
            .thenReturn(이호선_구간을_포함한_응답들());

        doReturn(선릉역)
            .when(stationRepository).findById(2L);
        doReturn(신림역)
            .when(stationRepository).findById(4L);

        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 4L, 3);

        // when
        lineService.saveSection(sectionRequest);

        // then
        verify(sectionRepository, times(2)).insert(any());
        verify(sectionRepository, times(1)).deleteOldSection(any(), any());
    }

    @Test
    @DisplayName("입력받은 끝점이 노선 구간의 끝점에 존재할 때, 기존 구간을 제거하고, 새로운 구간을 삽입한다")
    void saveSection_updateExistedTargetSection_test() {
        // given
        when(lineRepository.findById(anyLong()))
            .thenReturn(이호선);
        when(lineRepository.findWithSectionsByLineId(anyLong()))
            .thenReturn(이호선_구간을_포함한_응답들());

        doReturn(선릉역)
            .when(stationRepository).findById(2L);
        doReturn(신림역)
            .when(stationRepository).findById(4L);

        final SectionRequest sectionRequest = new SectionRequest(1L, 4L, 2L, 3);

        // when
        lineService.saveSection(sectionRequest);

        // then
        verify(sectionRepository, times(2)).insert(any());
        verify(sectionRepository, times(1)).deleteOldSection(any(), any());
    }

    @Test
    @DisplayName("특정 노선에 있는 역을 지우고, 갱신된 구간 정보를 업데이트한다.")
    void deleteByLineIdAndStationId() {
        // given
        when(lineRepository.findById(anyLong()))
            .thenReturn(이호선);
        when(lineRepository.findWithSectionsByLineId(anyLong()))
            .thenReturn(이호선_구간을_포함한_응답들());

        // when
        lineService.deleteByLineIdAndStationId(1L, 2L);

        // then
        verify(sectionRepository, times(1)).deleteByLineIdAndStationId(any(), any());
        verify(sectionRepository, times(1)).insert(any());
    }

    @Test
    @DisplayName("노선에 포함된 역을 순서대로 조회한다.")
    void getByLineId() {
        // given
        when(lineRepository.findById(anyLong()))
            .thenReturn(이호선);
        when(lineRepository.findWithSectionsByLineId(anyLong()))
            .thenReturn(이호선_구간을_포함한_응답들());

        // when
        final LineResponse lineResponse = lineService.getByLineId(1L);

        // then
        assertThat(lineResponse)
            .extracting("name", "color")
            .containsExactly("이호선", "bg-green-600");

        final List<StationResponse> stationResponses = lineResponse.getStationResponses();
        assertThat(stationResponses)
            .extracting(StationResponse::getName)
            .containsExactly("잠실역", "선릉역", "강남역");
    }

    @Test
    @DisplayName("모든 노선의 포함된 역 정보들을 순서대로 조회한다.")
    void getAllLines() {
        when(lineRepository.findAllWithSections())
            .thenReturn(이호선_팔호선_구간을_포함한_응답들());

        // when
        final List<LineResponse> responses = lineService.getAllLines();

        // then
        assertThat(responses)
            .extracting(LineResponse::getName, LineResponse::getColor)
            .containsExactly(
                tuple("이호선", "bg-green-600"),
                tuple("팔호선", "bg-pink-600"));

        assertThat(responses)
            .flatExtracting(LineResponse::getStationResponses)
            .extracting(StationResponse::getName)
            .containsExactly(
                "잠실역", "선릉역", "강남역", "복정역", "남위례역", "산성역");
    }
}
