package org.pipservices3.swagger.example.logic;

import org.pipservices3.commons.convert.JsonConverter;
import org.pipservices3.commons.convert.TypeConverter;
import org.pipservices3.swagger.example.data.Dummy;
import org.pipservices3.swagger.example.data.DummySchema;
import org.pipservices3.commons.commands.Command;
import org.pipservices3.commons.commands.CommandSet;
import org.pipservices3.commons.commands.ICommand;
import org.pipservices3.commons.convert.TypeCode;
import org.pipservices3.commons.data.FilterParams;
import org.pipservices3.commons.data.PagingParams;
import org.pipservices3.commons.run.Parameters;
import org.pipservices3.commons.validate.FilterParamsSchema;
import org.pipservices3.commons.validate.ObjectSchema;
import org.pipservices3.commons.validate.PagingParamsSchema;

import java.io.IOException;

public class DummyCommandSet extends CommandSet {
    private final IDummyController _controller;

    public DummyCommandSet(IDummyController controller) {
        this._controller = controller;

        this.addCommand(this.makeGetPageByFilterCommand());
        this.addCommand(this.makeGetOneByIdCommand());
        this.addCommand(this.makeCreateCommand());
        this.addCommand(this.makeUpdateCommand());
        this.addCommand(this.makeDeleteByIdCommand());
    }

    private ICommand makeGetPageByFilterCommand() {
        return new Command(
                "get_dummies",
                new ObjectSchema()
                        .withOptionalProperty("filter", new FilterParamsSchema())
                        .withOptionalProperty("paging", new PagingParamsSchema()),
                (String correlationId, Parameters args) -> {
                    var filter = FilterParams.fromValue(args.get("filter"));
                    var paging = PagingParams.fromValue(args.get("paging"));
                    return this._controller.getPageByFilter(correlationId, filter, paging);
                }
        );
    }

    private ICommand makeGetOneByIdCommand() {
        return new Command(
                "get_dummy_by_id",
                new ObjectSchema()
                        .withRequiredProperty("dummy_id", TypeCode.String),
                (String correlationId, Parameters args) -> {
                    var id = args.getAsString("dummy_id");
                    return this._controller.getOneById(correlationId, id);
                }
        );
    }

    private ICommand makeCreateCommand() {
        return new Command(
                "create_dummy",
                new ObjectSchema()
                        .withRequiredProperty("dummy", new DummySchema()),
                (String correlationId, Parameters args) -> {
                    Dummy entity;
                    try {
                        entity = JsonConverter.fromJson(Dummy.class, JsonConverter.toJson(args.get("dummy")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return this._controller.create(correlationId, entity);
                }
        );
    }

    private ICommand makeUpdateCommand() {
        return new Command(
                "update_dummy",
                new ObjectSchema()
                        .withRequiredProperty("dummy", new DummySchema()),
                (String correlationId, Parameters args) -> {
                    Dummy entity;
                    try {
                        entity = JsonConverter.fromJson(Dummy.class, JsonConverter.toJson(args.get("dummy")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return this._controller.update(correlationId, entity);
                }
        );
    }

    private ICommand makeDeleteByIdCommand() {
        return new Command(
                "delete_dummy",
                new ObjectSchema()
                        .withRequiredProperty("dummy_id", TypeCode.String),
                (String correlationId, Parameters args) -> {
                    var id = args.getAsString("dummy_id");
                    return this._controller.deleteById(correlationId, id);
                }
        );
    }
}
