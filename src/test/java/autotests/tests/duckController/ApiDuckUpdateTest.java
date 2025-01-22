package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class ApiDuckUpdateTest extends DuckActionsClient {

    @Test(description = "Проверка изменения цвета и высоты утки")
    @CitrusTest
    public void colorHeightUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        getNewDuckId(runner);
        duckUpdate(runner, "${duckId}", "red", 2, "rubber", "quack");
        validateResponse(runner, HttpStatus.OK, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"\n" + "}");
    }

    @Test(description = "Проверка изменения цвета и звука утки")
    @CitrusTest
    public void colorSoundUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        getNewDuckId(runner);
        duckUpdate(runner, "${duckId}", "green", 0.15, "rubber", "woof");
        validateResponse(runner, HttpStatus.OK, "{\n" + "  \"message\": \"Duck with id = ${duckId} is updated\"\n" + "}");
    }
}