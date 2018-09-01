package com.slimgears.rxrpc.apt.typescript; /**
 *
 */

import com.slimgears.apt.util.AnnotationProcessingTester;
import com.slimgears.rxrpc.apt.TestBundles;
import org.junit.Test;
import org.slf4j.event.Level;

public class TypeScriptEndpointGenerationTest {
    @Test
    public void testEndpointClientGeneration() {
        TestBundles.sampleEndpointTester()
                .apply(this::typeScriptOptions)
                .expectedFiles(
                        "sample-array-endpoint.ts",
                        "sample-base-endpoint.ts",
                        "sample-endpoint.ts",
                        "sample-endpoint-client.ts",
                        "sample-request.ts",
                        "sample-data.ts",
                        "sample-enum.ts",
                        "sample-array.ts",
//                        "index.ts",
//                        "module.ts",
                        "tsconfig.json")
                .test();
    }

    @Test
    public void testSpecializedClientGeneration() {
        TestBundles.sampleSpecializedEndpointTester()
                .apply(this::typeScriptOptions)
                .expectedFiles(
                        "sample-generic-data.ts",
                        "sample-generic-endpoint.ts",
                        "sample-specialized-endpoint.ts",
                        "sample-specialized-endpoint-client.ts")
                .test();
    }

    @Test
    public void testOptionalDataGeneration() {
        TestBundles.sampleOptionalDataTester()
                .apply(this::typeScriptOptions)
                .expectedFiles("sample-optional-data.ts")
                .test();
    }

    @Test
    public void testNestedDataGeneration() {
        TestBundles.sampleNestedDataEndpointTester()
                .apply(this::typeScriptOptions)
                .expectedFiles(
                        "sample-nested-data-endpoint.ts",
                        "sample-nested-data-endpoint-data.ts",
                        "sample-nested-data-endpoint-data-type.ts",
                        "sample-nested-data-endpoint-client.ts")
                .test();
    }

    @Test
    public void testDerivedAndBaseDataGeneration() {
        TestBundles.sampleDerivedDataEndpointTester()
                .apply(this::typeScriptOptions)
                .expectedFiles(
                        "sample-base-data.ts",
                        "sample-derived-data.ts")
                .test();
    }

    @Test
    public void testDefaultNameEndpointGeneration() {
        TestBundles.sampleDefaultNameEndpointTester()
                .apply(this::typeScriptOptions)
                .expectedFiles("sample-default-name-endpoint-client.ts")
                .test();
    }

    private AnnotationProcessingTester typeScriptOptions(AnnotationProcessingTester tester) {
        return tester
                //.verbosity(Level.TRACE)
                .options(
                        "-Arxrpc.ts.ngmodule",
                        "-Arxrpc.ts.npm");
    }
}
