package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static subway.SubwayFixture.*;

@ActiveProfiles("test")
class SectionsTest {
    private static final Distance DISTANCE_10 = new Distance(10);
    private static final Distance DISTANCE_6 = new Distance(6);

    private Sections sections;

    @BeforeEach
    void setUp() {
        this.sections = new Sections(new HashMap<>());
    }

    @DisplayName("올바른 역 간 거리를 계산한다.")
    @Test
    void getDistanceBetween() {
        sections.insertSectionBetween(2L, SADANG, JAMSIL, DISTANCE_10);
        sections.insertSectionBetween(2L, GUUI, SADANG, DISTANCE_6);
        Distance distance = sections.getDistanceBetween(GUUI, JAMSIL);

        assertThat(distance.getValue()).isEqualTo(16);
    }

    @DisplayName("상행 종점에서 하행 종점까지 정렬된 순서로 Section 리스트를 반환한다")
    @Test
    void getSections() {
        sections.insertSectionBetween(2L, SADANG, JAMSIL, DISTANCE_10);
        sections.insertSectionBetween(2L, GUUI, SADANG, DISTANCE_6);
        List<Station> stations = sections.getOrderedSections()
                .stream()
                .map(Section::getUpper)
                .collect(Collectors.toList());

        assertThat(stations).containsExactlyInAnyOrder(GUUI, SADANG);
    }
}
