package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class ApiDuckDeleteTest extends DuckActionsClient {

    @Test(description = "Проверка удаления утки")
    @CitrusTest
    public void successfulDelete(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        getNewDuckId(runner);
        duckDelete(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "{\n" + "  \"message\": \"Duck is deleted\"\n" + "}");
    }
}