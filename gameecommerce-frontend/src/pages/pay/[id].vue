<template>
  <v-main class="d-flex flex-column align-center justify-center pay-card" v-if="order">
    <div v-if="order.state === 'PENDING_PAYMENT'">
      <v-container class="d-flex align-center flex-column justify-center">
          <h1>Game Ecommerce</h1>
          <p>Nome no jogo: <b>{{ order.playerName }}</b></p>
      </v-container>

      <v-container class="" style="max-width: 500px">

        <v-row class="align-center justify-center ">
          <v-col cols="12">
            <v-sheet
              class="pa-5 "
              :elevation="2"
              border
              rounded
            >
              <div class="d-flex align-center">
                <p>Escaneie o código QR pelo seu app de pagamentos ou Internet Banking</p>
                <v-icon
                  icon="mdi-checkbox-marked-circle"
                  end
                ></v-icon>
              </div>
            </v-sheet>
          </v-col>
        </v-row>

        <v-row>
          <v-col cos="12">
            <v-sheet
              :elevation="2"
              border
              rounded
            >
              <v-container>
                <v-row>
                  <v-col cols="4">

                    <v-sheet
                      :elevation="2"
                      border
                      class="bg-white"
                      rounded>
                      <v-img
                        :src='getImageSource()'
                      ></v-img>
                    </v-sheet>
                  </v-col>
                  <v-col>
                    <div class="d-flex flex-column">
                      <p class="font-weight-bold text-subtitle-1">Valor do Pagamento</p>
                      <p class="font-weight-bold text-subtitle-2">R$ {{ order.price }}</p>

                      <small class="mt-1">{{ getExpireDate() }}</small>
                      <v-btn
                        class="mt-3"
                        variant="elevated"
                      >
                        Copiar Código
                      </v-btn>
                    </div>
                  </v-col>
                </v-row>
              </v-container>
            </v-sheet>
          </v-col>
        </v-row>

        <v-row>
          <v-col cos="12">
            <v-sheet
              :elevation="2"
              border
              class="pa-10"
              rounded
            >
              <v-table>
                <thead>
                <tr>
                  <th class="text-left">
                    produto
                  </th>
                  <th class="text-left">
                    quantidade
                  </th>
                </tr>
                </thead>
                <tbody>
                <tr
                  v-for="product in order.products"
                  :key="product.product.name">
                  <td>{{ product.product.name }}</td>
                  <td>{{ product.amount }}</td>
                </tr>
                </tbody>
              </v-table>
            </v-sheet>
          </v-col>
        </v-row>
      </v-container>
    </div>

    <div v-else-if="order.state === 'EXPIRED'">
      <v-empty-state
        icon="mdi-clock-alert"
        title="Pagamento Expirado!"
        text="O pagamento que você queria já expirou! Peça um novo link!"
      ></v-empty-state>
    </div>

    <div v-else-if="order.state === 'DELIVERED'">
      <v-empty-state
        icon="mdi-truck-delivery"
        title="Entregue!"
        text="Seu produto foi entregue com sucesso!"
      ></v-empty-state>
    </div>

    <div v-else-if="order.state === 'WAITING_DELIVERY'">
      <v-empty-state
        icon="mdi-cash-check"
        title="Pago!"
        text="Aguarde os produtos chegarem! Em breve você irá recebe-los!"
      ></v-empty-state>
    </div>

    <div v-else>
      <v-empty-state
        icon="mdi-alert-circle"
        title="Cancelado!"
        text="Seu pagamento foi cancelado!"
      ></v-empty-state>
    </div>

  </v-main>
</template>

<script setup>

import httpService from "@/api/HttpService";
import {useRoute, useRouter} from "vue-router";
import {onUnmounted, ref} from "vue";

const route = useRoute()
const router = useRouter()

const order = ref()

const loadOrder = async () => httpService.get('orders/' + route.params.id).then(response => {

  order.value = response.data

})
  .catch(error => {
    console.error("Error: ", error.message)
    router.push('/pay/not-found')
  })

await loadOrder()

let intervalId = setInterval(loadOrder, 2000);

onUnmounted(() => {
  clearInterval(intervalId);
})

function getImageSource() {
  return 'data:image/jpeg;base64,' + order.value.paymentGateway.pixQrCode
}

function getExpireDate() {
  const date = new Date(order.value.expiration);

  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = date.getFullYear();
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');

  return `Válido até ${day}/${month}/${year} às ${hours}:${minutes}`;
}

</script>

<style scoped lang="scss">
.pay-card {
  height: 80%;
}
</style>

