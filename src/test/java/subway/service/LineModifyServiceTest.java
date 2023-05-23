package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.request.StationRegisterInLineRequest;
import subway.controller.dto.request.StationUnregisterInLineRequest;
import subway.controller.dto.request.SubwayDirection;
import subway.controller.dto.response.LineResponse;
import subway.entity.SectionDetailEntity;
import subway.entity.SectionEntity;
import subway.repository.LineDao;
import subway.repository.SectionDao;
import subway.repository.StationDao;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class LineModifyServiceTest {

    @Mock
    private LineDao lineDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;

    @InjectMocks
    private LineModifyService lineModifyService;

    @Test
    @DisplayName("노선에 역 등록 성공 - 상행 끝에 추가")
    void registerStation_success_upper_end() {
        // given
        final long lineId = 1L;
        final StationRegisterInLineRequest request = new StationRegisterInLineRequest(SubwayDirection.UP, 1L, 4L, 1);
        final List<SectionDetailEntity> sectionDetailEntities = List.of(
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 4L, "석촌", 1L, "잠실"),
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 1L, "잠실", 2L, "잠실새내"),
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 2L, "잠실새내", 3L, "종합운동장")
        );

        given(sectionDao.findByLineIdAndPreviousStationId(lineId, 1L)).willReturn(Optional.empty());
        given(sectionDao.insert(any())).willReturn(any());
        given(sectionDao.findSectionDetailByLineId(lineId)).willReturn(sectionDetailEntities);

        // when
        final LineResponse response = lineModifyService.registerStation(lineId, request);

        // then
        assertAll(
                () -> assertThat(response.getStations()).hasSize(4),
                () -> assertThat(response.getId()).isEqualTo(1L),
                () -> assertThat(response.getName()).isEqualTo("2호선"),
                () -> assertThat(response.getStations().get(0).getName()).isEqualTo("석촌"),
                () -> assertThat(response.getStations().get(1).getName()).isEqualTo("잠실"),
                () -> assertThat(response.getStations().get(2).getName()).isEqualTo("잠실새내"),
                () -> assertThat(response.getStations().get(3).getName()).isEqualTo("종합운동장")
        );
    }

    @Test
    @DisplayName("노선에 역 등록 성공 - 하행 끝에 추가")
    void registerStation_success_down_end() {
        // given
        final long lineId = 1L;
        final StationRegisterInLineRequest request = new StationRegisterInLineRequest(SubwayDirection.DOWN, 3L, 4L, 1);
        final List<SectionDetailEntity> sectionDetailEntities = List.of(
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 1L, "잠실", 2L, "잠실새내"),
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 2L, "잠실새내", 3L, "종합운동장"),
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 3L, "종합운동장", 4L, "석촌")
        );

        given(sectionDao.findByLineIdAndNextStationId(lineId, 3L)).willReturn(Optional.empty());
        given(sectionDao.insert(any())).willReturn(any());
        given(sectionDao.findSectionDetailByLineId(lineId)).willReturn(sectionDetailEntities);

        // when
        final LineResponse response = lineModifyService.registerStation(lineId, request);

        // then
        assertAll(
                () -> assertThat(response.getStations()).hasSize(4),
                () -> assertThat(response.getId()).isEqualTo(1L),
                () -> assertThat(response.getName()).isEqualTo("2호선"),
                () -> assertThat(response.getStations().get(0).getName()).isEqualTo("잠실"),
                () -> assertThat(response.getStations().get(1).getName()).isEqualTo("잠실새내"),
                () -> assertThat(response.getStations().get(2).getName()).isEqualTo("종합운동장"),
                () -> assertThat(response.getStations().get(3).getName()).isEqualTo("석촌")
        );
    }

    @Test
    @DisplayName("노선에 역 등록 성공 - 상행 중간에 추가")
    void registerStation_success_upper_mid() {
        // given
        final long lineId = 1L;
        final StationRegisterInLineRequest request = new StationRegisterInLineRequest(SubwayDirection.UP, 1L, 4L, 1);
        final List<SectionDetailEntity> sectionDetailEntities = List.of(
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 1L, "잠실", 4L, "석촌"),
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 4L, "석촌", 2L, "잠실새내"),
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 2L, "잠실새내", 3L, "종합운동장")
        );

        given(sectionDao.findByLineIdAndPreviousStationId(lineId, 1L)).willReturn(Optional.empty());
        lenient().doNothing().when(sectionDao).delete(any());
        given(sectionDao.insert(any())).willReturn(any());
        given(sectionDao.findSectionDetailByLineId(lineId)).willReturn(sectionDetailEntities);

        // when
        final LineResponse response = lineModifyService.registerStation(lineId, request);

        // then
        assertAll(
                () -> assertThat(response.getStations()).hasSize(4),
                () -> assertThat(response.getId()).isEqualTo(1L),
                () -> assertThat(response.getName()).isEqualTo("2호선"),
                () -> assertThat(response.getStations().get(0).getName()).isEqualTo("잠실"),
                () -> assertThat(response.getStations().get(1).getName()).isEqualTo("석촌"),
                () -> assertThat(response.getStations().get(2).getName()).isEqualTo("잠실새내"),
                () -> assertThat(response.getStations().get(3).getName()).isEqualTo("종합운동장")
        );
    }

    @Test
    @DisplayName("노선에 역 등록 성공 - 하행 중간에 추가")
    void registerStation_success_down_mid() {
        // given
        final long lineId = 1L;
        final StationRegisterInLineRequest request = new StationRegisterInLineRequest(SubwayDirection.DOWN, 1L, 4L, 1);
        final List<SectionDetailEntity> sectionDetailEntities = List.of(
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 1L, "잠실", 4L, "석촌"),
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 4L, "석촌", 2L, "잠실새내"),
                new SectionDetailEntity(1L, 1, lineId, "2호선", "bg-green-600", 2L, "잠실새내", 3L, "종합운동장")
        );

        given(sectionDao.findByLineIdAndNextStationId(lineId, 1L)).willReturn(Optional.empty());
        lenient().doNothing().when(sectionDao).delete(any());
        given(sectionDao.insert(any())).willReturn(any());
        given(sectionDao.findSectionDetailByLineId(lineId)).willReturn(sectionDetailEntities);

        // when
        final LineResponse response = lineModifyService.registerStation(lineId, request);

        // then
        assertAll(
                () -> assertThat(response.getStations()).hasSize(4),
                () -> assertThat(response.getId()).isEqualTo(1L),
                () -> assertThat(response.getName()).isEqualTo("2호선"),
                () -> assertThat(response.getStations().get(0).getName()).isEqualTo("잠실"),
                () -> assertThat(response.getStations().get(1).getName()).isEqualTo("석촌"),
                () -> assertThat(response.getStations().get(2).getName()).isEqualTo("잠실새내"),
                () -> assertThat(response.getStations().get(3).getName()).isEqualTo("종합운동장")
        );
    }

    @Test
    @DisplayName("노선에 역 삭제 성공")
    void unregisterStation_success() {
        // given
        final long lineId = 1L;
        final String stationName = "잠실새내";
        final StationUnregisterInLineRequest request = new StationUnregisterInLineRequest("잠실새내");
        final SectionEntity frontSection = new SectionEntity(1L, lineId, 3, 1L, 2L);
        final SectionEntity backSection = new SectionEntity(2L, lineId, 3, 2L, 3L);
        final List<SectionEntity> entities = List.of(frontSection, backSection);
        final List<SectionDetailEntity> sectionDetailEntities = List.of(
                new SectionDetailEntity(1L, 6, lineId, "2호선", "bg-green-600", 1L, "잠실", 3L, "종합운동장")
        );

        given(sectionDao.findByLineIdAndPreviousStationNameOrNextStationName(1L, stationName)).willReturn(entities);
        lenient().doNothing().when(sectionDao).delete(frontSection);
        lenient().doNothing().when(sectionDao).delete(backSection);
        given(sectionDao.insert(any())).willReturn(any());
        given(sectionDao.findSectionDetailByLineId(lineId)).willReturn(sectionDetailEntities);

        // when
        final Optional<LineResponse> response = lineModifyService.unregisterStation(lineId, request);

        // then
        assertAll(
                () -> assertThat(response.get().getStations()).hasSize(2),
                () -> assertThat(response.get().getName()).isEqualTo("2호선")
        );
    }
}
