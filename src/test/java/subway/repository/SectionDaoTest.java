package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.exception.DuplicatedSectionException;
import subway.exception.LineNotFoundException;
import subway.exception.LineOrStationNotFoundException;
import subway.entity.SectionDetailEntity;
import subway.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.entity.RowMapperUtil.sectionEntityRowMapper;

@JdbcTest
@DisplayName("Section Dao")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/station_test_data.sql", "/line_test_data.sql", "/section_test_data.sql"})
class SectionDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        sectionDao = new SectionDao(dataSource);
    }

    @Test
    @DisplayName("저장 성공")
    void insert_success() {
        // given
        final SectionEntity sectionEntity = new SectionEntity(1L, 10, 1L, 5L);

        // when
        sectionDao.insert(sectionEntity);

        // then
        final String sql = "SELECT * FROM section WHERE previous_station_id = ? AND next_station_id = ?";
        List<SectionEntity> response = jdbcTemplate.query(sql, sectionEntityRowMapper, 1, 5);
        assertThat(response).hasSize(1)
                .anyMatch(entity -> entity.getLineId() == 1L)
                .anyMatch(entity -> entity.getPreviousStationId() == 1L)
                .anyMatch(entity -> entity.getNextStationId() == 5L)
                .anyMatch(entity -> entity.getDistance() == 10);
    }

    @Test
    @DisplayName("저장 실패 - 중복된 구간")
    void insert_fail_duplicated_section() {
        // given
        SectionEntity sectionEntity = new SectionEntity(1L, 10, 1L, 2L);

        // when, then
        assertThatThrownBy(() -> sectionDao.insert(sectionEntity))
                .isInstanceOf(DuplicatedSectionException.class);
    }

    @Test
    @DisplayName("노선명으로 구간 조회 성공")
    void findByLineName_success() {
        // given
        final String lineName = "2호선";

        // when
        final List<SectionEntity> sectionEntities = sectionDao.findByLineName(lineName);
        final List<Long> previousStationIds = sectionEntities.stream()
                .map(SectionEntity::getPreviousStationId)
                .collect(Collectors.toUnmodifiableList());
        final List<Long> nextStationIds = sectionEntities.stream()
                .map(SectionEntity::getNextStationId)
                .collect(Collectors.toUnmodifiableList());

        // then
        assertAll(
                () -> assertThat(sectionEntities).hasSize(2),
                () -> assertThat(previousStationIds).containsAll(List.of(1L, 2L)),
                () -> assertThat(nextStationIds).containsAll(List.of(2L, 3L))
        );
    }

    @Test
    @DisplayName("노선명으로 구간 조회 실패 - 존재하지 않는 노선")
    void findByLineName_fail_name_not_found() {
        // given
        final String lineName = "ditoo";

        // when, then
        assertThatThrownBy(() -> sectionDao.findByLineName(lineName))
                .isInstanceOf(LineNotFoundException.class);
    }

    @Test
    @DisplayName("구간 삭제 성공")
    void delete_success() {
        // given
        final long sectionId = 1L;
        final String sql = "SELECT * FROM section WHERE id = ?";
        final SectionEntity sectionEntity = jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, sectionId);

        // when
        sectionDao.delete(sectionEntity);

        // then
        assertThatThrownBy(() -> jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, sectionId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("구간 여러개 삭제 성공")
    void deleteAll_success() {
        // given
        final long[] sectionIds = {1L, 2L};
        final String sql = "SELECT * FROM section WHERE id = ?";
        final SectionEntity sectionEntity1 = jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, sectionIds[0]);
        final SectionEntity sectionEntity2 = jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, sectionIds[1]);

        // when
        sectionDao.deleteAll(List.of(sectionEntity1, sectionEntity2));

        // then
        assertThatThrownBy(() -> jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, sectionIds[0]))
                .isInstanceOf(EmptyResultDataAccessException.class);
        assertThatThrownBy(() -> jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, sectionIds[1]))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("노선 id와 이전 역 id로 구간 조회 성공")
    void findByLineIdAndPreviousStationId_success() {
        // given
        final long lineId = 1L;
        final long previousStationId = 1L;

        // when
        Optional<SectionEntity> result = sectionDao.findByLineIdAndPreviousStationId(lineId, previousStationId);

        // then
        assertThat(result.isPresent()).isTrue();
        assertAll(
                () -> assertThat(result.get().getLineId()).isEqualTo(lineId),
                () -> assertThat(result.get().getPreviousStationId()).isEqualTo(previousStationId)
        );
    }

    @Test
    @DisplayName("노선 id와 이전 역 id로 구간 조회 성공 - empty")
    void findByLineIdAndPreviousStationId_success_empty() {
        // given
        final long lineId = 1L;
        final long previousStationId = 3L;

        // when
        Optional<SectionEntity> result = sectionDao.findByLineIdAndPreviousStationId(lineId, previousStationId);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("노선 id와 다음 역 id로 구간 조회 성공")
    void findByLineIdAndNextStationId_success() {
        // given
        final long lineId = 1L;
        final long nextStationId = 3L;

        // when
        Optional<SectionEntity> result = sectionDao.findByLineIdAndNextStationId(lineId, nextStationId);

        // then
        assertThat(result.isPresent()).isTrue();
        assertAll(
                () -> assertThat(result.get().getLineId()).isEqualTo(lineId),
                () -> assertThat(result.get().getNextStationId()).isEqualTo(nextStationId)
        );
    }

    @Test
    @DisplayName("노선 id와 다음 역 id로 구간 조회 성공 - empty")
    void findByLineIdAndNextStationId_success_empty() {
        // given
        final long lineId = 1L;
        final long nextStationId = 1L;

        // when
        Optional<SectionEntity> result = sectionDao.findByLineIdAndNextStationId(lineId, nextStationId);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("노선 id와 역 이름으로 구간 목록 조회 성공 - 중간역인 경우")
    void findByLineIdAndPreviousStationNameOrNextStationName_success_mid_station() {
        // given
        final long lineId = 1L;
        final String stationName = "잠실새내";

        // when
        final List<SectionEntity> sectionEntities =
                sectionDao.findByLineIdAndPreviousStationNameOrNextStationName(lineId, stationName);

        // then
        assertAll(
                () -> assertThat(sectionEntities).hasSize(2),
                () -> assertThat(sectionEntities.stream()
                        .map(entity -> entity.getLineId())
                        .distinct()
                        .count()).isEqualTo(1),
                () -> assertThat(sectionEntities.stream()
                        .map(entity -> entity.getNextStationId())
                        .collect(Collectors.toUnmodifiableList())).contains(2L),
                () -> assertThat(sectionEntities.stream()
                        .map(entity -> entity.getPreviousStationId())
                        .collect(Collectors.toUnmodifiableList())).contains(2L)
        );
    }

    @Test
    @DisplayName("노선 id와 역 이름으로 구간 목록 조회 성공 - 종점인 경우")
    void findByLineIdAndPreviousStationNameOrNextStationName_success_end_station() {
        // given
        final long lineId = 1L;
        final String stationName = "잠실";

        // when
        final List<SectionEntity> sectionEntities =
                sectionDao.findByLineIdAndPreviousStationNameOrNextStationName(lineId, stationName);

        // then
        assertAll(
                () -> assertThat(sectionEntities).hasSize(1),
                () -> assertThat(sectionEntities.get(0).getLineId()).isEqualTo(lineId),
                () -> assertThat(sectionEntities.get(0).getPreviousStationId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("노선 id와 역 이름으로 구간 목록 조회 실패 - 노선에 없는 역인 경우")
    void findByLineIdAndPreviousStationNameOrNextStationName_fail_result_not_found() {
        // given
        final long lineId = 1L;
        final String stationName = "석촌";

        // when, then
        assertThatThrownBy(() -> sectionDao.findByLineIdAndPreviousStationNameOrNextStationName(lineId, stationName))
                .isInstanceOf(LineOrStationNotFoundException.class);
    }

    @Test
    @DisplayName("노선에 역 존재 확인 - true")
    void isStationExistInLine_true() {
        // given
        final long lineId = 1L;
        final long stationId = 1L;

        // when
        boolean exist = sectionDao.isStationExistInLine(lineId, stationId);

        // then
        assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("노선에 역 존재 확인 - false")
    void isStationExistInLine_false() {
        // given
        final long lineId = 1L;
        final long stationId = 4L;

        // when
        boolean exist = sectionDao.isStationExistInLine(lineId, stationId);

        // then
        assertThat(exist).isFalse();
    }

    @Test
    @DisplayName("구간 전체 조회 성공")
    void findSectionDetail_success() {
        // given, when
        final List<SectionDetailEntity> sectionDetailEntities = sectionDao.findSectionDetail();

        // then
        assertAll(
                () -> assertThat(sectionDetailEntities).hasSize(4),
                () -> assertThat(sectionDetailEntities.get(0).getDistance()).isEqualTo(3),
                () -> assertThat(sectionDetailEntities.get(0).getLineId()).isEqualTo(1L),
                () -> assertThat(sectionDetailEntities.get(0).getLineName()).isEqualTo("2호선"),
                () -> assertThat(sectionDetailEntities.get(0).getLineColor()).isEqualTo("bg-green-600"),
                () -> assertThat(sectionDetailEntities.get(0).getPreviousStationId()).isEqualTo(1L),
                () -> assertThat(sectionDetailEntities.get(0).getPreviousStationName()).isEqualTo("잠실"),
                () -> assertThat(sectionDetailEntities.get(0).getNextStationId()).isEqualTo(2L),
                () -> assertThat(sectionDetailEntities.get(0).getNextStationName()).isEqualTo("잠실새내")
        );
    }

    @Test
    @DisplayName("구간 상세 조회 성공")
    void findSectionDetailById_success() {
        // given
        final long id = 1L;

        // when
        final SectionDetailEntity sectionDetailEntity = sectionDao.findSectionDetailById(id);

        // then
        assertAll(
                () -> assertThat(sectionDetailEntity.getDistance()).isEqualTo(3),
                () -> assertThat(sectionDetailEntity.getLineId()).isEqualTo(1L),
                () -> assertThat(sectionDetailEntity.getLineName()).isEqualTo("2호선"),
                () -> assertThat(sectionDetailEntity.getLineColor()).isEqualTo("bg-green-600"),
                () -> assertThat(sectionDetailEntity.getPreviousStationId()).isEqualTo(1L),
                () -> assertThat(sectionDetailEntity.getPreviousStationName()).isEqualTo("잠실"),
                () -> assertThat(sectionDetailEntity.getNextStationId()).isEqualTo(2L),
                () -> assertThat(sectionDetailEntity.getNextStationName()).isEqualTo("잠실새내")
        );
    }

    @Test
    @DisplayName("구간 상세 조회 실패 - 존재하지 않는 id")
    void findSectionDetailById_fail_invalid_id() {
        // given
        final long id = 111L;

        // when, then
        assertThatThrownBy(() -> sectionDao.findSectionDetailById(id))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("노선 id에 해당하는 구간 조회 성공")
    void findSectionDetailByLineId_success() {
        // given
        final long lineId = 1L;

        // when
        final List<SectionDetailEntity> sectionDetailEntities = sectionDao.findSectionDetailByLineId(lineId);

        // then
        assertAll(
                () -> assertThat(sectionDetailEntities).hasSize(2),
                () -> assertThat(sectionDetailEntities.get(0).getDistance()).isEqualTo(3),
                () -> assertThat(sectionDetailEntities.get(0).getLineId()).isEqualTo(1L),
                () -> assertThat(sectionDetailEntities.get(0).getLineName()).isEqualTo("2호선"),
                () -> assertThat(sectionDetailEntities.get(0).getLineColor()).isEqualTo("bg-green-600"),
                () -> assertThat(sectionDetailEntities.get(0).getPreviousStationId()).isEqualTo(1L),
                () -> assertThat(sectionDetailEntities.get(0).getPreviousStationName()).isEqualTo("잠실"),
                () -> assertThat(sectionDetailEntities.get(0).getNextStationId()).isEqualTo(2L),
                () -> assertThat(sectionDetailEntities.get(0).getNextStationName()).isEqualTo("잠실새내")
        );
    }

    @Test
    @DisplayName("노선 id에 해당하는 구간 조회 실패 - 존재하지 않는 노선 id")
    void findSectionDetailByLineId_fail_line_not_found() {
        // given
        final long lineId = 10L;

        // when, then
        assertThatThrownBy(() -> sectionDao.findSectionDetailByLineId(lineId))
                .isInstanceOf(LineNotFoundException.class);
    }
}
