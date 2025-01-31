package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.exceptions.CitrusRuntimeException;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.validation.json.JsonPathMessageValidationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.atomic.AtomicReference;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient yellowDuckService;

    @Autowired
    protected SingleConnectionDataSource testDb;

    @Step("Эндпоинт для взаимодействия с БД")
    public void dataBaseUpdate(TestCaseRunner runner, String sql) {
        runner.$(sql(testDb)
                .statement(sql));
    }

    @Step("Эндпоинт для создания утки через БД")
    public void createDuckByDb(TestCaseRunner runner, String id, Duck duck) {
        runner.variable("duckId",  id);
        runner.variable("color",  duck.color());
        runner.variable("height",  duck.height());
        runner.variable("material",  duck.material());
        runner.variable("sound",  duck.sound());
        runner.variable("wings_state",  duck.wingsState());

        dataBaseUpdate(runner, "insert into DUCK (id, color, height, material, sound, wings_state) " +
                "values (${duckId}, '${color}', ${height}, '${material}', '${sound}', '${wings_state}');");
    }

    @Step("Эндпоинт для удаления утки через БД")
    public void deleteDuckByDb(TestCaseRunner runner, String id) {
        runner.variable("duckId", id);
        dataBaseUpdate(runner, "delete from DUCK where id = ${duckId};");
    }

    @Step("Эндпоинт для удаления утки через БД в конце теста")
    public void deleteDuckFinally(TestCaseRunner runner) {
        runner.$(doFinally().actions(context->
        dataBaseUpdate(runner, "delete from DUCK where id = ${duckId};")
    ));
    }

    @Step("Эндпоинт для обновления данных утки через БД")
    public void updateDuckByDb(TestCaseRunner runner, String id, Duck duck) {
        runner.variable("duckId",  id);
        runner.variable("color",  duck.color());
        runner.variable("height",  duck.height());
        runner.variable("material",  duck.material());
        runner.variable("sound",  duck.sound());
        runner.variable("wings_state",  duck.wingsState());

        dataBaseUpdate(runner, "update DUCK set color = '${color}', height = ${height}, material = '${material}', sound = '${sound}', wings_state = '${wings_state}' "
                + "where id = ${duckId};");
    }

    @Step("Эндпоинт для валидации данных утки через БД")
    public void validateDuckInDb(TestCaseRunner runner, String id, Duck duck) {
        runner.$(query(testDb)
                .statement("select * from duck where id = "+id + ";")
                .validate("color", duck.color())
                .validate("height", duck.height().toString())
                .validate("material", duck.material())
                .validate("sound", duck.sound())
                .validate("wings_state", duck.wingsState().toString())
        );
    }

    @Step("Эндпоинт для валидации удаления утки через БД")
    public void validateDeletedDuckInDb(TestCaseRunner runner, String id) {
        runner.$(query(testDb)
                .statement("select count(1) as overall_cnt from duck where id = "+id + ";")
                .validate("overall_cnt", "0" )
        );
    }

    @Step("Эндпоинт для метода Swim")
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id));
    }

    @Step("Эндпоинт для метода Fly")
    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id));
    }

    @Step("Эндпоинт для метода Properties")
    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    @Step("Эндпоинт для метода Quack")
    public void duckQuack(TestCaseRunner runner, String id, int repetitionCount, int soundCount) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", String.valueOf(repetitionCount))
                .queryParam("soundCount", String.valueOf(soundCount)));
    }

    @Step("Эндпоинт для валидации ответа от сервера")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, String responseMessage) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage)
        );
    }

    @Step("Эндпоинт для валидации ответа от сервера")
    public void validateResponse(TestCaseRunner runner, HttpStatus status, Object body) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
        );
    }

    @Step("Эндпоинт для валидации ответа от сервера")
    public void validateResponse(TestCaseRunner runner, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Step("Эндпоинт для валидации ответа с помощью Resource")
    public void validateResponseFromResources(TestCaseRunner runner, HttpStatus status, String pathToResource) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(pathToResource))
        );
    }

    @Step("Эндпоинт для валидации Json")
    public void validateResponseJsonPath(TestCaseRunner runner,
                                         JsonPathMessageValidationContext.Builder body) {
        runner.$(
                http()
                        .client(yellowDuckService)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .validate(body));
    }

    @Step("Эндпоинт для метода Create")
    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    @Step("Эндпоинт для создания утки с четным id")
    public void createDuckWithEvenId(TestCaseRunner runner, String color, double height, String material, String sound, WingState wingsState) {
        AtomicReference<Integer> id = new AtomicReference<>(0);
        Duck duck = new Duck().color(color).height(height).material(material).sound(sound).wingsState(wingsState);

        do {
            createDuck(runner, duck);
            getNewDuckId(runner);
            runner.$(action ->
            {
                id.set(Integer.parseInt(action.getVariable("duckId")));
            });
        } while (id.get() % 2 != 0);
    }

    @Step("Эндпоинт для создания утки с нечетным id")
    public void createDuckWithOddId(TestCaseRunner runner, String color, double height, String material, String sound, WingState wingsState) {
        AtomicReference<Integer> id = new AtomicReference<>(0);
        Duck duck = new Duck().color(color).height(height).material(material).sound(sound).wingsState(wingsState);

        do {
            createDuck(runner, duck);
            getNewDuckId(runner);
            runner.$(action ->
            {
                id.set(Integer.parseInt(action.getVariable("duckId")));
            });
        } while (id.get() % 2 == 0);
    }

    @Step("Эндпоинт для метода Delete")
    public void duckDelete(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }

    @Step("Эндпоинт для метода GetAllIds")
    public void duckGetAllIds(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/getAllIds"));
    }

    @Step("Эндпоинт для метода Update")
    public void duckUpdate(TestCaseRunner runner, String id, String color, double height, String material, String sound) {
        runner.$(http().client(yellowDuckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", color)
                .queryParam("height", String.valueOf(height))
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound));
    }

    @Step("Эндпоинт для получения id утки после создания через api")
    public void getNewDuckId(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    @Step("Эндпоинт для проверки материала утки")
    public void checkMaterial(TestCaseRunner runner, String material) {
        validateResponseJsonPath(
                runner,
                jsonPath().expression("$.material", material)
        );
    }

    @Step("Эндпоинт для проверки параметров утки")
    public void checkAllProperties(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        validateResponseJsonPath(
                runner,
                jsonPath()
                        .expression("$.color", color)
                        .expression("$.height", height)
                        .expression("$.material", material)
                        .expression("$.sound", sound)
                        .expression("$.wingsState", wingsState)
        );
    }

    @Step("Эндпоинт для получения id всех уток")
    public void getIds(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.[*]", "allDucksIds")));
    }

    @Step("Эндпоинт для проверки удаления утки через api")
    public void checkDeletedId(TestCaseRunner runner) {
        AtomicReference<Integer> id = new AtomicReference<>(0);
        AtomicReference<String> allIds = new AtomicReference<String>();
        getIds(runner);
        runner.$(action ->
        {
            id.set(Integer.parseInt(action.getVariable("duckId")));
            allIds.set(action.getVariable("allDucksIds"));

            if(allIds.get().contains(id.get().toString())) {
                action.addException(new CitrusRuntimeException("Уточка не удалена"));
            }
        });
    }
}