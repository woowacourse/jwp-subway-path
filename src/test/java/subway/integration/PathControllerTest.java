package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayNameGeneration;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.dto.request.ReadPathRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PathControllerTest extends IntegrationTest {

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationDao stationDao;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void 최단_경로를_조회한다() {
        // given
        노선_역_더미_등록();
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
        //

        // expect
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ReadPathRequest("문정역", "수서역"))
                .when().post("/paths")
                .then().log().all()
                .body("distance", equalTo(12))
                .body("fare", equalTo(1350))
                .body("stations[0]", equalTo("문정역"))
                .body("stations[1]", equalTo("가락시장역"))
                .body("stations[2]", equalTo("수서역"))
                .statusCode(is(HttpStatus.OK.value()));
    }

    private void 노선_역_더미_등록() {
        final Long firstLineId = lineDao.insert("3호선", "주황색");
        final Long secondLineId = lineDao.insert("분당선", "노란색");
        final Long thirdLineId = lineDao.insert("8호선", "분홍색");

        final StationEntity 수서역 = stationDao.insert("수서역");
        final StationEntity 가락시장역 = stationDao.insert("가락시장역");
        final StationEntity 복정역 = stationDao.insert("복정역");
        final StationEntity 장지역 = stationDao.insert("장지역");
        final StationEntity 문정역 = stationDao.insert("문정역");

        sectionDao.insert(firstLineId, 수서역.getId(), 가락시장역.getId(), 8);
        sectionDao.insert(secondLineId, 수서역.getId(), 복정역.getId(), 4);
        sectionDao.insert(thirdLineId, 가락시장역.getId(), 문정역.getId(), 4);
        sectionDao.insert(thirdLineId, 문정역.getId(), 장지역.getId(), 8);
        sectionDao.insert(thirdLineId, 장지역.getId(), 복정역.getId(), 10);
    }

}