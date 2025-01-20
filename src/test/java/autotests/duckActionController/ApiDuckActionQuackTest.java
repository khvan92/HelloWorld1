package autotests.duckActionController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.validation.json.JsonPathMessageValidationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class ApiDuckActionQuackTest extends TestNGCitrusSpringSupport {

    @Test(description = "Проверка action quack утки с корректным звуком и нечетным ID")
    @CitrusTest
    public void quackWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicReference<Integer> id = new AtomicReference<>(0);

        createDuck(runner, "yellow", 0.15, "wood", "quack", "ACTIVE");
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));

        runner.$(action ->
        {id.set(Integer.parseInt(action.getVariable("duckId")));
        });

        if (id.get()%2==0) {
            createDuck(runner, "yellow", 0.15, "wood", "quack", "ACTIVE");
            runner.$(http().client("http://localhost:2222")
                    .receive()
                    .response(HttpStatus.OK)
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
        }

        duckQuack(runner, "${duckId}", 1, 1);
        checkSound(runner, "quack");
    }

    @Test(description = "Проверка action quack утки с корректным звуком и четным ID")
    @CitrusTest
    public void quackWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        AtomicReference<Integer> id = new AtomicReference<>(0);

        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));

        runner.$(action ->
        {id.set(Integer.parseInt(action.getVariable("duckId")));
        });

        if (id.get()%2!=0) {
            createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
            runner.$(http().client("http://localhost:2222")
                    .receive()
                    .response(HttpStatus.OK)
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
        }

        duckQuack(runner, "${duckId}", 1, 1);
        checkSound(runner, "quack");
    }

    public void duckQuack(TestCaseRunner runner, String id, int repetitionCount, int soundCount) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", String.valueOf(repetitionCount))
                .queryParam("soundCount", String.valueOf(soundCount)));
    }

    public void checkSound(TestCaseRunner runner, String sound) {
        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.sound", sound)
        );
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" + "  \"color\": \"" + color + "\",\n"
                        + "  \"height\": " + height + ",\n"
                        + "  \"material\": \"" + material + "\",\n"
                        + "  \"sound\": \"" + sound + "\",\n"
                        + "  \"wingsState\": \"" + wingsState
                        + "\"\n" + "}"));
    }

    public void validateResponseJsonPath(TestCaseRunner runner,
                                         JsonPathMessageValidationContext.Builder body) {
        runner.$(
                http()
                        .client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK )
                        .message()
                        .type(MessageType.JSON )
                        .validate(body));
    }

}