package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.request.StationUnregisterInLineRequest;
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
    @DisplayName("노선에 역 삭제 성공")
    void deleteStation_success() {
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
