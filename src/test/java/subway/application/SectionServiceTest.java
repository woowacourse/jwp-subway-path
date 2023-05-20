package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.LineDao;
import subway.dao.SectionDAO;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.exception.InvalidInputException;

@JdbcTest
@Import({SectionService.class, SectionDAO.class, LineDao.class, StationDao.class})
class SectionServiceTest {
    
    @Autowired
    private SectionService sectionService;
    
    @Autowired
    private LineDao lineDao;
    
    @Autowired
    private StationDao stationDao;
    
    private Line testLine;
    private Station upTestStation;
    private Station downTestStation;
    
    @BeforeEach
    void setUp() {
        this.testLine = this.lineDao.insert(new Line("테스트라인", "TEST"));
        this.upTestStation = this.stationDao.insert(new Station("테스트역"));
        this.downTestStation = this.stationDao.insert(new Station("테스트역2"));
    }
    
    @Test
    @DisplayName("구간 저장 테스트 - 새로운 구간 추가")
    void saveNewSectionTest() {
        // given
        final int initialSize = this.sectionService.findAll().size();
        final List<SectionResponse> sectionResponses = this.새로운_테스트구역_추가();
        // then
        final int finalSize = this.sectionService.findAll().size();
        assertThat(sectionResponses).isNotNull();
        assertThat(finalSize).isEqualTo(initialSize + 1);
        assertThat(sectionResponses.get(0).getDistance()).isEqualTo(10);
    }
    
    private List<SectionResponse> 새로운_테스트구역_추가() {
        final LineRequest lineRequest = new LineRequest("테스트라인", "TEST");
        
        final SectionRequest sectionRequest = new SectionRequest(this.testLine.getId(), this.downTestStation.getId(),
                this.upTestStation.getId(), "DOWN", 10);
        // when
        final List<SectionResponse> sectionResponses = this.sectionService.insertSection(sectionRequest);
        return sectionResponses;
    }
    
    @Test
    @DisplayName("구간 저장 테스트 - 역들 사이에 새로운 구간 추가")
    void saveNewSectionBetweenStationsTest() {
        // given
        final int initialSize = this.sectionService.findAll().size();
        this.새로운_테스트구역_추가();
        final Station 테스트역3 = this.stationDao.insert(new Station("테스트역3"));
        final SectionRequest sectionRequest = new SectionRequest(this.testLine.getId(), 테스트역3.getId(),
                this.upTestStation.getId(), "DOWN", 2);
        
        // when
        final List<SectionResponse> sectionResponses = this.sectionService.insertSection(sectionRequest);
        // then
        final int finalSize = this.sectionService.findAll().size();
        assertThat(sectionResponses).isNotNull();
        assertThat(finalSize).isEqualTo(initialSize + 2);
        assertThat(sectionResponses.get(0).getDistance()).isEqualTo(2);
        assertThat(sectionResponses.get(1).getDistance()).isEqualTo(8);
    }
    
    @Test
    @DisplayName("구간 저장 테스트 - 새로운 종점역 추가")
    void saveNewSectionAtEndTest() {
        // given
        final int initialSize = this.sectionService.findAll().size();
        this.새로운_테스트구역_추가();
        final Station 테스트역3 = this.stationDao.insert(new Station("테스트역3"));
        final SectionRequest sectionRequest = new SectionRequest(this.testLine.getId(), 테스트역3.getId(),
                this.downTestStation.getId(), "DOWN", 2);
        
        // when
        final List<SectionResponse> sectionResponses = this.sectionService.insertSection(sectionRequest);
        // then
        final int finalSize = this.sectionService.findAll().size();
        assertThat(sectionResponses).isNotNull();
        assertThat(sectionResponses.size()).isEqualTo(1);
        assertThat(finalSize).isEqualTo(initialSize + 2);
        assertThat(sectionResponses.get(0).getDistance()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("구간 저장 테스트 - 새로운 구간 추가 실패 - 이미 존재하는 구간")
    void saveNewSectionFailTest() {
        // given
        this.새로운_테스트구역_추가();
        final SectionRequest sectionRequest = new SectionRequest(this.testLine.getId(), this.downTestStation.getId(),
                this.upTestStation.getId(), "DOWN", 10);
        // when
        // then
        assertThatThrownBy(() -> {
            this.sectionService.insertSection(sectionRequest);
        }).isInstanceOf(InvalidInputException.class);
    }
    
    @Test
    @DisplayName("구간 저장 테스트 - 새로운 구간 추가 실패 - 존재하지 않는 노선")
    void saveNewSectionFailTest2() {
        // given
        final SectionRequest sectionRequest = new SectionRequest(100L, this.downTestStation.getId(),
                this.upTestStation.getId(), "DOWN", 10);
        // when
        // then
        assertThatThrownBy(() -> {
            this.sectionService.insertSection(sectionRequest);
        }).isInstanceOf(InvalidInputException.class);
    }
    
    @Test
    @DisplayName("구간 저장 테스트 - 새로운 구간 추가 실패 - 존재하지 않는 역")
    void saveNewSectionFailTest3() {
        // given
        final SectionRequest sectionRequest = new SectionRequest(this.testLine.getId(), 100L,
                this.upTestStation.getId(), "DOWN", 10);
        // when
        // then
        assertThatThrownBy(() -> {
            this.sectionService.insertSection(sectionRequest);
        }).isInstanceOf(InvalidInputException.class);
    }
    
    @Test
    @DisplayName("구간 저장 테스트 - 새로운 구간 추가 실패 - 기존 역 사이 거리 보다 큰 거리")
    void saveNewSectionFailTest4() {
        // given
        this.새로운_테스트구역_추가();
        final SectionRequest sectionRequest = new SectionRequest(this.testLine.getId(), this.downTestStation.getId(),
                this.upTestStation.getId(), "DOWN", 100);
        // when
        // then
        assertThatThrownBy(() -> {
            this.sectionService.insertSection(sectionRequest);
        }).isInstanceOf(InvalidInputException.class);
    }
}