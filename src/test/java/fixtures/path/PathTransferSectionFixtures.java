package fixtures.path;

import static fixtures.path.PathStationFixtures.*;

import subway.domain.section.TransferSection;

public class PathTransferSectionFixtures {

    public static class INITIAL_TRANSFER_SECTION_LINE_2_TO_3_C {
        public static final Long ID = 1L;

        public static final TransferSection FIND_TRANSFER_SECTION =
                new TransferSection(ID, INITIAL_STATION_LINE2_C.FIND_STATION, INITIAL_STATION_LINE3_C.FIND_STATION);
    }

    public static class INITIAL_TRANSFER_SECTION_LINE_2_TO_7_A {
        public static final Long ID = 2L;

        public static final TransferSection FIND_TRANSFER_SECTION =
                new TransferSection(ID, INITIAL_STATION_LINE2_A.FIND_STATION, INITIAL_STATION_LINE7_A.FIND_STATION);
    }

    public static class INITIAL_TRANSFER_SECTION_LINE_7_TO_3_D {

        public static final Long ID = 3L;
        public static final TransferSection FIND_TRANSFER_SECTION =
                new TransferSection(ID, INITIAL_STATION_LINE7_D.FIND_STATION, INITIAL_STATION_LINE3_D.FIND_STATION);
    }

    public static class DUMMY_TRANSFER_SECTION_LINE_7_TO_3_E {

        public static final Long ID = -1L;
        public static final TransferSection DUMMY =
                new TransferSection(ID, DUMMY_STATION_LINE7_E.DUMMY, INITIAL_STATION_LINE3_E.FIND_STATION);
    }
}
