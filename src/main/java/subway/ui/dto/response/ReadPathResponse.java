package subway.ui.dto.response;

import subway.domain.Price;
import subway.domain.Station;

import java.util.List;

public class ReadPathResponse {

    final List<Station> stations;
    final Price price;

    public ReadPathResponse(final List<Station> stations, final Price price) {
        this.stations = stations;
        this.price = price;
    }

    public static ReadPathResponse from(final List<Station> stations,
                                        final Price price) {
        return new ReadPathResponse(stations, price);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Price getPrice() {
        return price;
    }
}
