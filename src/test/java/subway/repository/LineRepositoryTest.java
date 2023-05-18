package subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.service.domain.Distance;
import subway.service.domain.Line;
import subway.service.domain.LineProperty;
import subway.service.domain.Section;
import subway.service.domain.Sections;
import subway.service.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

}
