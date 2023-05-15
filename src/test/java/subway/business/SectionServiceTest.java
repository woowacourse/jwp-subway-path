package subway.business;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.business.dto.SectionInsertDto;
import subway.persistence.SectionDao;
import subway.persistence.StationDao;
import subway.persistence.entity.SectionDetailEntity;
import subway.persistence.entity.SectionEntity;
import subway.presentation.dto.response.SectionResponse;
import subway.presentation.query_option.SubwayDirection;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
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
        @DisplayName("성공 - 상행 중간 추가")
        void save_success_between_upper() {
            // given
            final String lineName = "2호선";
            final String standardStationName = "잠실";
            final String originalNextStationName = "잠실새내";
            final String newStationName = "송파";
            final int distance = 2;
            final int backDistance = 1;

            final List<SectionEntity> sectionEntities = List.of(
                    new SectionEntity(1L, 1L, 3, 1L, 2L),
                    new SectionEntity(2L, 1L, 4, 2L, 3L)
            );
            final SectionEntity frontSectionEntity = new SectionEntity(1L, distance, 1L, 5L);
            final SectionEntity backSectionEntity = new SectionEntity(1L, backDistance, 5L, 2L);
            final SectionEntity insertedFrontSectionEntity = new SectionEntity(5L, 1L, distance, 1L, 5L);
            final SectionEntity insertedBackSectionEntity = new SectionEntity(6L, 1L, backDistance, 5L, 2L);
            final SectionDetailEntity insertedFrontSectionDetailEntity = new SectionDetailEntity(
                    insertedFrontSectionEntity.getId(), distance, insertedFrontSectionEntity.getLineId(), "2호선", "bg-green-600",
                    insertedFrontSectionEntity.getPreviousStationId(), standardStationName,
                    insertedFrontSectionEntity.getNextStationId(), newStationName
            );
            final SectionDetailEntity insertedBackSectionDetailEntity = new SectionDetailEntity(
                    insertedBackSectionEntity.getId(), backDistance, insertedBackSectionEntity.getLineId(), "2호선", "bg-green-600",
                    insertedBackSectionEntity.getPreviousStationId(), newStationName,
                    insertedBackSectionEntity.getNextStationId(), originalNextStationName
            );

            given(sectionDao.findByLineName(lineName)).willReturn(sectionEntities);
            given(stationDao.findIdByName(standardStationName)).willReturn(1L);
            given(stationDao.findIdByName(newStationName)).willReturn(5L);
            given(sectionDao.insert(frontSectionEntity)).willReturn(insertedFrontSectionEntity);
            given(sectionDao.insert(backSectionEntity)).willReturn(insertedBackSectionEntity);
            given(sectionDao.findSectionDetailById(insertedFrontSectionEntity.getId())).willReturn(insertedFrontSectionDetailEntity);
            given(sectionDao.findSectionDetailById(insertedBackSectionEntity.getId())).willReturn(insertedBackSectionDetailEntity);

            // when
            final SectionInsertDto sectionInsertDto = new SectionInsertDto(
                    lineName, SubwayDirection.UP, standardStationName, newStationName, distance
            );
            final List<SectionResponse> responses = sectionService.save(sectionInsertDto);

            // then
            assertAll(
                    () -> assertThat(responses).hasSize(2),
                    () -> assertThat(responses.get(0).getId()).isEqualTo(insertedFrontSectionDetailEntity.getId()),
                    () -> assertThat(responses.get(0).getDistance()).isEqualTo(insertedFrontSectionDetailEntity.getDistance()),
                    () -> assertThat(responses.get(0).getLine().getId()).isEqualTo(insertedFrontSectionDetailEntity.getLineId()),
                    () -> assertThat(responses.get(0).getLine().getName()).isEqualTo(insertedFrontSectionDetailEntity.getLineName()),
                    () -> assertThat(responses.get(0).getLine().getColor()).isEqualTo(insertedFrontSectionDetailEntity.getLineColor()),
                    () -> assertThat(responses.get(0).getPreviousStation().getId()).isEqualTo(insertedFrontSectionDetailEntity.getPreviousStationId()),
                    () -> assertThat(responses.get(0).getPreviousStation().getName()).isEqualTo(insertedFrontSectionDetailEntity.getPreviousStationName()),
                    () -> assertThat(responses.get(0).getNextStation().getId()).isEqualTo(insertedFrontSectionDetailEntity.getNextStationId()),
                    () -> assertThat(responses.get(0).getNextStation().getName()).isEqualTo(insertedFrontSectionDetailEntity.getNextStationName()),
                    () -> assertThat(responses.get(1).getId()).isEqualTo(insertedBackSectionDetailEntity.getId()),
                    () -> assertThat(responses.get(1).getDistance()).isEqualTo(insertedBackSectionDetailEntity.getDistance()),
                    () -> assertThat(responses.get(1).getLine().getId()).isEqualTo(insertedBackSectionDetailEntity.getLineId()),
                    () -> assertThat(responses.get(1).getLine().getName()).isEqualTo(insertedBackSectionDetailEntity.getLineName()),
                    () -> assertThat(responses.get(1).getLine().getColor()).isEqualTo(insertedBackSectionDetailEntity.getLineColor()),
                    () -> assertThat(responses.get(1).getPreviousStation().getId()).isEqualTo(insertedBackSectionDetailEntity.getPreviousStationId()),
                    () -> assertThat(responses.get(1).getPreviousStation().getName()).isEqualTo(insertedBackSectionDetailEntity.getPreviousStationName()),
                    () -> assertThat(responses.get(1).getNextStation().getId()).isEqualTo(insertedBackSectionDetailEntity.getNextStationId()),
                    () -> assertThat(responses.get(1).getNextStation().getName()).isEqualTo(insertedBackSectionDetailEntity.getNextStationName())
            );
        }

        @Test
        @DisplayName("성공 - 하행 중간 추가")
        void save_success_between_down() {
            // given
            final String lineName = "2호선";
            final String standardStationName = "잠실새내";
            final String originalPreviousStationName = "잠실";
            final String newStationName = "송파";
            final int distance = 2;
            final int frontDistance = 1;

            final List<SectionEntity> sectionEntities = List.of(
                    new SectionEntity(1L, 1L, 3, 1L, 2L),
                    new SectionEntity(2L, 1L, 4, 2L, 3L)
            );
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

            given(sectionDao.findByLineName(lineName)).willReturn(sectionEntities);
            given(stationDao.findIdByName(standardStationName)).willReturn(2L);
            given(stationDao.findIdByName(newStationName)).willReturn(5L);
            given(sectionDao.insert(frontSectionEntity)).willReturn(insertedFrontSectionEntity);
            given(sectionDao.insert(backSectionEntity)).willReturn(insertedBackSectionEntity);
            given(sectionDao.findSectionDetailById(insertedFrontSectionEntity.getId())).willReturn(insertedFrontSectionDetailEntity);
            given(sectionDao.findSectionDetailById(insertedBackSectionEntity.getId())).willReturn(insertedBackSectionDetailEntity);

            // when
            final SectionInsertDto sectionInsertDto = new SectionInsertDto(
                    lineName, SubwayDirection.DOWN, standardStationName, newStationName, distance
            );
            final List<SectionResponse> responses = sectionService.save(sectionInsertDto);

            // then
            assertAll(
                    () -> assertThat(responses).hasSize(2),
                    () -> assertThat(responses.get(0).getId()).isEqualTo(insertedFrontSectionDetailEntity.getId()),
                    () -> assertThat(responses.get(0).getDistance()).isEqualTo(insertedFrontSectionDetailEntity.getDistance()),
                    () -> assertThat(responses.get(0).getLine().getId()).isEqualTo(insertedFrontSectionDetailEntity.getLineId()),
                    () -> assertThat(responses.get(0).getLine().getName()).isEqualTo(insertedFrontSectionDetailEntity.getLineName()),
                    () -> assertThat(responses.get(0).getLine().getColor()).isEqualTo(insertedFrontSectionDetailEntity.getLineColor()),
                    () -> assertThat(responses.get(0).getPreviousStation().getId()).isEqualTo(insertedFrontSectionDetailEntity.getPreviousStationId()),
                    () -> assertThat(responses.get(0).getPreviousStation().getName()).isEqualTo(insertedFrontSectionDetailEntity.getPreviousStationName()),
                    () -> assertThat(responses.get(0).getNextStation().getId()).isEqualTo(insertedFrontSectionDetailEntity.getNextStationId()),
                    () -> assertThat(responses.get(0).getNextStation().getName()).isEqualTo(insertedFrontSectionDetailEntity.getNextStationName()),
                    () -> assertThat(responses.get(1).getId()).isEqualTo(insertedBackSectionDetailEntity.getId()),
                    () -> assertThat(responses.get(1).getDistance()).isEqualTo(insertedBackSectionDetailEntity.getDistance()),
                    () -> assertThat(responses.get(1).getLine().getId()).isEqualTo(insertedBackSectionDetailEntity.getLineId()),
                    () -> assertThat(responses.get(1).getLine().getName()).isEqualTo(insertedBackSectionDetailEntity.getLineName()),
                    () -> assertThat(responses.get(1).getLine().getColor()).isEqualTo(insertedBackSectionDetailEntity.getLineColor()),
                    () -> assertThat(responses.get(1).getPreviousStation().getId()).isEqualTo(insertedBackSectionDetailEntity.getPreviousStationId()),
                    () -> assertThat(responses.get(1).getPreviousStation().getName()).isEqualTo(insertedBackSectionDetailEntity.getPreviousStationName()),
                    () -> assertThat(responses.get(1).getNextStation().getId()).isEqualTo(insertedBackSectionDetailEntity.getNextStationId()),
                    () -> assertThat(responses.get(1).getNextStation().getName()).isEqualTo(insertedBackSectionDetailEntity.getNextStationName())
            );
        }

    }

}
