package autotests.duckController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class ApiDuckUpdateTest extends TestNGCitrusSpringSupport {

    @Test(description = "Проверка изменения цвета и высоты утки")
    @CitrusTest
    public void colorHeightUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        runner.$(
                http().client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId")));
        duckUpdate(runner, "${duckId}", "red", 2, "rubber", "quack");
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"\n" + "}");
    }

    @Test(description = "Проверка изменения цвета и звука утки")
    @CitrusTest
    public void colorSoundUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        runner.$(
                http().client("http://localhost:2222")
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .extract(fromBody().expression("$.id", "duckId")));
        duckUpdate(runner, "${duckId}", "green", 0.15, "rubber", "woof");
        validateResponse(runner, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"\n" + "}");
    }

    public void duckUpdate(TestCaseRunner runner, String id, String color, double height, String material, String sound) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .put("/api/duck/update")
                .queryParam("color", color)
                .queryParam("height", String.valueOf(height))
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound));
    }

    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
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

}