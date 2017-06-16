package logistics;

/**
 * Created by alien on 6/15/17.
 */

import logistics.domain.Delivery;
import logistics.domain.Package;
import logistics.domain.Step;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TESTS FOR @see {@link logistics.controllers.DeliveryController}
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {"postgresl_host=localhost",})
public class DeliveryControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test: Posting non-existing delivery.
     * RestController: @POST '/delivery' @see {@link logistics.controllers.DeliveryController#create(Delivery)}
     * Input: a non-existing delivery
     *
     * @result Return @see {@link org.springframework.http.HttpStatus#UNSUPPORTED_MEDIA_TYPE}
     */
    @Test
    public void testCreateDeliveryWithNonExistingEntity() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> entity = this.restTemplate.postForEntity("/delivery", null, String.class, headers);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Test: Posting delivery with missing data.
     * RestController: @POST '/delivery' @see {@link logistics.controllers.DeliveryController#create(Delivery)}
     * Input: a delivery entity with missing data
     *
     * @result Return @see {@link org.springframework.http.HttpStatus#INTERNAL_SERVER_ERROR}
     */
    @Test
    public void testCreateDeliveryWithMissingData() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Missing Set of Packages Test

        // Setting vehicleId buy no set of packages
        Delivery delivery = new Delivery();

        ResponseEntity<String> entity = this.restTemplate.postForEntity("/delivery", delivery, String.class, headers);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Test: Posting delivery with invalid data.
     * RestController: @POST '/delivery' @see {@link logistics.controllers.DeliveryController#create(Delivery)}
     * Input: a delivery entity with invalid references
     *
     * @result Return @see {@link HttpStatus#is4xxClientError()}
     */
    @Test
    public void testCreateDeliveryWithInvalidData() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // TODO - use JSONObject instead of String
        // incorrect vehicleId property
        // incorrect packages property
        String invalidDelivery = "{ " +
                "\"vehicles\" : \"123456\", " +
                "\"deliveryId\" :  \"1234567890\", " +
                "\"package\" : [ " +
                "   { \"id\": \"1\", \"weight\": \"14.50\"}," +
                "   { \"id\": \"2\", \"weight\": \"12.15\"}," +
                "   { \"id\": \"3\", \"weight\": \"19.50\"} " +
                "] }";

        ResponseEntity<String> entity = this.restTemplate.postForEntity("/delivery", invalidDelivery, String.class, headers);
        assertThat(entity.getStatusCode().is4xxClientError()).isTrue();
    }

    /**
     * Test: Posting delivery with valid data.
     * RestController: @POST '/delivery' @see {@link logistics.controllers.DeliveryController#create(Delivery)}
     * Input: a delivery entity with valid references
     *
     * @result Return @see {@link org.springframework.http.HttpStatus#CREATED}
     */
    @Test
    public void testCreateDeliveryWithValidData() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Delivery delivery = new Delivery();
        delivery.setVehicleId(123456L);
        delivery.setDeliveryId(1234567890L);

        // New packages (without id)
        ArrayList<Package> packages = new ArrayList<Package>(3);
        packages.add(new Package(1L, 14.50));
        packages.add(new Package(2L, 12.15));
        packages.add(new Package(3L, 19.50));

        delivery.setPackages(packages);

        ResponseEntity<String> entity = this.restTemplate.postForEntity("/delivery", delivery, String.class, headers);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    /**
     * Test: Getting list of steps with non-existing delivery reference.
     * RestController: @GET '/delivery/{deliveryId}/step' @see {@link logistics.controllers.DeliveryController#steps(Long)}
     * Input: a non-existing delivery reference
     *
     * @result Return @see {@link HttpStatus#is4xxClientError()}
     */
    @Test
    public void testStepsWithNonExistingReference() {

        String url = "/delivery//step";

        ResponseEntity<Object> entity = this.restTemplate.getForEntity(url, Object.class);
        assertThat(entity.getStatusCode().is4xxClientError()).isTrue();
    }

    /**
     * Test: Getting list of steps with invalid delivery reference.
     * RestController: @GET '/delivery/{deliveryId}/step' @see {@link logistics.controllers.DeliveryController#steps(Long)}
     * Input: a invalid delivery reference
     *
     * @result Return @see {@link org.springframework.http.HttpStatus#NOT_FOUND}
     */
    @Test
    public void testStepsWithInvalidReference() {

        String url = "/delivery/-1/step";

        ResponseEntity<Object> entity = this.restTemplate.getForEntity(url, Object.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    /**
     * Test: Getting list of steps with valid delivery reference.
     * RestController: @GET '/delivery/{deliveryId}/step' @see {@link logistics.controllers.DeliveryController#steps(Long)}
     * Input: a valid delivery reference
     *
     * @result Return @see {@link org.springframework.http.HttpStatus#OK} and Set of Steps @see {@link logistics.domain.Step}
     */
    @Test
    public void testStepsWithvalidReference() {

        String url = "/delivery/1234567890/step";

        ResponseEntity<Object> entity = this.restTemplate.getForEntity(url, Object.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody()).isInstanceOf(List.class);
        assertThat((List<Step>) entity.getBody()).isNotEmpty();
    }

}