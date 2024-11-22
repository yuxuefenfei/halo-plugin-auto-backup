package cn.wangwenzhu.autobackup;

import java.util.stream.IntStream;
import org.springdoc.core.fn.builders.apiresponse.Builder;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import run.halo.app.core.extension.endpoint.CustomEndpoint;
import run.halo.app.extension.GroupVersion;
import run.halo.app.extension.ListResult;

@Component
public class AutoBackupEndpoint implements CustomEndpoint {

    @Override
    public RouterFunction<ServerResponse> endpoint() {
        return SpringdocRouteBuilder.route()
            .GET("/autobackup/config/interval", this::interval,
                builder -> builder.operationId("interval mapping")
                    .description("autobackup operation")
                    .tag("AutoBackupV1alpha1Console")
                    .response(Builder.responseBuilder()
                        .implementation(ListResult.generateGenericClass(ServerResponse.class))
                    )
            )
            .build();
    }

    private Mono<ServerResponse> interval(ServerRequest request) {
        var list = IntStream.rangeClosed(1, 30)
            .mapToObj(value -> new IntervalVo(String.valueOf(value), value))
            .toList();

        return ServerResponse.ok()
            .bodyValue(new ListResult<>(1, list.size(), list.size(), list));
    }

    @Override
    public GroupVersion groupVersion() {
        return new GroupVersion("autobackup.wangwenzhu.cn", "v1alpha1");
    }
}
