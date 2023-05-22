package subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.exception.LineNotFoundException;
import subway.service.domain.Distance;
import subway.service.domain.Line;
import subway.service.domain.LineProperty;
import subway.service.domain.Section;
import subway.service.domain.Sections;
import subway.service.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("lineProperty 를 넘겨 저장한다.")
    @Sql("/line_test_data.sql")
    void saveLineProperty() {
        LineProperty lineProperty = new LineProperty("1호선", "blue");

        LineProperty saveLineProperty = lineRepository.saveLineProperty(lineProperty);

        assertThat(saveLineProperty.getId()).isEqualTo(3L);
        assertThat(saveLineProperty.getName()).isEqualTo("1호선");
        assertThat(saveLineProperty.getColor()).isEqualTo("blue");
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("Line 을 저장한다. (Section 도 함께)")
    @Sql(scripts = {"/line_test_data.sql", "/section_test_data.sql", "/station_test_data.sql"})
    void save() {
        LineProperty lineProperty = new LineProperty("1호선", "blue");
        Station previousStation = new Station(1, "잠실");
        Station nextStation = new Station(2, "잠실새내");
        Section section = new Section(
                previousStation,
                nextStation,
                Distance.from(10)
        );

        Line line = new Line(lineProperty, new Sections(List.of(section)));
        Line saveLine = lineRepository.saveLine(line);

        assertThat(saveLine.getLineProperty().getId()).isEqualTo(3L);
        assertThat(saveLine.getLineProperty().getName()).isEqualTo("1호선");
        assertThat(saveLine.getLineProperty().getColor()).isEqualTo("blue");
        assertThat(saveLine.getSections().get(0).getPreviousStation()).isEqualTo(previousStation);
        assertThat(saveLine.getSections().get(0).getNextStation()).isEqualTo(nextStation);
        assertThat(saveLine.getSections().get(0).getDistance()).isEqualTo(10);
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("해당하는 이름의 노선이 존재하는지 확인한다.")
    @Sql("/line_test_data.sql")
    void existsByName() {
        assertThat(lineRepository.existsByName("2호선")).isTrue();
        assertThat(lineRepository.existsByName("1호선")).isFalse();
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("해당하는 ID 의 노선을 반환받는다.")
    @Sql(scripts = {"/line_test_data.sql", "/section_test_data.sql", "/station_test_data.sql"})
    void findById() {
        Station previous = new Station(1, "잠실");
        Station middle = new Station(2, "잠실새내");
        Station next = new Station(3, "종합운동장");
        Line line = lineRepository.findById(1L);

        assertThat(line.getLineProperty().getId()).isEqualTo(1L);
        assertThat(line.getLineProperty().getName()).isEqualTo("2호선");
        assertThat(line.getLineProperty().getColor()).isEqualTo("bg-green-600");
        assertThat(line.getSections()).hasSize(2)
                .anyMatch(section -> section.getDistance() == 3
                        && section.getPreviousStation().equals(previous)
                        && section.getNextStation().equals(middle))
                .anyMatch(section -> section.getDistance() == 4
                        && section.getPreviousStation().equals(middle)
                        && section.getNextStation().equals(next));
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("해당하는 Name 의 노선을 반환받는다.")
    @Sql(scripts = {"/line_test_data.sql", "/section_test_data.sql", "/station_test_data.sql"})
    void findByName() {
        Station previous = new Station(1, "잠실");
        Station middle = new Station(2, "잠실새내");
        Station next = new Station(3, "종합운동장");
        Line line = lineRepository.findByName("2호선");

        assertThat(line.getLineProperty().getId()).isEqualTo(1L);
        assertThat(line.getLineProperty().getName()).isEqualTo("2호선");
        assertThat(line.getLineProperty().getColor()).isEqualTo("bg-green-600");
        assertThat(line.getSections()).hasSize(2)
                .anyMatch(section -> section.getDistance() == 3
                        && section.getPreviousStation().equals(previous)
                        && section.getNextStation().equals(middle))
                .anyMatch(section -> section.getDistance() == 4
                        && section.getPreviousStation().equals(middle)
                        && section.getNextStation().equals(next));
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("해당하는 Name 의 노선을 반환받는다. (실패)")
    @Sql(scripts = {"/line_test_data.sql", "/section_test_data.sql", "/station_test_data.sql"})
    void findByName_fail() {
        assertThatThrownBy(() -> lineRepository.findByName("재연"))
                .isInstanceOf(LineNotFoundException.class);
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     *
     * INSERT INTO section(line_id, distance, previous_station_id, next_station_id)
     * VALUES(1, 3, 1, 2), (1, 4, 2, 3), (2, 5, 1, 4), (2, 6, 4, 5);
     *
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @Test
    @DisplayName("모든 노선을 반환받는다.")
    @Sql(scripts = {"/line_test_data.sql", "/section_test_data.sql", "/station_test_data.sql"})
    void findAll() {
        assertThat(lineRepository.findAll())
                .hasSize(2)
                .allMatch(line -> line.getLineProperty().getName().equals("2호선")
                        || line.getLineProperty().getName().equals("8호선"));
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("해당하는 ID 의 노선을 반환받는다. (실패)")
    @Sql("/line_test_data.sql")
    void findById_fail() {
        assertThatThrownBy(() -> lineRepository.findById(3L))
                .isInstanceOf(LineNotFoundException.class);
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("LineProperty 를 update 한다.")
    @Sql("/line_test_data.sql")
    void updateLineProperty() {
        LineProperty lineProperty = new LineProperty(1L, "2호선", "bg-green-600");
        assertDoesNotThrow(() -> lineRepository.updateLineProperty(lineProperty));
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("LineProperty 를 update 한다. (실패)")
    @Sql("/line_test_data.sql")
    void updateLineProperty_fail() {
        LineProperty lineProperty = new LineProperty(100L, "2호선", "bg-green-600");

        assertThatThrownBy(() -> lineRepository.updateLineProperty(lineProperty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("Line 을 삭제한다.")
    @Sql("/line_test_data.sql")
    void deleteById() {
        assertDoesNotThrow(() -> lineRepository.deleteById(1L));
    }

    /**
     * INSERT INTO line(name, color)
     * VALUES('2호선', 'bg-green-600'), ('8호선', 'bg-pink-600');
     */
    @Test
    @DisplayName("Line 을 삭제한다. (실패)")
    @Sql("/line_test_data.sql")
    void deleteById_fail() {
        assertThatThrownBy(() -> lineRepository.deleteById(4L))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
