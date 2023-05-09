package subway.integration;

import subway.dto.LineNodeRequest;

public class IntegrationFixture {

    public static final LineNodeRequest A_NODE = new LineNodeRequest(null, "a", 4);
    public static final LineNodeRequest B_NODE = new LineNodeRequest("a", "b", 3);
    public static final LineNodeRequest C_NODE = new LineNodeRequest("b", "c", null);

}
