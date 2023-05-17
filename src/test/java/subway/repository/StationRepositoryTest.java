package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.exception.DuplicateException;
import subway.exception.ErrorMessage;
import subway.exception.NotFoundException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        StationDao stationDao = new StationDao(jdbcTemplate);
        stationRepository = new StationRepository(stationDao);
    }

    @Test
    void 역을_입력받아_저장한다() {
        // given
        Station 역 = Station.createWithoutId("잠실역");

        // when
        Long 역_ID = stationRepository.save(역);
        Station 찾은_역 = stationRepository.findById(역_ID);

        // then
        assertThat(역.getName()).isEqualTo(찾은_역.getName());
    }

    @Test
    void 존재하는_이름을_갖는_역을_저장하면_예외가_발생한다() {
        // given
        Station 역 = Station.createWithoutId("잠실역");
        stationRepository.save(역);
        Station 중복_역 = Station.createWithoutId("잠실역");

        // then
        assertThatThrownBy(() -> stationRepository.save(중복_역))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ErrorMessage.DUPLICATE_NAME.getErrorMessage());
    }

    @Test
    void 역_ID를_입력받아_역을_반환한다() {
        // given
        Station 역 = Station.createWithoutId("잠실역");
        Long 역_ID = stationRepository.save(역);

        // when
        Station 찾은_역 = stationRepository.findById(역_ID);

        // then
        assertThat(역.getName()).isEqualTo(찾은_역.getName());
    }

    @Test
    void 존재하지_않는_역_ID를_입력_받으면_예외를_반환한다() {
        // then
        assertThatThrownBy(() -> stationRepository.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorMessage.NOT_FOUND_STATION.getErrorMessage());
    }
}
