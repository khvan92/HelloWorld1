package autotests.duckController;

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

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;


public class ApiDuckCreateTest extends TestNGCitrusSpringSupport {

    @Test(description = "Проверка создания утки с material = rubber")
    @CitrusTest
    public void createRubberDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        checkMaterial(runner, "rubber");
    }

    @Test(description = "Проверка создания утки с material = wood")
    @CitrusTest
    public void createWoodDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "wood", "quack", "ACTIVE");
        checkMaterial(runner, "wood");
    }

    public void validateResponse(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
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

        public void checkMaterial(TestCaseRunner runner, String material) {
        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.material", material)
        );
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