package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class ApiDuckActionSwimTest extends DuckActionsClient {

    @Test(description = "Проверка action swim утки с существующим id")
    @CitrusTest
    public void successfulSwimWithRightId(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        getNewDuckId(runner);
        duckSwim(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "{\n" + "  \"message\": \"I'm swimming\"\n" + "}");
    }

    @Test(description = "Проверка action swim утки с несуществующим id")
    @CitrusTest
    public void successfulSwimWithWrongId(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        validateResponse(runner, HttpStatus.OK);
        duckSwim(runner, "0");
        validateResponse(runner, HttpStatus.NOT_FOUND, "{\n" + "  \"message\": \"Paws are not found ((((\"\n" + "}");
    }

    public void validateResponse(TestCaseRunner runner, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}