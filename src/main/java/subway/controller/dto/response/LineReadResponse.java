package subway.controller.dto.response;


import java.util.List;

/*
{
    "id": 1,
    "name": "2호선",
    "color": "bg-green-600",
    "stations": [
        {
            "id": 1,
            "name": "잠실"
        },
        {
            "id": 2,
            "name": "잠실새내"
        },
        {
            "id": 3,
            "name": "종합운동장"
        }
    ]
}
 */
public class LineReadResponse {

    private final long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineReadResponse(final long id,
                            final String name,
                            final String color,
                            final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

}
