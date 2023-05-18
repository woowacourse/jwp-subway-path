package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        given(lineDao.insert(any(LineEntity.class))).willReturn(1L);
        LineRequest lineRequest = new LineRequest("2호선", "bg-green-600");

        // when, then
        assertThat(lineService.save(lineRequest)).isEqualTo(1L);
    }

    @DisplayName("모든 노선의 정보와 각 노선에 있는 구간을 조회하여 순서대로 반환한다.")
    @Test
    void findAll() {
        LineEntity line1 = LineEntity.of(1L, "1호선", "bg-red-500");
        LineEntity line2 = LineEntity.of(2L, "2호선", "bg-green-600");
        given(lineDao.findById(1L)).willReturn(Optional.of(line1));
        given(lineDao.findById(2L)).willReturn(Optional.of(line2));
        given(lineDao.findAll()).willReturn(List.of(line1, line2));

        StationEntity station1 = StationEntity.of(1L, "용산역");
        StationEntity station2 = StationEntity.of(2L, "죽전역");
        given(stationDao.findById(1L)).willReturn(Optional.of(station1));
        given(stationDao.findById(2L)).willReturn(Optional.of(station2));

        SectionEntity section = new SectionEntity(2L, 1L, 2L, 3);
        given(sectionDao.findByLineId(line1.getId())).willReturn(Collections.emptyList());
        given(sectionDao.findByLineId(line2.getId())).willReturn(List.of(section));

        // when
        List<LineStationResponse> lineStationResponses = lineService.findAll();
        LineStationResponse lineStationResponse1 = lineStationResponses.get(0);
        LineStationResponse lineStationResponse2 = lineStationResponses.get(1);

        // then
        assertThat(lineStationResponse1.getLineResponse()).usingRecursiveComparison().isEqualTo(line1);
        assertThat(lineStationResponse1.getStationResponses().size()).isEqualTo(0);
        assertThat(lineStationResponse2.getLineResponse()).usingRecursiveComparison().isEqualTo(line2);
        assertThat(lineStationResponse2.getStationResponses().get(0)).usingRecursiveComparison().isEqualTo(station1);
        assertThat(lineStationResponse2.getStationResponses().get(1)).usingRecursiveComparison().isEqualTo(station2);
    }

    @DisplayName("특정 노선의 정보와 노선에 있는 구간을 조회하여 순서대로 반환한다.")
    @Test
    void findById() {
        // given
        LineEntity line = LineEntity.of(1L, "1호선", "bg-red-500");
        given(lineDao.findById(anyLong())).willReturn(Optional.of(line));

        StationEntity station1 = StationEntity.of(1L, "반월당역");
        StationEntity station2 = StationEntity.of(2L, "신천역");
        StationEntity station3 = StationEntity.of(3L, "동대구역");
        given(stationDao.findById(1L)).willReturn(Optional.of(station1));
        given(stationDao.findById(2L)).willReturn(Optional.of(station2));
        given(stationDao.findById(3L)).willReturn(Optional.of(station3));

        SectionEntity section1 = new SectionEntity(1L, 1L, 2L, 3);
        SectionEntity section2 = new SectionEntity(1L, 2L, 3L, 4);
        given(sectionDao.findByLineId(1L)).willReturn(List.of(section1, section2));

        // when
        LineStationResponse lineStationResponse = lineService.findById(1L);

        // then
        assertThat(lineStationResponse.getLineResponse()).usingRecursiveComparison().isEqualTo(line);
        assertThat(lineStationResponse.getStationResponses().get(0)).usingRecursiveComparison().isEqualTo(station1);
        assertThat(lineStationResponse.getStationResponses().get(1)).usingRecursiveComparison().isEqualTo(station2);
        assertThat(lineStationResponse.getStationResponses().get(2)).usingRecursiveComparison().isEqualTo(station3);
    }

    @DisplayName("노선 정보를 업데이트 한다.")
    @Test
    void update() {
        // given
        given(lineDao.insert(any(LineEntity.class))).willReturn(1L);
        LineRequest lineRequest = new LineRequest("1호선", "bg-red-500");
        lineService.save(lineRequest);
        willDoNothing().given(lineDao).update(anyLong(), any(LineEntity.class));

        // when, then
        LineRequest lineUpdateRequest = new LineRequest("2호선", "bg-green-600");
        assertDoesNotThrow(() -> lineService.update(1L, lineUpdateRequest));
    }

    @DisplayName("노선 정보를 삭제한다.")
    @Test
    void deleteById() {
        // given
        given(lineDao.insert(any(LineEntity.class))).willReturn(1L);
        LineRequest lineRequest = new LineRequest("1호선", "bg-blue-500");
        lineService.save(lineRequest);
        willDoNothing().given(lineDao).deleteById(anyLong());

        // when, then
        assertDoesNotThrow(() -> lineService.deleteById(1L));
    }
}
