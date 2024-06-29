package com.gameecommerce.backend.gateway;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "order_payment")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GatewayPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gatewayOrderId;

    @Column(columnDefinition = "TEXT")
    private String pixQrCode;

}
