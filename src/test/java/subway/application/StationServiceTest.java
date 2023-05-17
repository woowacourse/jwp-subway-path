package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.application.dto.CreationStationDto;
import subway.application.dto.ReadStationDto;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.repository.StationRepository;

@JdbcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class StationServiceTest {

    StationRepository stationRepository;
    SectionDao sectionDao;
    
    StationService stationService;
    
    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate, @Autowired DataSource dataSource) {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        final StationDao stationDao = new StationDao(jdbcTemplate, dataSource);
        
        stationRepository = new StationRepository(stationDao, sectionDao);
        stationService = new StationService(stationRepository);
    }
    
    @Test
    void saveStation_메소드는_station을_저장하고_저장한_데이터를_반환한다() {
        final CreationStationDto actual = stationService.saveStation("12호선");

        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getName()).isEqualTo("12호선")
        );
    }

    @Test
    void saveStation_메소드는_지정한_역_이름이_이미_존재하는_경우_예외가_발생한다() {
        stationService.saveStation("12호선");

        assertThatThrownBy(() ->  stationService.saveStation("12호선"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지정한 역의 이름은 이미 존재하는 이름입니다.");
    }

    @Test
    void findStationById_메소드는_저장되어_있는_id를_전달하면_해당_station을_반환한다() {
        final CreationStationDto creationStationDto = stationService.saveStation("12호선");

        final ReadStationDto actual = stationService.findStationById(creationStationDto.getId());

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(creationStationDto.getId()),
                () -> assertThat(actual.getName()).isEqualTo(creationStationDto.getName())
        );
    }

    @Test
    void findStationById_메소드는_없는_id를_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> stationService.findStationById(-999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }

    @Test
    void deleteStationById_메소드는_노선에_등록되지_않은_id를_전달하면_해당_id를_가진_station을_삭제한다() {
        assertDoesNotThrow(() -> stationService.deleteStationById(1L));
    }


    @Test
    void deleteStationById_메소드는_노선에_등록된_id를_전달하면_예외가_발생한다() {
        final SectionEntity sectionEntity = SectionEntity.Builder
                .builder()
                .lineId(1L)
                .upStationId(1L)
                .downStationId(2L)
                .distance(5)
                .build();
        sectionDao.insert(sectionEntity);

        assertThatThrownBy(() -> stationService.deleteStationById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선에 등록되어 있는 역은 삭제할 수 없습니다.");
    }
}
