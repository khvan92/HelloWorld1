package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class ApiDuckActionSwimTest extends DuckActionsClient {

    @Test(description = "Проверка action swim утки с существующим id")
    @CitrusTest
    public void successfulSwimWithRightId(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duckRubber = new Duck()
                .color("yellow")
                .height(0.15)
                .material("rubber")
                .sound("quack")
                .wingsState(WingState.ACTIVE);

        deleteDuckFinally(runner);

//        createDuck(runner, duckRubber);
//        getNewDuckId(runner);

        createDuckByDb(runner, "87659835", duckRubber);
        validateDuckInDb(runner, "${duckId}", duckRubber);

        duckSwim(runner, "${duckId}");
        validateResponseFromResources(runner, HttpStatus.OK,
                "duckActionsTest/successfulSwim.json");
    }

    @Test(description = "Проверка action swim утки с несуществующим id")
    @CitrusTest
    public void successfulSwimWithWrongId(@Optional @CitrusResource TestCaseRunner runner) {
//        Duck duckRubber = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.FIXED);
//        createDuck(runner, duckRubber);
//        validateResponse(runner, HttpStatus.OK);

        duckSwim(runner, "0");
        validateResponseFromResources(runner, HttpStatus.NOT_FOUND,
                "duckActionsTest/pawsNotFound.json");
    }
}