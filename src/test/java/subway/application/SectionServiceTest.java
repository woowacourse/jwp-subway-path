package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

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
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock
    private SectionDao sectionDao;
    @Mock
    private StationDao stationDao;
    @Mock
    private LineDao lineDao;
    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        sectionService = new SectionService(sectionDao, stationDao, lineDao);
    }

    @Test
    @DisplayName("Section이 정상적으로 저장된다.")
    void save() {
        // given
        given(stationDao.findById(anyLong())).willReturn(Optional.of(StationEntity.of(1L, "용산역")));
        willDoNothing().given(sectionDao).insertAll(any());

        // when & then
        assertDoesNotThrow(() -> sectionService.save(
                new SectionRequest(1L, 1L, 2L, 10))
        );
    }

    @Test
    @DisplayName("Section이 정상적으로 삭제된다.")
    void delete() {
        // given
        given(stationDao.findById(anyLong())).willReturn(Optional.of(StationEntity.of(1L, "용산역")));
        given(sectionDao.findByLineId(anyLong())).willReturn(
                List.of(new SectionEntity(1L, 1L, 2L, 10))
        );
        willDoNothing().given(sectionDao).deleteAllByLineId(anyLong());

        // when & then
        assertDoesNotThrow(() -> sectionService.delete(1L, 1L));
    }

    @Test
    @DisplayName("lineId로 Section이 정상적으로 조회된다.")
    void findByLineId() {
        // given
        final SectionEntity sectionEntity = new SectionEntity(1L, 1L, 2L, 10);
        given(sectionDao.findByLineId(anyLong())).willReturn(List.of(sectionEntity));

        // when
        final List<SectionResponse> sectionResponses = sectionService.findByLineId(1L);

        // then
        assertThat(sectionResponses.size()).isEqualTo(1);
        assertThat(sectionResponses.get(0).getUpStationId()).isEqualTo(sectionEntity.getUpStationId());
        assertThat(sectionResponses.get(0).getDownStationId()).isEqualTo(sectionEntity.getDownStationId());
        assertThat(sectionResponses.get(0).getDistance()).isEqualTo(sectionEntity.getDistance());
    }
}
