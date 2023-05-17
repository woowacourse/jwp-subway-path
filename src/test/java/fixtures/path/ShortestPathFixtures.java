package fixtures.path;

import static fixtures.path.PathGeneralSectionFixtures.INITIAL_GENERAL_SECTION_LINE3_D_TO_E;
import static fixtures.path.PathGeneralSectionFixtures.INITIAL_GENERAL_SECTION_LINE7_A_TO_D;
import static fixtures.path.PathTransferSectionFixtures.INITIAL_TRANSFER_SECTION_LINE_2_TO_7_A;
import static fixtures.path.PathTransferSectionFixtures.INITIAL_TRANSFER_SECTION_LINE_7_TO_3_D;

import java.util.List;

import subway.domain.path.PathSections;

public class ShortestPathFixtures {

    public static final PathSections INITIAL_SHORTEST_PATH =
            new PathSections(
                    List.of(
                            INITIAL_TRANSFER_SECTION_LINE_2_TO_7_A.FIND_TRANSFER_SECTION,
                            INITIAL_GENERAL_SECTION_LINE7_A_TO_D.FIND_SECTION,
                            INITIAL_TRANSFER_SECTION_LINE_7_TO_3_D.FIND_TRANSFER_SECTION,
                            INITIAL_GENERAL_SECTION_LINE3_D_TO_E.FIND_SECTION
                    )
            );
}
