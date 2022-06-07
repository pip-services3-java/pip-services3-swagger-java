package org.pipservices3.swagger.example;

import org.pipservices3.swagger.example.services.DummyRestService;
import org.pipservices3.commons.config.ConfigParams;
import org.pipservices3.commons.errors.ConfigException;
import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.commons.refer.Referencer;
import org.pipservices3.commons.refer.References;
import org.pipservices3.commons.run.Closer;
import org.pipservices3.commons.run.Opener;
import org.pipservices3.components.count.LogCounters;
import org.pipservices3.components.log.ConsoleLogger;
import org.pipservices3.rpc.services.HeartbeatRestService;
import org.pipservices3.rpc.services.HttpEndpoint;
import org.pipservices3.rpc.services.StatusRestService;
import org.pipservices3.swagger.services.SwaggerService;

import org.pipservices3.swagger.example.services.DummyCommandableHttpService;
import org.pipservices3.swagger.example.logic.DummyController;

import java.util.List;
import java.util.concurrent.Semaphore;

public class ExampleSwagger {
    public static void main(String[] args) throws ConfigException {
        // Create components
        var logger = new ConsoleLogger();
        var controller = new DummyController();
        var httpEndpoint = new HttpEndpoint();
        var restService = new DummyRestService();
        var httpService = new DummyCommandableHttpService();
        var statusService = new StatusRestService();
        var heartbeatService = new HeartbeatRestService();
        var swaggerService = new SwaggerService();

        var components = List.of(
                controller,
                httpEndpoint,
                restService,
                httpService,
                statusService,
                heartbeatService,
                swaggerService,
                logger
        );

        // Configure components
        logger.configure(ConfigParams.fromTuples(
                "level", "trace"
        ));

        httpEndpoint.configure(ConfigParams.fromTuples(
                "connection.protocol", "http",
                "connection.host", "localhost",
                "connection.port", 8080
        ));

        restService.configure(ConfigParams.fromTuples(
                "swagger.enable", true
        ));

        httpService.configure(ConfigParams.fromTuples(
                "base_route", "dummies2",
                "swagger.enable", true
        ));

        try {
            // Set references
            var references = References.fromTuples(
                    new Descriptor("pip-services", "logger", "console", "default", "1.0"), logger,
                    new Descriptor("pip-services", "counters", "log", "default", "1.0"), new LogCounters(),
                    new Descriptor("pip-services", "endpoint", "http", "default", "1.0"), httpEndpoint,
                    new Descriptor("pip-services-dummies", "controller", "default", "default", "1.0"), controller,
//                    new Descriptor("pip-services-dummies", "service", "rest", "default", "1.0"), restService,
                    new Descriptor("pip-services-dummies", "service", "commandable-http", "default", "1.0"), httpService,
                    new Descriptor("pip-services", "status-service", "rest", "default", "1.0"), statusService,
                    new Descriptor("pip-services", "heartbeat-service", "rest", "default", "1.0"), heartbeatService,
                    new Descriptor("pip-services", "swagger-service", "http", "default", "1.0"), swaggerService
            );

            Referencer.setReferences(references, components);

            Opener.open(null, components);

            System.out.println("Press Ctrl-C twice to stop the microservice...");

            // Signal handler
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Goodbye!");

                _exitEvent.release();

                // Runtime.getRuntime().exit(1);
            }));

            // Wait and close
            try {
                _exitEvent.acquire();
            } catch (InterruptedException ex) {
                // Ignore...
            }

            Closer.close(null, components);
            System.exit(0);
        } catch (Exception ex) {
            logger.error(null, ex, "Failed to execute the microservice");
            System.exit(1);
        }

    }

    private static final Semaphore _exitEvent = new Semaphore(0);
}
