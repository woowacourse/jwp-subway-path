package subway.fixture;

import subway.domain.Sections;

import java.util.ArrayList;
import java.util.List;

import static subway.fixture.SectionFixture.*;

public class SectionsFixture {
    public static final Sections SECTIONS =
            new Sections(new ArrayList<>(List.of(SECTION_1, SECTION_2, SECTION_3, SECTION_4,
                    SECTION_5, SECTION_6, SECTION_7, SECTION_8, SECTION_9, SECTION_10, SECTION_11,
                    SECTION_12, SECTION_13, SECTION_14, SECTION_15, SECTION_16, SECTION_17, SECTION_18, SECTION_19)));
}
