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

public class ApiDuckActionPropertiesTest extends TestNGCitrusSpringSupport {

    @Test(description = "Проверка action properties утки из материала wood и четным ID")
    @CitrusTest
    public void propertiesMaterialWood(@Optional @CitrusResource TestCaseRunner runner) {
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

        duckProperties(runner, "${duckId}");
        checkMaterial(runner, "wood");
    }

    @Test(description = "Проверка action properties утки из материала rubber и нечетным ID")
    @CitrusTest
    public void propertiesMaterialRubber(@Optional @CitrusResource TestCaseRunner runner) {
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

        duckProperties(runner, "${duckId}");
        checkMaterial(runner, "rubber");
    }


    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    public void checkMaterial(TestCaseRunner runner, String material) {
        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.material", material)
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