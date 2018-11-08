import { NgModule, Type, InjectionToken, ModuleWithProviders, Injector } from '@angular/core';
import { RxRpcClientModule, RxRpcTransport, RxRpcClient, RxRpcInvoker } from 'ng-rxrpc';
import { SampleEndpointClient } from './sample-endpoint-client';

const RXRPC_INVOKER = new InjectionToken<RxRpcInvoker>('RxRpcGeneratedClientModule.RxRpcInvoker');

@NgModule({
    imports: [ RxRpcClientModule ],
    providers: [
        SampleEndpointClient.provider(RXRPC_INVOKER)
    ]
})
export class RxRpcGeneratedClientModule {

    public static withTransport(transport: Type<RxRpcTransport>|InjectionToken<RxRpcTransport>): ModuleWithProviders<RxRpcGeneratedClientModule> {
        return {
            ngModule: RxRpcGeneratedClientModule,
            providers: [{
                provide: RXRPC_INVOKER,
                useFactory: injector => Injector
                    .create({
                        providers: [
                            {provide: RxRpcClient},
                            {provide: RxRpcTransport, useFactory: () => injector.get(transport)}],
                        parent: injector})
                    .get(RxRpcClient),
                deps: [Injector]
            }]
        };
    }
}
