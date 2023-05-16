package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Station;

public class IntegrationFixture {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String jsonSerialize(final Object request) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(request);
    }

    public static final Line LINE1 = new Line(1L, new LineName("1"));
    public static final Line LINE2 = new Line(2L, new LineName("2"));
    public static final Line LINE3 = new Line(3L, new LineName("3"));

    public static final Station STATION_A = new Station(1L, "A");
    public static final Station STATION_B = new Station(2L, "B");
    public static final Station STATION_C = new Station(3L, "C");
    public static final Station STATION_D = new Station(4L, "D");
    public static final Station STATION_E = new Station(5L, "E");
    public static final Station STATION_F = new Station(6L, "F");

    public static final Distance DISTANCE1 = new Distance(1);
    public static final Distance DISTANCE2 = new Distance(2);
    public static final Distance DISTANCE3 = new Distance(3);
    public static final Distance DISTANCE4 = new Distance(4);
    public static final Distance DISTANCE5 = new Distance(5);
    public static final Distance DISTANCE6 = new Distance(6);
    public static final Distance DISTANCE7 = new Distance(7);
    public static final Distance DISTANCE8 = new Distance(8);
    public static final Distance DISTANCE9 = new Distance(9);
    public static final Distance DISTANCE10 = new Distance(10);
}
