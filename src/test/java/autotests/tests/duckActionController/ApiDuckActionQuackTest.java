package autotests.tests.duckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.QuackResponse;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class ApiDuckActionQuackTest extends DuckActionsClient {

    @Test(description = "Проверка action quack утки с корректным звуком и нечетным ID")
    @CitrusTest
    public void quackWithOddId(@Optional @CitrusResource TestCaseRunner runner) {
        QuackResponse quackResponse = new QuackResponse().sound("quack");

        Duck duckRubber = new Duck()
                .color("yellow")
                .height(0.15)
                .material("rubber")
                .sound("quack")
                .wingsState(WingState.ACTIVE);

        deleteDuckFinally(runner);

//        createDuckWithOddId(runner, "yellow", 0.15, "wood", "quack", WingState.ACTIVE);

        createDuckByDb(runner, "576555", duckRubber);
        validateDuckInDb(runner, "${duckId}", duckRubber);

        duckQuack(runner, "${duckId}", 1, 1);
        validateResponse(runner, HttpStatus.OK, quackResponse);
    }

    @Test(description = "Проверка action quack утки с корректным звуком и четным ID")
    @CitrusTest
    public void quackWithEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        QuackResponse quackResponse = new QuackResponse().sound("quack");

        Duck duckRubber = new Duck()
                .color("yellow")
                .height(0.15)
                .material("rubber")
                .sound("quack")
                .wingsState(WingState.ACTIVE);

        deleteDuckFinally(runner);

//        createDuckWithEvenId(runner, "yellow", 0.15, "rubber", "quack", WingState.ACTIVE);

        createDuckByDb(runner, "576556", duckRubber);
        validateDuckInDb(runner, "${duckId}", duckRubber);

        duckQuack(runner, "${duckId}", 1, 1);
        validateResponse(runner, HttpStatus.OK, quackResponse);
    }
}