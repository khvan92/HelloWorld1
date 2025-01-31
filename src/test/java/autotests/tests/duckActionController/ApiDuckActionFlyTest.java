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

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

public class ApiDuckActionFlyTest extends DuckActionsClient {

    @Test(description = "Проверка action fly утки с активными крыльями")
    @CitrusTest
    public void flyWithActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duckRubber = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.ACTIVE);
        deleteDuckFinally(runner);
//        createDuck(runner, duckRubber);
//        getNewDuckId(runner);
        createDuckByDb(runner, "897976", duckRubber);
        validateDuckInDb(runner, "${duckId}", duckRubber);
        duckFly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK, "{\n" + "  \"message\": \"I'm flying\"\n" + "}");
    }

    @Test(description = "Проверка action fly утки со связанными крыльями")
    @CitrusTest
    public void flyWithFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duckRubber = new Duck()
                .color("yellow")
                .height(0.15)
                .material("rubber")
                .sound("quack")
                .wingsState(WingState.FIXED);

        deleteDuckFinally(runner);

//        createDuck(runner, duckRubber);
//        getNewDuckId(runner);

        createDuckByDb(runner, "65892", duckRubber);
        validateDuckInDb(runner, "${duckId}", duckRubber);
        duckFly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.OK,
                "{\n" + "  \"message\": \"I can't fly\"\n" + "}");
    }

    @Test(description = "Проверка action fly утки с крыльями в неопределенном состоянии")
    @CitrusTest
    public void flyWithUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duckRubber = new Duck()
                .color("yellow")
                .height(0.15)
                .material("rubber")
                .sound("quack")
                .wingsState(WingState.UNDEFINED);

        deleteDuckFinally(runner);

//        createDuck(runner, duckRubber);
//        getNewDuckId(runner);

        createDuckByDb(runner, "672389", duckRubber);
        validateDuckInDb(runner, "${duckId}", duckRubber);
        duckFly(runner, "${duckId}");
        validateResponse(runner, HttpStatus.INTERNAL_SERVER_ERROR,
                "{\n" + "  \"message\": \"Wings aren't detected\"\n" + "}");
    }
}