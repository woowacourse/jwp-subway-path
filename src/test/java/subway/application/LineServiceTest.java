package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.LineRequest;
import subway.dto.LineStationRequest;
import subway.dto.LineStationResponse;
import subway.dto.StationResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private LineDao lineDao;

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationDao stationDao;

    @Mock
    private LineRepository lineRepository;

    private LineService lineService;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineDao, sectionDao, stationDao, lineRepository);
    }

    @DisplayName("Line이 정상적으로 저장되고 lineId 값을 반환한다.")
    @Test
    void save() {
        // given
        LineRequest lineRequest = new LineRequest("1호선", "bg-blue-500");
        LineEntity lineEntity = new LineEntity(1L, lineRequest.getName(), lineRequest.getColor());
        given(lineDao.insert(any(LineEntity.class))).willReturn(lineEntity);

        // when, then
        assertThat(lineService.save(lineRequest)).isEqualTo(1L);
    }

    @DisplayName("Line에 등록할 구간이 동일한 역인 경우 IllegalArgumentException 예외가 발생한다.")
    @Test
    void saveStationInLine_ValidateDifferentStation() {
        // given
        LineStationRequest request = new LineStationRequest(1L, 1L, 5);
        LineEntity lineEntity = new LineEntity(1L, "1호선", "bg-blue-500");
        given(lineDao.findById(anyLong())).willReturn(Optional.of(lineEntity));

        // when & then
        assertThatThrownBy(() -> lineService.saveStationInLine(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간은 서로 다른 역이여야 합니다.");
    }

    @DisplayName("Line에 등록할 구간의 역이 Station에 존재하지 않는 경우 NoSuchElementException 예외가 발생한다. ")
    @Test
    void saveStationInLine_ValidateExistStation() {
        // given
        Long lineId = 1L;
        LineStationRequest request = new LineStationRequest(1L, 2L, 5);
        given(lineDao.findById(1L)).willReturn(Optional.of(new LineEntity(lineId, "2호선", "bg-green-500")));
        given(stationDao.findById(request.getUpStationId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> lineService.saveStationInLine(lineId, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 상행역이 존재하지 않습니다.");
    }

    @DisplayName("Line에 구간을 정상적으로 저장한다.")
    @Test
    void saveStationInLine() {
        // given
        Long lineId = 1L;
        LineStationRequest lineStationRequest = new LineStationRequest(1L, 2L, 5);

        LineEntity lineEntity = new LineEntity(1L, "1호선", "bg-blue-500");
        given(lineDao.findById(1L)).willReturn(Optional.of(lineEntity));

        StationEntity upStationEntity = new StationEntity(1L, "서울역");
        StationEntity downStationEntity = new StationEntity(2L, "천안역");
        given(stationDao.findById(1L)).willReturn(Optional.of(upStationEntity));
        given(stationDao.findById(2L)).willReturn(Optional.of(downStationEntity));

        given(lineRepository.findSectionsByLineIdWithSort(anyLong())).willReturn(new ArrayList<>());

        given(stationDao.findById(1L)).willReturn(Optional.of(upStationEntity));
        given(stationDao.findById(2L)).willReturn(Optional.of(downStationEntity));

        // when
        lineService.saveStationInLine(lineId, lineStationRequest);

        // then
        verify(sectionDao).deleteByLineId(lineId);

        ArgumentCaptor<List<SectionEntity>> sectionEntityListCaptor = ArgumentCaptor.forClass(List.class);
        verify(sectionDao).insertAll(sectionEntityListCaptor.capture());

        List<SectionEntity> capturedSectionEntityList = sectionEntityListCaptor.getValue();
        assertEquals(1, capturedSectionEntityList.size());

        SectionEntity firstSectionEntity = capturedSectionEntityList.get(0);
        assertEquals(1L, firstSectionEntity.getLineId());
        assertEquals(1L, firstSectionEntity.getUpStationId());
        assertEquals(2L, firstSectionEntity.getDownStationId());
        assertEquals(5, firstSectionEntity.getDistance());
        assertEquals(1, firstSectionEntity.getOrder());
    }

    @DisplayName("모든 노선의 정보와 각 노선에 있는 구간을 조회하여 순서대로 반환한다.")
    @Test
    void findAll() {
        LineEntity lineEntity1 = new LineEntity(1L, "1호선", "bg-blue-500");
        LineEntity lineEntity2 = new LineEntity(2L, "2호선", "bg-green-500");
        given(lineDao.findAll()).willReturn(List.of(lineEntity1, lineEntity2));

        Station 서울역 = new Station(1L, "서울역");
        Station 수원역 = new Station(2L, "수원역");
        Station 천안역 = new Station(3L, "천안역");

        Station 강남역 = new Station(4L, "강남역");
        Station 선릉역 = new Station(5L, "선릉역");
        Station 잠실역 = new Station(6L, "잠실역");

        Section 서울역_수원역 = new Section(1L, 서울역, 수원역, 3, 1);
        Section 수원역_천안역 = new Section(2L, 수원역, 천안역, 4, 2);

        Section 강남역_선릉역 = new Section(3L, 강남역, 선릉역, 3, 1);
        Section 선릉역_잠실역 = new Section(4L, 선릉역, 잠실역, 4, 2);

        Line 호선1 = new Line(lineEntity1.getId(), lineEntity1.getName(), lineEntity1.getColor(), new Sections(List.of(서울역_수원역, 수원역_천안역)));
        Line 호선2 = new Line(lineEntity2.getId(), lineEntity2.getName(), lineEntity2.getColor(), new Sections(List.of(강남역_선릉역, 선릉역_잠실역)));
        given(lineRepository.findLinesWithSort()).willReturn(List.of(호선1, 호선2));

        // when
        List<LineStationResponse> lineStationResponses = lineService.findAll();

        // then
        assertThat(lineStationResponses.size()).isEqualTo(2);
        LineStationResponse 호선1_역정보 = lineStationResponses.get(0);
        LineStationResponse 호선2_역정보 = lineStationResponses.get(1);

        assertThat(호선1_역정보.getLineResponse()).usingRecursiveComparison().isEqualTo(호선1);
        assertThat(호선1_역정보.getStationResponses()).extracting(StationResponse::getId).containsExactly(1L, 2L, 3L);
        assertThat(호선1_역정보.getStationResponses()).extracting(StationResponse::getName).containsExactly("서울역", "수원역", "천안역");

        assertThat(호선2_역정보.getLineResponse()).usingRecursiveComparison().isEqualTo(호선2);
        assertThat(호선2_역정보.getStationResponses()).extracting(StationResponse::getId).containsExactly(4L, 5L, 6L);
        assertThat(호선2_역정보.getStationResponses()).extracting(StationResponse::getName).containsExactly("강남역", "선릉역", "잠실역");
    }

    @DisplayName("특정 노선의 정보와 노선에 있는 구간을 조회하여 순서대로 반환한다.")
    @Test
    void findById() {
        // given
        LineEntity lineEntity1 = new LineEntity(1L, "1호선", "bg-blue-500");
        given(lineDao.findById(anyLong())).willReturn(Optional.of(lineEntity1));

        Station 서울역 = new Station(1L, "서울역");
        Station 수원역 = new Station(2L, "수원역");
        Station 천안역 = new Station(3L, "천안역");

        Section 서울역_수원역 = new Section(1L, 서울역, 수원역, 3, 1);
        Section 수원역_천안역 = new Section(2L, 수원역, 천안역, 4, 2);

        Line 호선1 = new Line(lineEntity1.getId(), lineEntity1.getName(), lineEntity1.getColor(), new Sections(List.of(서울역_수원역, 수원역_천안역)));
        given(lineRepository.findSectionsByLineIdWithSort(anyLong())).willReturn(List.of(서울역_수원역, 수원역_천안역));

        // when
        LineStationResponse 호선1_역정보 = lineService.findById(1L);

        // then
        assertThat(호선1_역정보.getLineResponse()).usingRecursiveComparison().isEqualTo(호선1);
        assertThat(호선1_역정보.getStationResponses()).extracting(StationResponse::getId).containsExactly(1L, 2L, 3L);
        assertThat(호선1_역정보.getStationResponses()).extracting(StationResponse::getName).containsExactly("서울역", "수원역", "천안역");
    }

    @DisplayName("노선 정보를 업데이트 한다.")
    @Test
    void update() {
        // given
        willDoNothing().given(lineDao).update(any(LineEntity.class));

        // when, then
        LineRequest lineUpdateRequest = new LineRequest("2호선", "bg-green-500");
        assertDoesNotThrow(() -> lineService.update(1L, lineUpdateRequest));
    }

    @DisplayName("노선 정보를 삭제한다.")
    @Test
    void deleteById() {
        // given
        willDoNothing().given(lineDao).deleteById(anyLong());

        // when, then
        assertDoesNotThrow(() -> lineService.deleteById(1L));
    }

    @DisplayName("노선에 있는 구간 역을 삭제한다.")
    @Test
    void deleteByLineIdAndStationId() {
        // given
        Station 서울역 = new Station(1L, "서울역");
        Station 수원역 = new Station(2L, "수원역");

        given(stationDao.findById(1L)).willReturn(Optional.of(new StationEntity(1L, "서울역")));
        given(lineDao.findById(1L)).willReturn(Optional.of(new LineEntity(1L, "1호선", "bg-blue-500")));

        Section 서울역_수원역 = new Section(1L, 서울역, 수원역, 3, 1);
        given(lineRepository.findSectionsByLineIdWithSort(anyLong())).willReturn(new ArrayList<>(List.of(서울역_수원역)));
        // when
        lineService.deleteByLineIdAndStationId(1L, 1L);

        // then
        verify(sectionDao).deleteByLineId(1L);
        verify(sectionDao).insertAll(Collections.emptyList());
    }

    @DisplayName("삭제할 역이 노선에 존재하지 않는 경우 NoSuchElementException을 반환한다.")
    @Test
    void deleteByLineIdAndStationId_fail() {
        // given
        given(lineDao.findById(1L))
                .willReturn(Optional.of(new LineEntity(1L, "1호선", "bg-blue-500")));
        given(stationDao.findById(1L)).willReturn(Optional.of(new StationEntity(1L, "서울역")));

        // when & then
        assertThatThrownBy(() -> lineService.deleteByLineIdAndStationId(1L, 1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("삭제할 구간이 존재하지 않습니다.");
    }
}
