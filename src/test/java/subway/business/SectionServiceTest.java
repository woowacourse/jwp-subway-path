package subway.business;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.business.dto.SectionInsertDto;
import subway.exception.not_found.LineNotFoundException;
import subway.persistence.SectionDao;
import subway.persistence.StationDao;
import subway.persistence.entity.SectionDetailEntity;
import subway.persistence.entity.SectionEntity;
import subway.presentation.dto.response.SectionResponse;
import subway.presentation.dto.request.converter.SubwayDirection;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("구간 Service")
class SectionServiceTest {

    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;
    @InjectMocks
    private SectionService sectionService;

    @Nested
    @DisplayName("노선에 역 추가")
    class Save {

        @Test
        @DisplayName("성공 - 상행 끝에 추가")
        void save_success_top() {
            // given
            final String lineName = "2호선";
            final String standardStationName = "종합운동장";
            final String newStationName = "송파";
            final int distance = 3;

            final List<SectionEntity> sectionEntities = List.of(
                    new SectionEntity(1L, 1L, 3, 1L, 2L),
                    new SectionEntity(2L, 1L, 4, 2L, 3L)
            );
            final SectionEntity insertedSectionEntity = new SectionEntity(5L, 1L, distance, 3L, 5L);
            final SectionDetailEntity insertedSectionDetailEntity = new SectionDetailEntity(
                    insertedSectionEntity.getId(), distance, insertedSectionEntity.getLineId(), "2호선", "bg-green-600",
                    insertedSectionEntity.getPreviousStationId(), standardStationName,
                    insertedSectionEntity.getNextStationId(), newStationName
            );

            given(sectionDao.findByLineName(lineName)).willReturn(sectionEntities);
            given(stationDao.findIdByName(standardStationName)).willReturn(3L);
            given(stationDao.findIdByName(newStationName)).willReturn(5L);
            given(sectionDao.insert(any())).willReturn(insertedSectionEntity);
            given(sectionDao.findSectionDetailById(insertedSectionEntity.getId())).willReturn(insertedSectionDetailEntity);

            // when
            final SectionInsertDto sectionInsertDto = new SectionInsertDto(
                    lineName, SubwayDirection.UP, standardStationName, newStationName, distance
            );
            final List<SectionResponse> responses = sectionService.save(sectionInsertDto);

            // then
            assertAll(
                    () -> assertThat(responses).hasSize(1),
                    () -> assertThat(responses.get(0).getId()).isEqualTo(insertedSectionDetailEntity.getId()),
                    () -> assertThat(responses.get(0).getDistance()).isEqualTo(insertedSectionDetailEntity.getDistance()),
                    () -> assertThat(responses.get(0).getLine().getId()).isEqualTo(insertedSectionDetailEntity.getLineId()),
                    () -> assertThat(responses.get(0).getLine().getName()).isEqualTo(insertedSectionDetailEntity.getLineName()),
                    () -> assertThat(responses.get(0).getLine().getColor()).isEqualTo(insertedSectionDetailEntity.getLineColor()),
                    () -> assertThat(responses.get(0).getPreviousStation().getId()).isEqualTo(insertedSectionDetailEntity.getPreviousStationId()),
                    () -> assertThat(responses.get(0).getPreviousStation().getName()).isEqualTo(insertedSectionDetailEntity.getPreviousStationName()),
                    () -> assertThat(responses.get(0).getNextStation().getId()).isEqualTo(insertedSectionDetailEntity.getNextStationId()),
                    () -> assertThat(responses.get(0).getNextStation().getName()).isEqualTo(insertedSectionDetailEntity.getNextStationName())
            );
        }

        @Test
        @DisplayName("성공 - 하행 끝에 추가")
        void save_success_bottom() {
            // given
            final String lineName = "2호선";
            final String standardStationName = "잠실";
            final String newStationName = "송파";
            final int distance = 3;

            final List<SectionEntity> sectionEntities = List.of(
                    new SectionEntity(1L, 1L, 3, 1L, 2L),
                    new SectionEntity(2L, 1L, 4, 2L, 3L)
            );
            final SectionEntity insertedSectionEntity = new SectionEntity(5L, 1L, distance, 5L, 1L);
            final SectionDetailEntity insertedSectionDetailEntity = new SectionDetailEntity(
                    insertedSectionEntity.getId(), distance, insertedSectionEntity.getLineId(), "2호선", "bg-green-600",
                    insertedSectionEntity.getPreviousStationId(), newStationName,
                    insertedSectionEntity.getNextStationId(), standardStationName
            );

            given(sectionDao.findByLineName(lineName)).willReturn(sectionEntities);
            given(stationDao.findIdByName(standardStationName)).willReturn(1L);
            given(stationDao.findIdByName(newStationName)).willReturn(5L);
            given(sectionDao.insert(any())).willReturn(insertedSectionEntity);
            given(sectionDao.findSectionDetailById(insertedSectionEntity.getId())).willReturn(insertedSectionDetailEntity);

            // when
            final SectionInsertDto sectionInsertDto = new SectionInsertDto(
                    lineName, SubwayDirection.DOWN, standardStationName, newStationName, distance
            );
            final List<SectionResponse> responses = sectionService.save(sectionInsertDto);

            // then
            assertAll(
                    () -> assertThat(responses).hasSize(1),
                    () -> assertThat(responses.get(0).getId()).isEqualTo(insertedSectionDetailEntity.getId()),
                    () -> assertThat(responses.get(0).getDistance()).isEqualTo(insertedSectionDetailEntity.getDistance()),
                    () -> assertThat(responses.get(0).getLine().getId()).isEqualTo(insertedSectionDetailEntity.getLineId()),
                    () -> assertThat(responses.get(0).getLine().getName()).isEqualTo(insertedSectionDetailEntity.getLineName()),
                    () -> assertThat(responses.get(0).getLine().getColor()).isEqualTo(insertedSectionDetailEntity.getLineColor()),
                    () -> assertThat(responses.get(0).getPreviousStation().getId()).isEqualTo(insertedSectionDetailEntity.getPreviousStationId()),
                    () -> assertThat(responses.get(0).getPreviousStation().getName()).isEqualTo(insertedSectionDetailEntity.getPreviousStationName()),
                    () -> assertThat(responses.get(0).getNextStation().getId()).isEqualTo(insertedSectionDetailEntity.getNextStationId()),
                    () -> assertThat(responses.get(0).getNextStation().getName()).isEqualTo(insertedSectionDetailEntity.getNextStationName())
            );
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 노선 입력")
        void save_fail_station_not_found_in_line() {
            // given
            final String lineName = "22호선";
            final String standardStationName = "석촌";
            final String originalPreviousStationName = "잠실";
            final String newStationName = "송파";
            final int distance = 2;
            final int frontDistance = 1;

            final SectionEntity frontSectionEntity = new SectionEntity(1L, frontDistance, 1L, 5L);
            final SectionEntity backSectionEntity = new SectionEntity(1L, distance, 5L, 2L);
            final SectionEntity insertedFrontSectionEntity = new SectionEntity(5L, 1L, frontDistance, 1L, 5L);
            final SectionEntity insertedBackSectionEntity = new SectionEntity(6L, 1L, distance, 5L, 2L);
            final SectionDetailEntity insertedFrontSectionDetailEntity = new SectionDetailEntity(
                    insertedFrontSectionEntity.getId(), frontDistance, insertedFrontSectionEntity.getLineId(), "2호선", "bg-green-600",
                    insertedFrontSectionEntity.getPreviousStationId(), originalPreviousStationName,
                    insertedFrontSectionEntity.getNextStationId(), newStationName
            );
            final SectionDetailEntity insertedBackSectionDetailEntity = new SectionDetailEntity(
                    insertedBackSectionEntity.getId(), distance, insertedBackSectionEntity.getLineId(), "2호선", "bg-green-600",
                    insertedBackSectionEntity.getPreviousStationId(), newStationName,
                    insertedBackSectionEntity.getNextStationId(), standardStationName
            );

            given(sectionDao.findByLineName(lineName)).willThrow(LineNotFoundException.class);

            // when, then
            final SectionInsertDto sectionInsertDto = new SectionInsertDto(
                    lineName, SubwayDirection.DOWN, standardStationName, newStationName, distance
            );
            assertThatThrownBy(() -> sectionService.save(sectionInsertDto))
                    .isInstanceOf(LineNotFoundException.class);
        }

    }

}
