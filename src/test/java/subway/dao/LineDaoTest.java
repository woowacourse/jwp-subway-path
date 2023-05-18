package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import subway.entity.LineEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = LineDao.class)
public class LineDaoTest {

    @Autowired
    private LineDao lineDao;

    @DisplayName("추가한다.")
    @Test
    public void insert() {
        // given
        String lineName = "a";
        String color = "#FFFFFF";
        LineEntity line = new LineEntity(null, lineName, color, null, null);

        // when
        LineEntity insertedLine = lineDao.insert(line);

        // then
        assertThat(insertedLine.getId()).isNotNull();
        assertThat(insertedLine.getName()).isEqualTo(lineName);
        assertThat(insertedLine.getColor()).isEqualTo(color);
        assertThat(insertedLine.getUpBoundStationId()).isNull();
        assertThat(insertedLine.getDownBoundStationId()).isNull();
    }

    @DisplayName("모든 노선을 조회한다.")
    @Test
    public void find_all() {
        // given
        String lineName1 = "a";
        LineEntity line1 = new LineEntity(null, lineName1, "Red", null, null);
        String lineName2 = "aa";
        LineEntity line2 = new LineEntity(null, lineName2, "Blue", null, null);
        lineDao.insert(line1);
        lineDao.insert(line2);

        // when
        List<LineEntity> lines = lineDao.findAll();

        // then
        assertThat(lines).hasSize(2);
        assertThat(lines.get(0).getName()).isEqualTo(lineName1);
        assertThat(lines.get(1).getName()).isEqualTo(lineName2);
    }

    @DisplayName("있는 것을 ID로 조회")
    @Test
    public void exists_by_id_with_existing_id() {
        // given
        LineEntity line = new LineEntity(null, "1호선", "#FFFFFF", null, null);
        LineEntity insertedLine = lineDao.insert(line);

        // when
        boolean exists = lineDao.existsById(insertedLine.getId());

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("없는 것을 ID로 조회")
    @Test
    public void exists_by_id_with_non_existing_id() {
        // when
        boolean exists = lineDao.existsById(1L);

        // then
        assertThat(exists).isFalse();
    }

    @DisplayName("갱신한다.")
    @Test
    public void update() {
        // given
        String lineName = "1호선";
        LineEntity line = new LineEntity(null, lineName, "#123456", null, null);
        LineEntity inserted = lineDao.insert(line);
        String updateName = "update";
        String updateColor = "#FFFFFF";
        LineEntity updateLine = new LineEntity(inserted.getId(), updateName, updateColor, null, null);

        // when
        LineEntity updatedLine = lineDao.update(updateLine);

        // then
        assertThat(updatedLine.getId()).isEqualTo(updatedLine.getId());
        assertThat(updatedLine.getName()).isEqualTo(updateName);
        assertThat(updatedLine.getColor()).isEqualTo(updateColor);
    }

    @DisplayName("id로 제거한다.")
    @Test
    public void delete_by_id() {
        //given
        Long lineId = 1L;
        lineDao.deleteById(lineId);

        //when
        Optional<LineEntity> optionalLine = lineDao.findById(lineId);

        //then
        assertThat(optionalLine.isEmpty()).isTrue();
    }

    @DisplayName("id로 조회한다.")
    @Test
    public void find_by_id() {
        //given
        LineEntity inserted = lineDao.insert(new LineEntity(null, "test", "#DDDDDD", null, null));
        Long id = inserted.getId();

        //when
        Optional<LineEntity> optionalLine = lineDao.findById(id);
        LineEntity line = optionalLine.get();

        //then
        assertThat(id).isEqualTo(line.getId());
    }
}
