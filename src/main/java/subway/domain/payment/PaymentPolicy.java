package subway.domain.payment;

import subway.domain.Lines;

public interface PaymentPolicy {

    int calculatePayment(final Lines route);
}
