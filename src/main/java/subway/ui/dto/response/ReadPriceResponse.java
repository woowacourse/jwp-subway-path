package subway.ui.dto.response;

import subway.domain.Price;

public class ReadPriceResponse {

    private final int price;

    public ReadPriceResponse(final int price) {
        this.price = price;
    }

    public static ReadPriceResponse from(final Price price) {
        return new ReadPriceResponse(price.getPrice());
    }

    public int getPrice() {
        return price;
    }
}
