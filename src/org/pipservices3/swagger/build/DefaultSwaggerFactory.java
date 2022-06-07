package org.pipservices3.swagger.build;

import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.components.build.Factory;
import org.pipservices3.swagger.services.SwaggerService;

/**
 * Creates Swagger components by their descriptors.
 *
 * @see org.pipservices3.components.build.Factory
 * @see org.pipservices3.rpc.services.HeartbeatRestService
 * @see org.pipservices3.rpc.services.HttpEndpoint
 * @see org.pipservices3.rpc.services.StatusRestService
 */
public class DefaultSwaggerFactory extends Factory {
    private static final Descriptor SwaggerServiceDescriptor = new Descriptor("pip-services", "swagger-service", "*", "*", "1.0");

    public DefaultSwaggerFactory() {
        this.registerAsType(DefaultSwaggerFactory.SwaggerServiceDescriptor, SwaggerService.class);
    }
}
