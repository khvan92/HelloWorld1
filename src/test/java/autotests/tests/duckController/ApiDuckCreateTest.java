package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class ApiDuckCreateTest extends DuckActionsClient {

    @Test(description = "Проверка создания утки с material = rubber")
    @CitrusTest
    public void createRubberDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duckRubber = new Duck().color("yellow").height(0.15).material("rubber").sound("quack").wingsState(WingState.ACTIVE);

        deleteDuckFinally(runner);

        createDuck(runner, duckRubber);
        getNewDuckId(runner);
        validateDuckInDb(runner, "${duckId}", duckRubber);

//        duckProperties(runner, "${duckId}");
//        checkAllProperties(runner, "yellow", 0.15, "rubber", "quack", "ACTIVE");
    }

    @Test(description = "Проверка создания утки с material = wood")
    @CitrusTest
    public void createWoodDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        Duck duckWood = new Duck().color("yellow").height(0.15).material("wood").sound("quack").wingsState(WingState.ACTIVE);

        deleteDuckFinally(runner);

        createDuck(runner, duckWood);
        getNewDuckId(runner);
        validateDuckInDb(runner, "${duckId}", duckWood);

//        duckProperties(runner, "${duckId}");
//        checkAllProperties(runner, "yellow", 0.15, "wood", "quack", "ACTIVE");
    }
}