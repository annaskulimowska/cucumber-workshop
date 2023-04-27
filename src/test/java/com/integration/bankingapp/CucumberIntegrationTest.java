package com.integration.bankingapp;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.integration.bankingapp")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "classpath:features")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberIntegrationTest {

    public final static WireMockServer WIRE_MOCK_SERVER =
            new WireMockServer(WireMockConfiguration.options().port(2345));

    public final static CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

    @BeforeAll
    public static void setUp() {
        System.out.println("--- Start WireMock ---");
        WIRE_MOCK_SERVER.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        HTTP_CLIENT.close();
        WIRE_MOCK_SERVER.stop();
        System.out.println("--- Stop WireMock ---");
    }
}