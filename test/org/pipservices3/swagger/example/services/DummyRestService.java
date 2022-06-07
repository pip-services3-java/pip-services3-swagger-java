package org.pipservices3.swagger.example.services;

import org.pipservices3.commons.errors.ApplicationException;
import org.pipservices3.swagger.example.data.Dummy;
import org.pipservices3.swagger.example.data.DummySchema;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import org.pipservices3.swagger.example.logic.IDummyController;
import org.glassfish.jersey.process.Inflector;
import org.pipservices3.commons.convert.JsonConverter;
import org.pipservices3.commons.convert.TypeCode;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;
import org.pipservices3.commons.errors.ConfigException;
import org.pipservices3.commons.refer.Descriptor;
import org.pipservices3.commons.refer.IReferences;
import org.pipservices3.commons.refer.ReferenceException;
import org.pipservices3.commons.validate.ObjectSchema;
import org.pipservices3.rpc.services.RestService;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class DummyRestService extends RestService {
    private IDummyController _controller;

    public DummyRestService() {
        this._dependencyResolver.put("controller", new Descriptor("pip-services-dummies", "controller", "default", "*", "*"));
    }

    @Override
    public void setReferences(IReferences references) throws ReferenceException, ConfigException {
        super.setReferences(references);
        this._controller = this._dependencyResolver.getOneRequired(IDummyController.class, "controller");
    }

    private Response getPageByFilter(ContainerRequestContext req) {
        try {
            var result = this._controller.getPageByFilter(
                    this.getCorrelationId(req),
                    new FilterParams(req.getUriInfo().getPathParameters()),
                    PagingParams.fromValue(req.getUriInfo().getPathParameters())
            );
            return this.sendResult(result);
        } catch (Exception ex) {
            return this.sendError(ex);
        }
    }

    private Response getOneById(ContainerRequestContext req) {
        try {
            var result = this._controller.getOneById(
                    this.getCorrelationId(req),
                    req.getUriInfo().getPathParameters().get("dummy_id").get(0)
            );
            return this.sendResult(result);
        } catch (Exception ex) {
            return this.sendError(ex);
        }
    }

    private Response create(ContainerRequestContext req) {
        try {
            var result = this._controller.create(
                    this.getCorrelationId(req),
                    JsonConverter.fromJson(
                            Dummy.class,
                            new String(req.getEntityStream().readAllBytes(), StandardCharsets.UTF_8)
                    )
            );
            return this.sendCreatedResult(result);
        } catch (Exception ex) {
            return this.sendError(ex);
        }
    }

    private Response update(ContainerRequestContext req) {
        try {
            var result = this._controller.update(
                    getCorrelationId(req),
                    JsonConverter.fromJson(
                            Dummy.class,
                            new String(req.getEntityStream().readAllBytes(), StandardCharsets.UTF_8)
                    )
            );
            return this.sendResult(result);
        } catch (Exception ex) {
            return this.sendError(ex);
        }
    }

    private Response deleteById(ContainerRequestContext req) {
        try {
            var result = this._controller.deleteById(
                    this.getCorrelationId(req),
                    req.getUriInfo().getPathParameters().get("dummy_id").get(0)
            );
            return this.sendDeletedResult(result);
        } catch (Exception ex) {
            return this.sendError(ex);
        }
    }

    @Override
    public void register() {
        this.registerRoute(
                "get", "/dummies",
                new ObjectSchema()
                        .withOptionalProperty("skip", TypeCode.String)
                        .withOptionalProperty("take", TypeCode.String)
                        .withOptionalProperty("total", TypeCode.String),
                this::getPageByFilter
        );

        this.registerRoute(
                "get", "/dummies/{dummy_id}",
                new ObjectSchema()
                        .withRequiredProperty("dummy_id", TypeCode.String),
                this::getOneById
        );

        this.registerRoute(
                "post", "/dummies",
                new ObjectSchema()
                        .withRequiredProperty("body", new DummySchema()),
                this::create
        );

        this.registerRoute(
                "put", "/dummies/{dummy_id}",
                new ObjectSchema()
                        .withRequiredProperty("body", new DummySchema()),
                this::update
        );

        this.registerRoute(
                "delete", "/dummies/{dummy_id}",
                new ObjectSchema()
                        .withRequiredProperty("dummy_id", TypeCode.String),
                this::deleteById
        );

        this._swaggerRoute = "/dummies/swagger";
        var dirname = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath();
        this.registerOpenApiSpecFromFile(dirname + "./org/pipservices3/swagger/example/services/dummy.yml");
    }
}
