package subway.path.domain;

public interface PaymentPolicy {

    int calculateFee(final Path path);
}
