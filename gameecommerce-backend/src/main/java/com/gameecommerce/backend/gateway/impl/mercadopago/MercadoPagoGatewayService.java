package com.gameecommerce.backend.gateway.impl.mercadopago;

import com.gameecommerce.backend.gateway.PaymentGateway;
import com.gameecommerce.backend.gateway.PaymentGatewayService;
import com.gameecommerce.backend.order.OrderState;
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
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class MercadoPagoGatewayService implements PaymentGatewayService {

    @Override
    public PaymentGateway createPayment(String link, String player, double price) {
        MercadoPagoConfig.setAccessToken(getAccessToken());

        PaymentClient paymentClient = new PaymentClient();

        val reference = String.valueOf(UUID.randomUUID());

        PaymentCreateRequest paymentCreateRequest =
                PaymentCreateRequest.builder()
                        .transactionAmount(new BigDecimal(price))
                        .description("Produto - GameEcommerce")
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

        Payment createdPayment;
        try {
            createdPayment = paymentClient.create(paymentCreateRequest);
        } catch (MPException e) {
            throw new RuntimeException(e);
        } catch (MPApiException exception) {

            // custava adicionar na mensagem da exceção?
            var apiResponse = exception.getApiResponse();
            var content = apiResponse.getContent();
            log.error(content);

            throw new RuntimeException(exception);
        }

        return new PaymentGateway(
                null,
                reference,
                createdPayment.getPointOfInteraction().getTransactionData().getQrCodeBase64()
        );
    }

    @Override
    public @Nullable OrderState getStage(String id) {
        MercadoPagoConfig.setAccessToken(getAccessToken());

        PaymentClient paymentClient = new PaymentClient();

        MPResultsResourcesPage<Payment> result;
        try {

            result = paymentClient.search(MPSearchRequest.builder()
                    .filters(Map.of(
                                    "sort", "date_created",
                                    "criteria", "desc",
                                    "external_reference", id
                            )
                    )
                    .limit(1)
                    .offset(0)
                    .build());
        } catch (MPException exception) {
            throw new RuntimeException(exception);
        } catch (MPApiException exception) {

            val apiResponse = exception.getApiResponse();
            val content = apiResponse.getContent();
            log.error(content);

            throw new RuntimeException(exception);
        }

        val payment = result.getResults().stream()
                .findFirst()
                .orElse(null);

        if (payment == null) {
            return null;
        }

        val status = payment.getStatus();

        return switch (status) {
            case "approved" -> OrderState.WAITING_DELIVERY;
            case "pending", "in_process", "authorized" -> OrderState.PENDING_PAYMENT;
            case "in_mediation" -> OrderState.REFUNDING;
            case "refunded", "charged_back" -> OrderState.REFUNDED;
            case "rejected", "cancelled" -> OrderState.CANCELLED;
            default -> throw new RuntimeException("Unknown payment status: " + status);
        };
    }

    private String getAccessToken() {
        return System.getenv("MERCADO_PAGO_ACCESS_TOKEN");
    }

}
