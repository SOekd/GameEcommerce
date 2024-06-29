package com.gameecommerce.backend.gateway.impl.mercadopago;

import com.gameecommerce.backend.gateway.GatewayPayment;
import com.gameecommerce.backend.gateway.PaymentGatewayService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResultsResourcesPage;
import com.mercadopago.net.MPSearchRequest;
import com.mercadopago.resources.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MercadoPagoGatewayService implements PaymentGatewayService {

    @Override
    public GatewayPayment createPayment(String link, String player, long price) {
        MercadoPagoConfig.setAccessToken(getAccessToken());

        PaymentClient paymentClient = new PaymentClient();

        val reference = String.valueOf(UUID.randomUUID());

        PaymentCreateRequest paymentCreateRequest =
                PaymentCreateRequest.builder()
                        .transactionAmount(new BigDecimal("100"))
                        .description("Título do produto")
                        .paymentMethodId("pix")
                        .externalReference(reference)
                        .payer(
                                PaymentPayerRequest.builder()
                                        .type("customer")
                                        .entityType("individual")
                                        .email(player + "@gameecommerce.com")
                                        .build()
                        )
                        .build();

        Payment createdPayment = null;
        try {
            createdPayment = paymentClient.create(paymentCreateRequest);
        } catch (MPException e) {
            throw new RuntimeException(e);
        } catch (MPApiException e) {

            var apiResponse = e.getApiResponse();
            var content = apiResponse.getContent();
            System.out.println(content);

            throw new RuntimeException(e);
        }

        return new GatewayPayment(
                null,
                reference,
                createdPayment.getPointOfInteraction().getTransactionData().getQrCodeBase64()
        );
    }

    @Override
    public boolean paid(String id) {
        MercadoPagoConfig.setAccessToken(getAccessToken());

        PaymentClient paymentClient = new PaymentClient();

        System.out.println("Reference: " + id);
        val filter = new HashMap<String, Object>();
        filter.put("sort", "date_created");
        filter.put("criteria", "desc");
//        filter.put("external_reference", id);
        filter.put("range", "date_created");
        filter.put("begin_date", "NOW-30DAYS");
        filter.put("end_date", "NOW");

        MPResultsResourcesPage<Payment> result;
        try {

            result = paymentClient.search(MPSearchRequest.builder()
                    .filters(filter)
                    .limit(10)
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
            System.out.println(resultResult.getTransactionAmount());
            System.out.println(resultResult.getDescription());
            System.out.println(resultResult.getExternalReference());
            System.out.println(resultResult.getStatus());
        }

        return false;
    }

    private String getAccessToken() {
        return System.getenv("MERCADO_PAGO_ACCESS_TOKEN");
    }

}
