package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.LineRequest;
import subway.dto.LineStationResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

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
        LineEntity line = new LineEntity(1L, lineRequest.getName(), lineRequest.getColor());
        given(lineDao.insert(any(LineEntity.class))).willReturn(line);

        // when, then
        assertThat(lineService.save(lineRequest)).isEqualTo(1L);
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

        StationEntity 선릉역 = new StationEntity(4L, "선릉역");
        StationEntity 강남역 = new StationEntity(5L, "강남역");
        StationEntity 잠실역 = new StationEntity(6L, "잠실역");
        given(stationDao.findAll()).willReturn(List.of(서울역, 수원역, 천안역, 선릉역, 강남역, 잠실역));

        SectionEntity null_서울역 = new SectionEntity(1L, 1L, null, 1L, 0);
        SectionEntity 서울역_수원역 = new SectionEntity(2L, 1L, 1L, 2L, 3);
        SectionEntity 수원역_천안역 = new SectionEntity(3L, 1L, 2L, 3L, 4);
        SectionEntity 천안역_null = new SectionEntity(4L, 1L, 3L, null, 0);

        SectionEntity null_선릉역 = new SectionEntity(5L, 2L, null, 4L, 0);
        SectionEntity 선릉역_강남역 = new SectionEntity(6L, 2L, 4L, 5L, 3);
        SectionEntity 강남역_잠실역 = new SectionEntity(7L, 2L, 5L, 6L, 4);
        SectionEntity 잠실역_null = new SectionEntity(8L, 2L, 6L, null, 0);

        given(sectionDao.findAll()).willReturn(List.of(
                null_서울역, 서울역_수원역, 수원역_천안역, 천안역_null,
                null_선릉역, 선릉역_강남역, 강남역_잠실역, 잠실역_null)
        );

        // when
        List<LineStationResponse> lineStationResponses = lineService.findAll();
        LineStationResponse 호선1_역정보 = lineStationResponses.get(0);
        LineStationResponse 호선2_역정보 = lineStationResponses.get(1);

        // then
        assertThat(호선1_역정보.getLineResponse()).usingRecursiveComparison().isEqualTo(호선1);
        assertThat(호선1_역정보.getStationResponses().get(0)).usingRecursiveComparison().isEqualTo(서울역);
        assertThat(호선1_역정보.getStationResponses().get(1)).usingRecursiveComparison().isEqualTo(수원역);
        assertThat(호선1_역정보.getStationResponses().get(2)).usingRecursiveComparison().isEqualTo(천안역);

        assertThat(호선2_역정보.getLineResponse()).usingRecursiveComparison().isEqualTo(호선2);
        assertThat(호선2_역정보.getStationResponses().get(0)).usingRecursiveComparison().isEqualTo(선릉역);
        assertThat(호선2_역정보.getStationResponses().get(1)).usingRecursiveComparison().isEqualTo(강남역);
        assertThat(호선2_역정보.getStationResponses().get(2)).usingRecursiveComparison().isEqualTo(잠실역);
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
        assertThat(lineStationResponse.getStationResponses().get(0)).usingRecursiveComparison().isEqualTo(서울역);
        assertThat(lineStationResponse.getStationResponses().get(1)).usingRecursiveComparison().isEqualTo(수원역);
        assertThat(lineStationResponse.getStationResponses().get(2)).usingRecursiveComparison().isEqualTo(천안역);
    }

    @DisplayName("노선 정보를 업데이트 한다.")
    @Test
    void update() {
        // given
        LineRequest lineRequest = new LineRequest("1호선", "bg-blue-500");
        LineEntity line = new LineEntity(1L, lineRequest.getName(), lineRequest.getColor());

        given(lineDao.insert(any(LineEntity.class))).willReturn(line);
        lineService.save(lineRequest);

        willDoNothing().given(lineDao).update(anyLong(), any(LineEntity.class));

        // when, then
        LineRequest lineUpdateRequest = new LineRequest("2호선", "bg-green-500");
        assertDoesNotThrow(() -> lineService.update(1L, lineUpdateRequest));
    }

    @DisplayName("노선 정보를 삭제한다.")
    @Test
    void deleteById() {
        // given
        LineRequest lineRequest = new LineRequest("1호선", "bg-blue-500");
        LineEntity line = new LineEntity(1L, lineRequest.getName(), lineRequest.getColor());

        given(lineDao.insert(any(LineEntity.class))).willReturn(line);
        lineService.save(lineRequest);

        willDoNothing().given(lineDao).deleteById(anyLong());

        // when, then
        assertDoesNotThrow(() -> lineService.deleteById(1L));
    }
}
