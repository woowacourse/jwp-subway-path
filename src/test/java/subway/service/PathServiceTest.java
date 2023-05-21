package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.controller.exception.PathNotFoundException;
import subway.controller.exception.StationNotFoundException;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.service.dto.PathDto;
import subway.dto.response.PathResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        데이터베이스_초기화();
        노선_역_더미_등록();
    }

    //
    //              가락시장
    //              /    \
    //             /      4
    //            /        \
    //           /         문정
    //          /            \
    //         8              8
    //        /                \
    //       /                 장지
    //      /                    \
    //     /                      10
    //    /                        \
    //   수서 -------- 4 ---------- 복정
    //
    //    경기광주 ----- 10 ------- 삼동

    @Test
    void 시작역부터_도착역의_최단_경로를_구할_수_있다() {
        // when
        final PathResponse pathResponse = pathService.findPath(new PathDto("수서역", "장지역"));

        // then
        assertAll(
                () -> assertThat(pathResponse.getFare()).isEqualTo(1350),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(14),
                () -> assertThat(pathResponse.getStations()).containsExactly("수서역", "복정역", "장지역")
        );
    }

    @Test
    void 출발역이_노선에_없을_경우_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> pathService.findPath(new PathDto("터틀역", "수서역")))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("출발역이 존재하지 않습니다.");
    }

    @Test
    void 도착역이_노선에_없을_경우_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> pathService.findPath(new PathDto("수서역", "터틀역")))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("도착역이 존재하지 않습니다.");
    }

    @Test
    void 두_역_사이_경로가_존재하지_않는_경우_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> pathService.findPath(new PathDto("경기광주역", "수서역")))
                .isInstanceOf(PathNotFoundException.class)
                .hasMessageContaining("두 역 사이의 경로가 존재하지 않습니다");
    }


    private void 데이터베이스_초기화() {
        jdbcTemplate.execute("DELETE FROM line");
        jdbcTemplate.execute("DELETE FROM station");
        jdbcTemplate.execute("DELETE FROM section");
    }

    private void 노선_역_더미_등록() {
        final Long firstLineId = lineDao.insert("3호선", "주황색");
        final Long secondLineId = lineDao.insert("분당선", "노란색");
        final Long thirdLineId = lineDao.insert("8호선", "분홍색");
        final Long fourthLineId = lineDao.insert("경강선", "파란색");

        final StationEntity 수서역 = stationDao.insert("수서역");
        final StationEntity 가락시장역 = stationDao.insert("가락시장역");
        final StationEntity 복정역 = stationDao.insert("복정역");
        final StationEntity 장지역 = stationDao.insert("장지역");
        final StationEntity 문정역 = stationDao.insert("문정역");
        final StationEntity 경기광주역 = stationDao.insert("경기광주역");
        final StationEntity 삼동역 = stationDao.insert("삼동역");

        sectionDao.insert(firstLineId, 수서역.getId(), 가락시장역.getId(), 8);
        sectionDao.insert(secondLineId, 수서역.getId(), 복정역.getId(), 4);
        sectionDao.insert(thirdLineId, 가락시장역.getId(), 문정역.getId(), 4);
        sectionDao.insert(thirdLineId, 문정역.getId(), 장지역.getId(), 8);
        sectionDao.insert(thirdLineId, 장지역.getId(), 복정역.getId(), 10);
        sectionDao.insert(fourthLineId, 경기광주역.getId(), 삼동역.getId(), 10);
    }

}
