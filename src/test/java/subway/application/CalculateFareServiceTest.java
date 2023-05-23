package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.application.request.CreateLineRequest;
import subway.application.request.CreateSectionRequest;
import subway.application.request.UpdateLineExpenseRequest;
import subway.domain.Line;
import subway.domain.route.Route;
import subway.domain.route.RouteFinder;
import subway.domain.vo.Money;
import subway.repository.FareRepository;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import static subway.domain.PassengerType.*;
import static subway.fixture.StationFixture.역;

@Sql("/truncate.sql")
@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
class CalculateFareServiceTest {

    @Autowired
    CalculateFareService calculateFareService;

    @Autowired
    RouteFinder routeFinder;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    FareRepository fareRepository;

    List<Line> 전체_노선_목록;

    @BeforeEach
    void setUp() {
        LineService lineService = new LineService(lineRepository, fareRepository);
        GeneralFareService generalFareService = new GeneralFareService(fareRepository);
        StationService stationService = new StationService(stationRepository, sectionRepository, lineRepository);

        final Long 파랑_노선_식별자값 = lineService.saveLine(new CreateLineRequest("1", "파랑"));
        final Long 초록_노선_식별자값 = lineService.saveLine(new CreateLineRequest("2", "초록"));
        final Long 주황_노선_식별자값 = lineService.saveLine(new CreateLineRequest("3", "주황"));

        stationService.saveSection(new CreateSectionRequest("A", "B", 파랑_노선_식별자값, 10));
        stationService.saveSection(new CreateSectionRequest("B", "C", 파랑_노선_식별자값, 10));
        stationService.saveSection(new CreateSectionRequest("C", "D", 파랑_노선_식별자값, 10));
        stationService.saveSection(new CreateSectionRequest("D", "E", 초록_노선_식별자값, 10));
        stationService.saveSection(new CreateSectionRequest("E", "F", 초록_노선_식별자값, 10));
        stationService.saveSection(new CreateSectionRequest("F", "G", 주황_노선_식별자값, 10));

        generalFareService.updateLineExpense(파랑_노선_식별자값, new UpdateLineExpenseRequest("100", 10));
        generalFareService.updateLineExpense(초록_노선_식별자값, new UpdateLineExpenseRequest("500", 10));
        generalFareService.updateLineExpense(주황_노선_식별자값, new UpdateLineExpenseRequest("1000", 10));

        전체_노선_목록 = lineRepository.findAll();
    }

    @Test
    void 성인_요금을_계산한다() {
        // given
        final Route 최단_경로 = routeFinder.findRouteBy(전체_노선_목록, 역("A"), 역("G"));

        // when
        final Money totalFare = calculateFareService.calculate(ADULT, 최단_경로);

        // then
        assertThat(totalFare)
                .usingRecursiveComparison()
                .isEqualTo(Money.from("3550"));
    }

    @Test
    void 청소년_요금을_계산한다() {
        // given
        final Route 최단_경로 = routeFinder.findRouteBy(전체_노선_목록, 역("A"), 역("G"));

        // when
        final Money totalFare = calculateFareService.calculate(TEENAGER, 최단_경로);

        // then
        assertThat(totalFare)
                .usingRecursiveComparison()
                .isEqualTo(Money.from("2840"));
    }

    @Test
    void 어린이_요금을_계산한다() {
        // given
        final Route 최단_경로 = routeFinder.findRouteBy(전체_노선_목록, 역("A"), 역("G"));

        // when
        final Money totalFare = calculateFareService.calculate(CHILDREN, 최단_경로);

        // then
        assertThat(totalFare)
                .usingRecursiveComparison()
                .isEqualTo(Money.from("1775"));
    }

    @Test
    void 아기_요금을_계산한다() {
        // given
        final Route 최단_경로 = routeFinder.findRouteBy(전체_노선_목록, 역("A"), 역("G"));

        // when
        final Money totalFare = calculateFareService.calculate(BABY, 최단_경로);

        // then
        assertThat(totalFare)
                .usingRecursiveComparison()
                .isEqualTo(Money.from("0"));
    }
}
