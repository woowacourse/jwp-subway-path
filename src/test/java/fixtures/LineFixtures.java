package fixtures;

import static fixtures.StationFixtures.*;

import java.util.List;

import subway.domain.line.Line;
import subway.dto.LineFindResponse;

public class LineFixtures {

    public static class INITIAL_Line2 {
        public static final Long ID = 1L;
        public static final String NAME = "2호선";
        public static final Line FIND_LINE = new Line(ID, NAME);
    }

    public static class INITIAL_Line7 {
        public static final Long ID = 2L;
        public static final String NAME = "7호선";
        public static final Line FIND_LINE = new Line(ID, NAME);
    }

    public static class Line3 {

        public static final String NAME = "3호선";
        public static final Line INSERT_LINE = new Line(null, NAME);
    }

    /**
     * Response
     */
    public static class LINE_FIND_RESPONSE_LINE2_A_TO_C_AND_C_TO_E {

        public static final LineFindResponse RESPONSE =
                new LineFindResponse(
                        INITIAL_Line2.NAME,
                        List.of(INITIAL_STATION_A.NAME, INITIAL_STATION_C.NAME, STATION_E.NAME)
                );
    }

    public static class LINE_FIND_RESPONSE_LINE7_B_TO_D {

        public static final LineFindResponse RESPONSE =
                new LineFindResponse(
                        INITIAL_Line7.NAME,
                        List.of(STATION_B.NAME, STATION_D.NAME)
                );
    }

    public static class ALL_LINE_FIND_RESPONSE_LINE2_AND_LINE7 {
        public static final List<LineFindResponse> RESPONSE =
                List.of(
                        LINE_FIND_RESPONSE_LINE2_A_TO_C_AND_C_TO_E.RESPONSE,
                        LINE_FIND_RESPONSE_LINE7_B_TO_D.RESPONSE
                );
    }
}
