package fixtures.path;

import static fixtures.path.PathStationFixtures.*;

import java.util.List;

import subway.domain.section.general.NearbyStations;
import subway.domain.section.transfer.TransferSection;

public class PathTransferSectionFixtures {

    public static class INITIAL_TRANSFER_SECTION_LINE_2_TO_3_C {

        public static final TransferSection FIND_TRANSFER_SECTION =
                new TransferSection(
                        NearbyStations.createByUpStationAndDownStation(INITIAL_STATION_LINE2_C.FIND_STATION, INITIAL_STATION_LINE3_C.FIND_STATION));
    }

    public static class INITIAL_TRANSFER_SECTION_LINE_2_TO_7_A {
        public static final TransferSection FIND_TRANSFER_SECTION =
                new TransferSection(
                        NearbyStations.createByUpStationAndDownStation(INITIAL_STATION_LINE2_A.FIND_STATION, INITIAL_STATION_LINE7_A.FIND_STATION));
    }

    public static class INITIAL_TRANSFER_SECTION_LINE_7_TO_3_D {

        public static final TransferSection FIND_TRANSFER_SECTION =
                new TransferSection(
                        NearbyStations.createByUpStationAndDownStation(INITIAL_STATION_LINE7_D.FIND_STATION, INITIAL_STATION_LINE3_D.FIND_STATION));
    }

    public static class DUMMY_TRANSFER_SECTION_LINE_7_TO_3_E {

        public static final TransferSection DUMMY =
                new TransferSection(NearbyStations.createByUpStationAndDownStation(DUMMY_STATION_LINE7_E.DUMMY, INITIAL_STATION_LINE3_E.FIND_STATION));
    }

    public static List<TransferSection> ALL_INITIAL_TRANSFER_SECTION =
            List.of(
                    INITIAL_TRANSFER_SECTION_LINE_2_TO_3_C.FIND_TRANSFER_SECTION,
                    INITIAL_TRANSFER_SECTION_LINE_2_TO_7_A.FIND_TRANSFER_SECTION,
                    INITIAL_TRANSFER_SECTION_LINE_7_TO_3_D.FIND_TRANSFER_SECTION
            );
}
