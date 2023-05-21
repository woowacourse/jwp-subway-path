package fixtures.path;

import static fixtures.path.PathGeneralSectionFixtures.INITIAL_GENERAL_SECTION_LINE3_D_TO_E;
import static fixtures.path.PathGeneralSectionFixtures.INITIAL_GENERAL_SECTION_LINE7_A_TO_D;
import static fixtures.path.PathTransferSectionFixtures.INITIAL_TRANSFER_SECTION_LINE_2_TO_7_A;
import static fixtures.path.PathTransferSectionFixtures.INITIAL_TRANSFER_SECTION_LINE_7_TO_3_D;

import java.util.List;

import subway.domain.path.PathSections;

public class PathSectionsFixtures {

    public static class INITIAL_SHORTEST_PATH {

        public static final int TRANSFER_COUNT = 2;
        public static final int TOTAL_DISTANCE = 17;


        public static final PathSections PATH_SECTIONS =
                new PathSections(
                        List.of(
                                INITIAL_TRANSFER_SECTION_LINE_2_TO_7_A.FIND_TRANSFER_SECTION,
                                INITIAL_GENERAL_SECTION_LINE7_A_TO_D.FIND_SECTION,
                                INITIAL_TRANSFER_SECTION_LINE_7_TO_3_D.FIND_TRANSFER_SECTION,
                                INITIAL_GENERAL_SECTION_LINE3_D_TO_E.FIND_SECTION
                        )
                );
    }
}
