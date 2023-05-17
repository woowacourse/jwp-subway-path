package subway.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.station.Station;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.SectionEntity.Builder;
import subway.persistence.entity.StationEntity;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class StationRepositoryTest {

    @Mock
    StationDao stationDao;

    @Mock
    SectionDao sectionDao;

    StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository = new StationRepository(stationDao, sectionDao);
    }

    @Test
    void insert_메소드는_station을_저장하고_저장한_데이터를_반환한다() {
        final Station station = Station.from("12역");
        given(stationDao.insert(any(StationEntity.class))).willReturn(
                StationEntity.of(1L, station.getName()));

        final Station actual = stationRepository.insert(station);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getName()).isEqualTo(station.getName())
        );
    }

    @Test
    void insert_메소드는_지정한_역_이름이_이미_존재하는_경우_예외가_발생한다() {
        final Station station = Station.from("12역");
        given(stationDao.existsByName(anyString())).willReturn(true);

        assertThatThrownBy(() -> stationRepository.insert(station))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지정한 역의 이름은 이미 존재하는 이름입니다.");
    }

    @Test
    void findById_메소드는_저장되어_있는_id를_전달하면_해당_station을_반환한다() {
        final StationEntity stationEntity = StationEntity.of(1L, "12역");
        given(stationDao.findById(anyLong())).willReturn(Optional.of(stationEntity));

        final Optional<Station> actual = stationRepository.findById(1L);

        assertThat(actual).isPresent();
    }

    @Test
    void findById_메소드는_없는_id를_전달하면_빈_Optional을_반환한다() {
        given(stationDao.findById(anyLong())).willReturn(Optional.empty());

        final Optional<Station> actual = stationRepository.findById(-999L);

        assertThat(actual).isEmpty();
    }

    @Test
    void findAllByIds_메소드는_ids를_전달하면_해당_id를_가진_station을_반환한다() {
        final StationEntity stationEntity = StationEntity.of(1L, "12역");
        given(stationDao.findAllByIds(any(Set.class))).willReturn(List.of(stationEntity));

        final Map<Long, Station> actual = stationRepository.findAllByIds(Set.of(1L));

        assertThat(actual).hasSize(1);
    }

    @Test
    void deletById_메소드는_노선에_등록되지_않은_id를_전달하면_해당_id를_가진_station을_삭제한다() {
        assertDoesNotThrow(() -> stationRepository.deleteById(1L));
    }

    @Test
    void deleteById_메소드는_노선에_등록된_id를_전달하면_예외가_발생한다() {
        final SectionEntity sectionEntity = Builder.builder()
                .id(1L)
                .lineId(1L)
                .upStationId(1L)
                .downStationId(2L)
                .distance(5)
                .build();
        given(sectionDao.findAllByStationId(anyLong())).willReturn(List.of(sectionEntity));

        assertThatThrownBy(() -> stationRepository.deleteById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선에 등록되어 있는 역은 삭제할 수 없습니다.");
    }
}
