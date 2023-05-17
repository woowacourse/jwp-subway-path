package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fixture.Fixture;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.farecalculator.FareCalculatorImpl;
import subway.domain.farecalculator.policy.additional.LineAdditionalFarePolicy;
import subway.domain.farecalculator.policy.discount.AgeDiscountPolicy;
import subway.domain.farecalculator.policy.distance.BasicFareByDistancePolicy;
import subway.domain.pathfinder.JgraphtPathFinder;
import subway.domain.pathfinder.LineWeightedEdge;
import subway.dto.PathResponse;
import subway.exception.DomainException;
import subway.exception.ExceptionType;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    PathService pathService;
    @Mock
    SectionDao sectionDao;
    @Mock
    LineDao lineDao;
    @Mock
    StationDao stationDao;

    @BeforeEach
    void setUp() {
        pathService = new PathService(
                new JgraphtPathFinder(new WeightedMultigraph<>(LineWeightedEdge.class)),
                new FareCalculatorImpl(
                        new BasicFareByDistancePolicy(),
                        new LineAdditionalFarePolicy(),
                        new AgeDiscountPolicy()
                ),
                lineDao,
                stationDao,
                sectionDao
        );

    }


    @Test
    @DisplayName("source로 부터 target 까지의 경로와 거리, 요금을 반환한다.")
    void computePath() {
        //given
        when(sectionDao.findAll()).thenReturn(Fixture.SECTIONS);
        when(stationDao.findAll()).thenReturn(Fixture.STATIONS);
        when(lineDao.findAll()).thenReturn(Fixture.LINES);

        //when
        final PathResponse result = pathService.computePath(1L, 8L);

        //then
        assertAll(
                () -> assertThat(result.getDistance()).isEqualTo(15),
                () -> assertThat(result.getFareResponses()).hasSize(3),
                () -> assertThat(result.getFareResponses().get(0).getType()).isEqualTo("DEFAULT"),
                () -> assertThat(result.getFareResponses().get(0).getFare()).isEqualTo(1650),
                () -> assertThat(result.getFareResponses().get(1).getType()).isEqualTo("YOUTH"),
                () -> assertThat(result.getFareResponses().get(1).getFare()).isEqualTo(1040),
                () -> assertThat(result.getFareResponses().get(2).getType()).isEqualTo("CHILD"),
                () -> assertThat(result.getFareResponses().get(2).getFare()).isEqualTo(650),
                () -> assertThat(result.getPath()).hasSize(3)
        );
    }

    @Test
    @DisplayName("source 와 target이 같은 경우 예외가 발생한다.")
    void computePathFailBySameStation() {
        assertThatThrownBy(() -> pathService.computePath(1L, 1L))
                .isInstanceOf(DomainException.class)
                .hasMessage(ExceptionType.SOURCE_IS_SAME_WITH_TARGET.name());
    }
}
