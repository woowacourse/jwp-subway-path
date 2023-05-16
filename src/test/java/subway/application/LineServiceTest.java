package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.*;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationDao stationDao;

    @Mock
    private LineDao lineDao;

    private LineService lineService;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineDao, sectionDao, stationDao);
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
        RegisterStationRequest request = new RegisterStationRequest(1L, 1L, 5);

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
        RegisterStationRequest request = new RegisterStationRequest(1L, 2L, 5);
        given(lineDao.findById(1L)).willReturn(Optional.of(new LineEntity(lineId, "2호선", "bg-green-500")));
        given(stationDao.findById(request.getUpStationId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> lineService.saveStationInLine(lineId, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 상행역이 존재하지 않습니다.");
    }

    @DisplayName("Line에 등록할 구간의 역이 이미 등록된 구간인 경우 IllegalArgumentException 예외가 발생한다.")
    @Test
    void saveStationInLine_ValidateAlreadyExistSection() {
        // given
        Long lineId = 1L;
        RegisterStationRequest request = new RegisterStationRequest(1L, 2L, 5);
        given(lineDao.findById(1L)).willReturn(Optional.of(new LineEntity(lineId, "2호선", "bg-green-500")));
        given(stationDao.findById(request.getUpStationId())).willReturn(Optional.of(new StationEntity(1L, "강남역")));
        given(stationDao.findById(request.getDownStationId())).willReturn(Optional.of(new StationEntity(2L, "잠실역")));
        given(sectionDao.findByStationIds(request.getUpStationId(), request.getDownStationId())).willReturn(Optional.of(1L));

        // when & then
        assertThatThrownBy(() -> lineService.saveStationInLine(lineId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 구간입니다.");
    }

    @DisplayName("Line에 구간을 정상적으로 저장한다.")
    @Test
    void saveStationInLine() {
        // given
        Long lineId = 1L;
        RegisterStationRequest registerStationRequest = new RegisterStationRequest(1L, 2L, 5);

        LineEntity lineEntity = new LineEntity(1L, "1호선", "bg-blue-500");
        given(lineDao.findById(1L)).willReturn(Optional.of(lineEntity));

        StationEntity upStationEntity = new StationEntity(1L, "서울역");
        StationEntity downStationEntity = new StationEntity(2L, "천안역");
        given(stationDao.findById(1L)).willReturn(Optional.of(upStationEntity));
        given(stationDao.findById(2L)).willReturn(Optional.of(downStationEntity));

        given(sectionDao.findByStationIds(anyLong(), anyLong())).willReturn(Optional.empty());
        given(sectionDao.findByLineId(anyLong())).willReturn(Collections.emptyList());
        given(stationDao.findAll()).willReturn(List.of(upStationEntity, downStationEntity));

        SectionEntity null_서울역 = new SectionEntity(null, 1L, null, 1L, 0);
        SectionEntity 서울역_천안역 = new SectionEntity(null, 1L, 1L, 2L, 5);
        SectionEntity 천안역_null = new SectionEntity(null, 1L, 2L, null, 0);

        // when
        lineService.saveStationInLine(lineId, registerStationRequest);

        // then
        verify(sectionDao).deleteByLineId(lineId);
        verify(sectionDao).insertAll(ArgumentMatchers.eq(Arrays.asList(null_서울역, 서울역_천안역, 천안역_null)));
    }

    @DisplayName("모든 Line의 정보를 반환한다.")
    @Test
    void findLineResponses() {
        // given
        LineEntity 호선1 = new LineEntity(1L, "1호선", "bg-blue-500");
        LineEntity 호선2 = new LineEntity(2L, "2호선", "bg-green-500");
        given(lineDao.findAll()).willReturn(new ArrayList<>(List.of(호선1, 호선2)));

        // when
        List<LineResponse> lineResponses = lineService.findLineResponses();

        // then
        assertThat(lineResponses.size()).isEqualTo(2);
        assertThat(lineResponses).extracting(LineResponse::getId).containsExactly(1L, 2L);
        assertThat(lineResponses).extracting(LineResponse::getName).containsExactly("1호선", "2호선");
        assertThat(lineResponses).extracting(LineResponse::getColor).containsExactly("bg-blue-500", "bg-green-500");
    }

    @DisplayName("특정 Line의 정보를 반환한다.")
    @Test
    void findLineResponseById() {
        LineEntity 호선1 = new LineEntity(1L, "1호선", "bg-blue-500");
        given(lineDao.findById(1L)).willReturn(Optional.of(호선1));

        // when
        LineResponse lineResponse = lineService.findLineResponseById(1L);

        // then
        assertThat(lineResponse).usingRecursiveComparison().isEqualTo(호선1);
    }

    @DisplayName("모든 노선의 정보와 각 노선에 있는 구간을 조회하여 순서대로 반환한다.")
    @Test
    void findAll() {
        LineEntity 호선1 = new LineEntity(1L, "1호선", "bg-blue-500");
        LineEntity 호선2 = new LineEntity(2L, "2호선", "bg-green-500");
        given(lineDao.findAll()).willReturn(List.of(호선1, 호선2));

        StationEntity 서울역 = new StationEntity(1L, "서울역");
        StationEntity 수원역 = new StationEntity(2L, "수원역");
        StationEntity 천안역 = new StationEntity(3L, "천안역");

        StationEntity 강남역 = new StationEntity(4L, "강남역");
        StationEntity 선릉역 = new StationEntity(5L, "선릉역");
        StationEntity 잠실역 = new StationEntity(6L, "잠실역");
        given(stationDao.findAll()).willReturn(List.of(서울역, 수원역, 천안역, 강남역, 선릉역, 잠실역));

        SectionEntity null_서울역 = new SectionEntity(1L, 1L, null, 1L, 0);
        SectionEntity 서울역_수원역 = new SectionEntity(2L, 1L, 1L, 2L, 3);
        SectionEntity 수원역_천안역 = new SectionEntity(3L, 1L, 2L, 3L, 4);
        SectionEntity 천안역_null = new SectionEntity(4L, 1L, 3L, null, 0);

        SectionEntity null_강남역 = new SectionEntity(5L, 2L, null, 4L, 0);
        SectionEntity 강남역_선릉역 = new SectionEntity(6L, 2L, 4L, 5L, 3);
        SectionEntity 선릉역_잠실역 = new SectionEntity(7L, 2L, 5L, 6L, 4);
        SectionEntity 잠실역_null = new SectionEntity(8L, 2L, 6L, null, 0);

        given(sectionDao.findAll()).willReturn(List.of(
                null_서울역, 서울역_수원역, 수원역_천안역, 천안역_null,
                null_강남역, 강남역_선릉역, 선릉역_잠실역, 잠실역_null)
        );

        // when
        List<LineStationResponse> lineStationResponses = lineService.findAll();
        LineStationResponse 호선1_역정보 = lineStationResponses.get(0);
        LineStationResponse 호선2_역정보 = lineStationResponses.get(1);

        // then
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
        LineEntity 호선1 = new LineEntity(1L, "1호선", "bg-blue-500");
        given(lineDao.findById(anyLong())).willReturn(Optional.of(호선1));

        StationEntity 서울역 = new StationEntity(1L, "서울역");
        StationEntity 수원역 = new StationEntity(2L, "수원역");
        StationEntity 천안역 = new StationEntity(3L, "천안역");
        given(stationDao.findAll()).willReturn(List.of(서울역, 수원역, 천안역));

        SectionEntity null_서울역 = new SectionEntity(1L, 1L, null, 1L, 0);
        SectionEntity 서울역_수원역 = new SectionEntity(1L, 1L, 1L, 2L, 3);
        SectionEntity 수원역_천안역 = new SectionEntity(1L, 1L, 2L, 3L, 4);
        SectionEntity 천안역_null = new SectionEntity(1L, 1L, 3L, null, 0);
        given(sectionDao.findByLineId(1L)).willReturn(List.of(null_서울역, 서울역_수원역, 수원역_천안역, 천안역_null));

        // when
        LineStationResponse lineStationResponse = lineService.findById(1L);

        // then
        assertThat(lineStationResponse.getLineResponse()).usingRecursiveComparison().isEqualTo(호선1);
        assertThat(lineStationResponse.getStationResponses()).extracting(StationResponse::getId).containsExactly(1L, 2L, 3L);
        assertThat(lineStationResponse.getStationResponses()).extracting(StationResponse::getName).containsExactly("서울역", "수원역", "천안역");
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
        List<SectionEntity> sectionEntities = Arrays.asList(
                new SectionEntity(1L, 1L, null, 1L, 0),
                new SectionEntity(1L, 1L, 1L, 2L, 5),
                new SectionEntity(1L, 1L, 2L, null, 0)
        );

        List<StationEntity> stationEntities = Arrays.asList(
                new StationEntity(1L, "서울역"),
                new StationEntity(2L, "천안역")
        );

        given(sectionDao.findByLineId(1L)).willReturn(sectionEntities);
        given(stationDao.findAll()).willReturn(stationEntities);

        given(lineDao.findById(1L)).willReturn(Optional.of(new LineEntity(1L, "1호선", "bg-blue-500")));
        given(sectionDao.findByLineIdAndStationId(anyLong(), anyLong())).willReturn(sectionEntities);

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
        given(sectionDao.findByLineIdAndStationId(anyLong(), anyLong()))
                .willReturn(Collections.emptyList());

        given(lineDao.findById(1L))
                .willReturn(Optional.of(new LineEntity(1L, "1호선", "bg-blue-500")));

        // when & then
        assertThatThrownBy(() -> lineService.deleteByLineIdAndStationId(1L, 1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("삭제할 구간이 존재하지 않습니다.");
    }
}
