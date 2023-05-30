package subway.ui.dto.response;

import subway.domain.price.Price;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class ReadPathResponse {

    final List<ReadStationResponse> stations;
    final int price;

    public ReadPathResponse(final List<ReadStationResponse> stations, final int price) {
        this.stations = stations;
        this.price = price;
    }

    public static ReadPathResponse from(final List<Station> stations,
                                        final Price price) {
        final List<ReadStationResponse> stationResponses = stations.stream()
                .map(ReadStationResponse::from)
                .collect(Collectors.toList());

        final int priceResponse = price.getPrice();

        return new ReadPathResponse(stationResponses, priceResponse);
    }

    public List<ReadStationResponse> getStations() {
        return stations;
    }

    public int getPrice() {
        return price;
    }
}
