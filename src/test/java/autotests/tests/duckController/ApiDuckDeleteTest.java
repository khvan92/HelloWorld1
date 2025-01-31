package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
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
        Duck duckRubber = new Duck()
                .color("yellow")
                .height(0.15)
                .material("rubber")
                .sound("quack")
                .wingsState(WingState.ACTIVE);

//        createDuck(runner, duckRubber);
//        getNewDuckId(runner);

        createDuckByDb(runner, "285123", duckRubber);
        duckDelete(runner, "${duckId}");
        validateResponseFromResources(runner, HttpStatus.OK,
                "duckActionsTest/successfulDelete.json");
        validateDeletedDuckInDb(runner, "${duckId}");

//        duckGetAllIds(runner);
//        checkDeletedId(runner);
    }
}