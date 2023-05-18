package subway.domain.station;

import static fixtures.path.PathStationFixtures.ALL_INITIAL_STATION;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import fixtures.path.PathTransferSectionFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.transfer.TransferSection;

class StationsTest {

    @Test
    @DisplayName("환승 구간 리스트를 반환한다.")
    void getTransferSections() {
        // given
        Stations stations = new Stations(ALL_INITIAL_STATION);

        // when
        List<TransferSection> transferSections = stations.getTransferSections();

        // then
        assertThat(transferSections).usingRecursiveFieldByFieldElementComparator().contains(
                PathTransferSectionFixtures.INITIAL_TRANSFER_SECTION_LINE_2_TO_3_C.FIND_TRANSFER_SECTION,
                PathTransferSectionFixtures.INITIAL_TRANSFER_SECTION_LINE_2_TO_7_A.FIND_TRANSFER_SECTION,
                PathTransferSectionFixtures.INITIAL_TRANSFER_SECTION_LINE_7_TO_3_D.FIND_TRANSFER_SECTION);
    }
}
