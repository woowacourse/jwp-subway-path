package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import subway.application.dto.PathDto;
import subway.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.application.PathServiceTest.Data.*;

@ActiveProfiles("test")
@Sql("/initialization.sql")
@SpringBootTest
class PathServiceTest {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private PathService pathService;

    @DisplayName("최단 경로 및 이용 요금을 조회할 수 있다.")
    @Test
    void test() {
        //given
        Long lineId1 = lineRepository.save(호선1);
        Long lineId2 = lineRepository.save(호선2);

        stationRepository.save(회기);
        stationRepository.save(왕십리);
        Long 청량리Id = stationRepository.save(청량리);
        Long 잠실Id = stationRepository.save(잠실);
        sectionRepository.saveAll(lineId1, new Sections(List.of(회기_청량리, 청량리_왕십리)));
        sectionRepository.saveAll(lineId2, new Sections(List.of(왕십리_잠실)));

        //when
        PathDto shortestPath = pathService.findShortestPath(청량리Id, 잠실Id);

        //then
        assertThat(shortestPath.getPath()).extractingResultOf("getName")
                .containsExactly("청량리", "왕십리", "잠실");
        //15km = 1250 + 300
        assertThat(shortestPath.getCost()).isEqualTo(1550);
    }

    static class Data {
        static Line 호선1 = new Line("1호선");
        static Line 호선2 = new Line("2호선");

        static Station 회기 = new Station("회기");
        static Station 청량리 = new Station("청량리");
        static Station 왕십리 = new Station("왕십리");
        static Station 잠실 = new Station("잠실");

        static Section 회기_청량리 = new Section(회기, 청량리, new Distance(10));
        static Section 청량리_왕십리 = new Section(청량리, 왕십리, new Distance(5));
        static Section 왕십리_잠실 = new Section(왕십리, 잠실, new Distance(10));

    }

}
