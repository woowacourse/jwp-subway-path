package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class SectionDaoTest {

    private static final Long LINE_ID = 2L;
    private static final Long STATION_ID = 1L;
    private static final Long NEXT_STATION_ID = 2L;
    private static final int DISTANCE = 10;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;

    @BeforeEach
    void setup() {
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @DisplayName("지하철 구간 데이터를 삽입한다")
    @Test
    void insertSection() {
        SectionDto section = new SectionDto(LINE_ID, STATION_ID, NEXT_STATION_ID, DISTANCE);
        sectionDao.insert(section);

        assertThat(sectionDao.findAllByLineId(LINE_ID)).isNotNull();
    }

    @DisplayName("지하철 구간 데이터를 한번에 삽입한다")
    @Test
    void insertAllSections() {
        List<SectionDto> sectionDtos = List.of(
                new SectionDto(LINE_ID, 1L, 2L, DISTANCE),
                new SectionDto(LINE_ID, 2L, 3L, DISTANCE),
                new SectionDto(LINE_ID, 3L, 4L, DISTANCE)
        );
        sectionDao.insertAll(sectionDtos);

        assertThat(sectionDao.findAllByLineId(LINE_ID))
                .extracting("lineId", "stationId", "nextStationId", "distance")
                .contains(
                        tuple(LINE_ID, 1L, 2L, DISTANCE),
                        tuple(LINE_ID, 2L, 3L, DISTANCE),
                        tuple(LINE_ID, 3L, 4L, DISTANCE)
                );
    }

    @DisplayName("지하철 구간 데이터를 조회한다")
    @Test
    void findAllByLineId() {
        SectionDto section = new SectionDto(LINE_ID, STATION_ID, NEXT_STATION_ID, DISTANCE);
        sectionDao.insert(section);
        List<SectionDto> result = sectionDao.findAllByLineId(LINE_ID);

        assertThat(result).extracting("lineId", "stationId", "nextStationId", "distance")
                .contains(tuple(LINE_ID, STATION_ID, NEXT_STATION_ID, DISTANCE));
    }

    @DisplayName("호선의 지하철 구간 데이터를 삭제한다")
    @Test
    void deleteSection() {
        SectionDto section = new SectionDto(LINE_ID, STATION_ID, NEXT_STATION_ID, DISTANCE);
        sectionDao.insert(section);

        assertThat(sectionDao.deleteAllByLineId(LINE_ID)).isGreaterThanOrEqualTo(1);
    }
}
