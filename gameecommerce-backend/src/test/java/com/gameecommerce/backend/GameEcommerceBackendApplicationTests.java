package com.gameecommerce.backend;

import com.gameecommerce.backend.gateway.impl.mercadopago.MercadoPagoGatewayService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResultsResourcesPage;
import com.mercadopago.net.MPSearchRequest;
import com.mercadopago.resources.payment.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ContextConfiguration
@ActiveProfiles(profiles = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GameEcommerceBackendApplicationTests {


    @Test
    void testModulesIntegrity() {
        ApplicationModules.of(GameEcommerceApplication.class).verify();
    }

    @Test
    void testCheckout() {
        MercadoPagoConfig.setAccessToken(getAccessToken());

        System.out.println("Access Token: " + MercadoPagoConfig.getAccessToken());
        System.out.println("Access Token 2: " + getAccessToken());
        System.out.println("Check:" + MercadoPagoConfig.getAccessToken().equals(getAccessToken()));

        PaymentClient paymentClient = new PaymentClient();


//        System.out.println("Reference: " + id);
        HashMap<String, Object> filter = new HashMap<String, Object>();

        MPResultsResourcesPage<Payment> result;
        try {

            result = paymentClient.search(MPSearchRequest.builder()
                    .filters(Map.of(
                                    "sort", "date_created",
                                    "criteria", "desc",
                                    "external_reference", "046b9f54-8e7f-4463-8d08-87640b20a62a"
                            )
                    )
                    .limit(1)
                    .offset(0)
                    .build());
        } catch (MPException e) {

            throw new RuntimeException(e);
        } catch (MPApiException e) {
            var apiResponse = e.getApiResponse();
            var content = apiResponse.getContent();
            System.out.println(content);

            throw new RuntimeException(e);
        }

        System.out.println("Size: " + result.getResults().size());

        for (Payment resultResult : result.getResults()) {
            System.out.println("-----------------");
            System.out.println(resultResult.getExternalReference());
            System.out.println(resultResult.getStatus());
            System.out.println(resultResult.toString());
        }

    }

    private String getAccessToken() {
        return System.getenv("MERCADO_PAGO_ACCESS_TOKEN");
    }

}
