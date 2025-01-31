package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.validation.json.JsonPathMessageValidationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient yellowDuckService;

    protected void sendGetRequest(TestCaseRunner runner,
                                  HttpClient URL, String path) {
        runner.$(http()
                .client(URL)
                .send()
                .get(path));
    }

    protected void sendPostRequestPayload(TestCaseRunner runner, HttpClient URL, String path, Object body) {
        runner.$(http()
                .client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    protected void sendDeleteRequest(TestCaseRunner runner,
                                     HttpClient URL, String path, String queName, String queValue) {
        runner.$(http()
                .client(URL)
                .send()
                .delete(path)
                .queryParam(queName, queValue));
    }

    protected void sendPutRequest(TestCaseRunner runner, HttpClient URL, String path) {
        runner.$(http()
                .client(URL)
                .send()
                .put(path));
    }

    protected void validationResponseMessage(TestCaseRunner runner, HttpClient URL, HttpStatus status, String responseMessage) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage)
        );
    }

    protected void validationResponseFromPayload(TestCaseRunner runner, HttpClient URL, HttpStatus status, Object body) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
        );
    }

    protected void validationResponseStatus(TestCaseRunner runner, HttpClient URL,  HttpStatus status) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    protected void validationResponseFromResources(TestCaseRunner runner, HttpClient URL,
                                                HttpStatus status, String pathToResource) {
        runner.$(http()
                .client(URL)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(pathToResource))
        );
    }

    protected void validationResponseJsonPath(TestCaseRunner runner, HttpClient URL,
                                         JsonPathMessageValidationContext.Builder body) {
        runner.$(
                http()
                        .client(URL)
                        .receive()
                        .response(HttpStatus.OK)
                        .message()
                        .type(MessageType.JSON)
                        .validate(body));
    }
}