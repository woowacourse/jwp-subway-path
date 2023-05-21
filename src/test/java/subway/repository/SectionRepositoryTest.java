package subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.entity.StationEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SectionRepositoryTest {

    @InjectMocks
    private SectionRepository sectionRepository;

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationDao stationDao;

    @Test
    @DisplayName("노선에 초기 두 역을 저장한다.")
    void register_init_station() {
        // given
        List<StationEntity> stationEntities = List.of(
                new StationEntity(1L, "잠실역", 1L),
                new StationEntity(2L, "선릉역", 1L));
        doReturn(stationEntities).when(stationDao).insertInit(any(List.class));


        // when
        List<Station> result = sectionRepository.saveInitStations(
                new Section(new Station("잠실역"), new Station("선릉역"), 10),
                1L);

        // then
        assertThat(result.get(0).getId()).isEqualTo(stationEntities.get(0).getId());
        assertThat(result.get(0).getName()).isEqualTo(stationEntities.get(0).getName());
        assertThat(result.get(1).getId()).isEqualTo(stationEntities.get(1).getId());
        assertThat(result.get(1).getName()).isEqualTo(stationEntities.get(1).getName());
    }

}