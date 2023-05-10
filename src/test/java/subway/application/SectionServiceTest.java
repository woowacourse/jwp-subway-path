package subway.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.SectionSaveRequest;
import subway.integration.IntegrationTest;

class SectionServiceTest extends IntegrationTest {

    public static final Station station1 = new Station("암사역");
    public static final Station station2 = new Station("숙대역");
    public static final Station station3 = new Station("잠실나루");
    public static final Station station4 = new Station("잠실");
    public static final Station station5 = new Station("나루");
    public static final SectionSaveRequest request1 = new SectionSaveRequest(1L, 2L, 10);
    public static final Line line1 = new Line("line1", "black");
    @Autowired
    SectionService sectionService;

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Test
    @DisplayName("최초로 구간을 등록한다.")
    void addInitialSection_success() {
        long id = initInsert();
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("하행종점에 구간을 등록한다.")
    void addDownEndSection_success() {
        //given
        initInsert();

        //when
        SectionSaveRequest endSectionRequest = new SectionSaveRequest(2L, 3L, 5);

        long id = sectionService.addSection(1L, endSectionRequest);
        assertThat(id).isEqualTo(2L);
    }

    @Test
    @DisplayName("상행종점에 구간을 등록한다.")
    void addUpEndSection_success() {
        //given
        initInsert();

        //when
        SectionSaveRequest endSectionRequest = new SectionSaveRequest(3L, 1L, 5);

        long id = sectionService.addSection(1L, endSectionRequest);
        assertThat(id).isEqualTo(2L);
    }


    @Test
    @DisplayName("구간 중간 - 새로운 길이가 더 큰 구간을 등록하려하면 실패한다.")
    void addMiddleSection_fail_distance_too_big() {
        //given
        initInsert();

        //when
        SectionSaveRequest middleSectionRequest = new SectionSaveRequest(3L, 2L, 10);

        assertThatThrownBy(() -> sectionService.addSection(1L, middleSectionRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("구간 중간 - 오른쪽에 새로운 구간을 등록한다.")
    void addMiddleSection_innerRight_success() {
        //given
        initInsert();

        //when
        SectionSaveRequest middleSectionRequest = new SectionSaveRequest(3L, 2L, 5);


        long id = sectionService.addSection(1L, middleSectionRequest);
        assertThat(id).isEqualTo(2L);       }


    @Test
    @DisplayName("구간 중간 - 왼쪽에 새로운 구간을 등록한다.")
    void addMiddleSection_innerLeft_success() {
        //given
        initInsert();

        //when
        SectionSaveRequest middleSectionRequest = new SectionSaveRequest(1L, 3L, 5);


        long id = sectionService.addSection(1L, middleSectionRequest);
        assertThat(id).isEqualTo(2L);       }

    private long initInsert() {
        lineDao.insert(line1);
        stationDao.insert(station1);
        stationDao.insert(station2);
        stationDao.insert(station3);
        stationDao.insert(station4);
        stationDao.insert(station5);

        return sectionService.addSection(1L, request1);
    }
}
