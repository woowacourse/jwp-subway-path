package subway.domain.payment;

import subway.domain.LinkedRoute;

public interface PaymentPolicy {

    int calculatePayment(final LinkedRoute route);
}
