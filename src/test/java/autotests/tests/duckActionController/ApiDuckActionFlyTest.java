package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class ApiDuckActionFlyTest extends DuckActionsClient {

    @Test(description = "Проверка action fly утки с активными крыльями")
    @CitrusTest
    public void flyWithActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
        getNewDuckId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "{\n" + "  \"message\": \"I am flying :)\"\n" + "}");
    }

    @Test(description = "Проверка action fly утки со связанными крыльями")
    @CitrusTest
    public void flyWithFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "FIXED");
        getNewDuckId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "{\n" + "  \"message\": \"I can not fly :C\"\n" + "}");
    }

    @Test(description = "Проверка action fly утки с крыльями в неопределенном состоянии")
    @CitrusTest
    public void flyWithUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 0.15, "rubber", "quack", "UNDEFINED");
        getNewDuckId(runner);
        duckFly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "{\n" + "  \"message\": \"Wings are not detected :(\"\n" + "}");
    }
}