package org.pipservices3.swagger.example.services;

import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.rpc.services.CommandableHttpService;

public class DummyCommandableHttpService extends CommandableHttpService {

    /**
     * Creates a new instance of the service.
     */
    public DummyCommandableHttpService() {
        super("dummies2");
        this._dependencyResolver.put("controller", new Descriptor("pip-services-dummies", "controller", "default", "*", "*"));
    }

    @Override
    public void register() {
        // if (!this._swaggerAuto && this._swaggerEnabled) {
        //     this.registerOpenApiSpec("swagger yaml content");
        // }

        super.register();
    }
}
