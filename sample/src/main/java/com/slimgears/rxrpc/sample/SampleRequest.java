/**
 *
 */
package com.slimgears.rxrpc.sample;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slimgears.rxrpc.core.RxRpcData;

@RxRpcData
public class SampleRequest {
    @JsonProperty public final int id;
    @JsonProperty public final String name;

    public SampleRequest(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
