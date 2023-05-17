package subway.dao;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@JdbcTest
@Import({RouteDAO.class, StationDao.class, LineDao.class, SectionDAO.class})
class RouteDAOTest {
    
    @Autowired
    private RouteDAO routeDAO;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private SectionDAO sectionDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Test
    @DisplayName("노선에 속한 모든 역을 조회한다.")
    void findAllStationsInLine() {
        // given
        final Station 강동역 = this.stationDao.insert(new Station("강동역"));
        final Station 강일역 = this.stationDao.insert(new Station("강일역"));
        final Station 고덕역 = this.stationDao.insert(new Station("고덕역"));
        
        final Line 호선 = this.lineDao.insert(new Line("호선", "PINK"));
        
        final Section section1 = new Section(호선.getId(), 강동역.getId(), 강일역.getId(), 10);
        this.sectionDAO.insert(section1);
        
        final Section section2 = new Section(호선.getId(), 강일역.getId(), 고덕역.getId(), 10);
        this.sectionDAO.insert(section2);
        
        final long lineId = 1L;
        
        // when
        final List<Station> allStationsInLine = this.routeDAO.findAllStationsInLine(lineId);
        
        // then
        Assertions.assertThat(allStationsInLine).containsExactly(강동역, 강일역, 고덕역);
        
    }
}