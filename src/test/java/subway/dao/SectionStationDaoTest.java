package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.SubwayJdbcFixture;
import subway.dao.dto.SectionStationResultMap;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("SectionStationDao를 테스트한다.")
class SectionStationDaoTest extends SubwayJdbcFixture {

    private SectionStationDao sectionStationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        sectionStationDao = new SectionStationDao(jdbcTemplate);
    }

    @Test
    void 노선_아이디로_노선정보를_반환한다() {
        // when
        final List<SectionStationResultMap> resultMaps = sectionStationDao.findAllByLineId(이호선);

        // then
        assertAll(
                () -> assertThat(resultMaps).hasSize(3),
                () -> assertThat(extractAllStationNames(resultMaps)).containsExactlyInAnyOrder("잠실역", "잠실새내역", "삼성역", "선릉역")
        );
    }

    @Test
    void 노선_전체_정보를_반환한다() {
        // when
        final List<SectionStationResultMap> resultMaps = sectionStationDao.findAll();

        // then
        assertAll(
                () -> assertThat(resultMaps).hasSize(5),
                () -> assertThat(extractAllStationNames(resultMaps)).containsExactlyInAnyOrder("잠실역", "잠실새내역", "삼성역", "선릉역", "몽촌토성역", "석촌역")
        );
    }

    private static List<String> extractAllStationNames(List<SectionStationResultMap> resultMaps) {
        return resultMaps.stream()
                .flatMap(resultMap -> Stream.of(resultMap.getUpStationName(), resultMap.getDownStationName()))
                .distinct()
                .collect(Collectors.toList());
    }
}
