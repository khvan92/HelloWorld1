package autotests.tests.duckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Тесты на duck-controller")
@Feature("Эндпоинт /api/duck/create")
public class ApiDuckCreateTest extends DuckActionsClient {

    Duck duck1 = new Duck()
            .color("yellow").height(0.15)
            .material("plastic").sound("quack").wingsState(WingState.ACTIVE);
    Duck duck2 = new Duck()
            .color("green").height(0.3)
            .material("rubber").sound("woof").wingsState(WingState.FIXED);
    Duck duck3 = new Duck()
            .color("red").height(2.0)
            .material("iron").sound("meow").wingsState(WingState.UNDEFINED);
    Duck duck4 = new Duck()
            .color("white").height(3.0)
            .material("wood").sound("piupiu").wingsState(WingState.ACTIVE);
    Duck duck5 = new Duck()
            .color("black").height(0.0)
            .material("latex").sound("grrr").wingsState(WingState.FIXED);

    @DataProvider(name = "duckList")
    public Object[][] duckList() {
        return new Object[][]{
                {duck1, null},
                {duck2, null},
                {duck3, null},
                {duck4, null},
                {duck5, null}
        };
    }

    @Test(dataProvider = "duckList")
    @CitrusTest
    @CitrusParameters({"payload", "runner"})
    public void successfulDuckCreate(Duck payload, @Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, payload);
        getNewDuckId(runner);
        validateDuckInDb(runner, "${duckId}", payload);
    }

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