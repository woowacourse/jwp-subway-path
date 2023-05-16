package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class SectionsTest {

    private static final Station JAMSIL_NARU = new Station("잠실나루");
    private static final Station JAMSIL = new Station("잠실");
    private static final Station JAMSIL_SAENAE = new Station("잠실새내");
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
        sections.insertSectionBetween(JAMSIL_NARU, JAMSIL, DISTANCE_10);
        sections.insertSectionBetween(JAMSIL_SAENAE, JAMSIL_NARU, DISTANCE_6);
        Distance distance = sections.getDistanceBetween(JAMSIL_SAENAE, JAMSIL);

        assertThat(distance.getValue()).isEqualTo(16);
    }

    @DisplayName("상행 종점에서 하행 종점까지 정렬된 순서로 Section 리스트를 반환한다")
    @Test
    void getSections() {
        sections.insertSectionBetween(JAMSIL_NARU, JAMSIL, DISTANCE_10);
        sections.insertSectionBetween(JAMSIL_SAENAE, JAMSIL_NARU, DISTANCE_6);
        List<Station> stations = sections.getSections()
                .stream()
                .map(section -> section.getUpper())
                .collect(Collectors.toList());

        assertThat(stations).containsExactlyInAnyOrder(JAMSIL_SAENAE, JAMSIL_NARU);
    }
}
