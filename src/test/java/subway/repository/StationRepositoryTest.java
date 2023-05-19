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
import subway.exception.DuplicatedNameException;
import subway.repository.dao.StationDao;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationRepository stationRepository;

    @BeforeEach
    void setUp() {
        stationRepository = new StationRepository(new StationDao(jdbcTemplate));
    }

    @Test
    void 이미_존재하는_역을_저장하면_예외가_발생한다() {
        // given
        stationRepository.insert("강남역");

        // when, then
        assertThatThrownBy(() -> stationRepository.insert("강남역"))
                .isInstanceOf(DuplicatedNameException.class)
                .hasMessage("이미 존재하는 이름입니다. (입력값 : 강남역)");
    }
}
