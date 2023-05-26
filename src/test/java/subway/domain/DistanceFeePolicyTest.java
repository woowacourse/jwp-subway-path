package subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.service.path.domain.DistanceFeePolicy;
import subway.service.path.domain.SectionEdge;
import subway.service.section.domain.Distance;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class DistanceFeePolicyTest {
    @ParameterizedTest
    @CsvSource(value = {"1,2,1", "3,4,2"})
    void 기본요금_거리(int distance1, int distance2, int distance3) {
        SectionEdge sectionEdge = new SectionEdge(new Distance(distance1));
        SectionEdge sectionEdge1 = new SectionEdge(new Distance(distance2));
        SectionEdge sectionEdge2 = new SectionEdge(new Distance(distance3));

        DistanceFeePolicy distanceFeePolicy = new DistanceFeePolicy();
        int fee = distanceFeePolicy.calculateFee(List.of(sectionEdge, sectionEdge1, sectionEdge2));

        assertThat(fee).isEqualTo(1250);
    }

    @ParameterizedTest
    @CsvSource(value = {"10,2,1,1350", "20,20,10, 2050", "10,10,15,1750"})
    void 기본요금_초과_50km_이하(int distance1, int distance2, int distance3, int finalFee) {
        SectionEdge sectionEdge = new SectionEdge(new Distance(distance1));
        SectionEdge sectionEdge1 = new SectionEdge(new Distance(distance2));
        SectionEdge sectionEdge2 = new SectionEdge(new Distance(distance3));

        DistanceFeePolicy distanceFeePolicy = new DistanceFeePolicy();
        int fee = distanceFeePolicy.calculateFee(List.of(sectionEdge, sectionEdge1, sectionEdge2));
        assertThat(fee).isEqualTo(finalFee);
    }

    @ParameterizedTest
    @CsvSource(value = {"30,10,11,2150", "30,10,26, 2250", "30,10,36,2450"})
    void _50km_초과(int distance1, int distance2, int distance3, int finalFee) {
        SectionEdge sectionEdge = new SectionEdge(new Distance(distance1));
        SectionEdge sectionEdge1 = new SectionEdge(new Distance(distance2));
        SectionEdge sectionEdge2 = new SectionEdge(new Distance(distance3));

        DistanceFeePolicy distanceFeePolicy = new DistanceFeePolicy();
        int fee = distanceFeePolicy.calculateFee(List.of(sectionEdge, sectionEdge1, sectionEdge2));
        assertThat(fee).isEqualTo(finalFee);
    }
}
