package subway.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.service.domain.Distance;
import subway.service.domain.Section;
import subway.service.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SectionRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    /**
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     */
    @Test
    @DisplayName("SectionRepository 에 Section 을 넘겨 저장한다.")
    @Sql("/section_test_data.sql")
    void save() {
        Section section = new Section(
                new Station(1, "previous"),
                new Station(2, "next"),
                Distance.from(10)
        );

        Section saveSection = sectionRepository.save(1L, section);

        assertThat(saveSection.getId()).isEqualTo(5L);
        assertThat(saveSection.getPreviousStation().getId()).isEqualTo(1L);
        assertThat(saveSection.getNextStation().getId()).isEqualTo(2L);
        assertThat(saveSection.getDistance()).isEqualTo(10);
    }

    /**
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     */
    @Test
    @DisplayName("SectionRepository 에 id 를 넘겨 Section 을 삭제한다.")
    @Sql("/section_test_data.sql")
    void deleteById() {
        Assertions.assertDoesNotThrow(
                () -> sectionRepository.deleteById(1L));
    }

    /**
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     */
    @Test
    @DisplayName("SectionRepository 에 id 를 넘겨 Section 을 삭제한다. (실패)")
    @Sql("/section_test_data.sql")
    void deleteById_fail() {
        assertThatThrownBy(() -> sectionRepository.deleteById(100L))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
