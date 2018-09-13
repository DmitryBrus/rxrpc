package com.slimgears.rxrpc.sample;

import com.slimgears.rxrpc.client.AbstractClient;
import com.slimgears.rxrpc.client.RxClient.Session;
import com.slimgears.util.reflect.TypeToken;

import java.lang.Integer;
import javax.annotation.Generated;



/**
 * Generated from com.slimgears.rxrpc.sample.SampleDefaultNameEndpoint
 */
@Generated("com.slimgears.rxrpc.apt.RxRpcEndpointAnnotationProcessor")
public class SampleDefaultNameEndpoint_RxClient extends AbstractClient implements SampleDefaultNameEndpoint {
    public SampleDefaultNameEndpoint_RxClient(Session session) {
        super(session);
    }

    @Override
    public int method() {
        return invokeBlocking(
                TypeToken.of(Integer.class),
                "sample-default-name-endpoint/method",
                arguments()
        );
    }

}
