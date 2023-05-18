package subway.domain.path;

import static fixtures.path.PathSectionDtoFixtures.INITIAL_PATH_SECTION_DTOS;
import static fixtures.path.PathSectionsFixtures.INITIAL_SHORTEST_PATH;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dto.PathSectionDto;

class PathSectionsTest {

    @Test
    @DisplayName("경로 구간으로 경로 구간 DTO를 생성하여 반환한다.")
    void toPathSectionDtos() {
        // given
        PathSections pathSections = INITIAL_SHORTEST_PATH.PATH_SECTIONS;

        // when
        List<PathSectionDto> pathSectionDtos = pathSections.toPathSectionDtos();

        // then
        assertThat(pathSectionDtos).usingRecursiveComparison().isEqualTo(INITIAL_PATH_SECTION_DTOS.DTOS);
    }

    @Test
    @DisplayName("경로 구간의 환승 횟수를 구한다.")
    void getTransferCount() {
        // given
        PathSections pathSections = INITIAL_SHORTEST_PATH.PATH_SECTIONS;

        // when
        int transferCount = pathSections.getTransferCount();

        // then
        assertThat(transferCount).isEqualTo(INITIAL_SHORTEST_PATH.TRANSFER_COUNT);
    }

    @Test
    @DisplayName("경로 구간의 총 거리를 구한다.")
    void getTotalDistance() {
        // given
        PathSections pathSections = INITIAL_SHORTEST_PATH.PATH_SECTIONS;

        // when
        int totalDistance = pathSections.getTotalDistance();

        // then
        assertThat(totalDistance).isEqualTo(INITIAL_SHORTEST_PATH.TOTAL_DISTANCE);
    }
}
