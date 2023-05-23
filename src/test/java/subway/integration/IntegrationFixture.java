package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import subway.domain.line.Line;
import subway.domain.line.LineName;
import subway.domain.section.Distance;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationName;

import java.util.ArrayList;

public class IntegrationFixture {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final Line LINE1 = new Line(1L, new LineName("1"), new Sections(new ArrayList<>()));
    public static final Line LINE2 = new Line(2L, new LineName("2"), new Sections(new ArrayList<>()));
    public static final Line LINE3 = new Line(3L, new LineName("3"), new Sections(new ArrayList<>()));
    public static final Station STATION_A = new Station(1L, new StationName("A"));
    public static final Station STATION_B = new Station(2L, new StationName("B"));
    public static final Station STATION_C = new Station(3L, new StationName("C"));
    public static final Station STATION_D = new Station(4L, new StationName("D"));
    public static final Station STATION_E = new Station(5L, new StationName("E"));
    public static final Station STATION_F = new Station(6L, new StationName("F"));
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

    public static String jsonSerialize(final Object request) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(request);
    }
}
