<template>
  <v-main class="d-flex flex-column align-center justify-center pay-card">
    <h1>Game Ecommerce</h1>
    <p>Nome no jogo: <b>{{ order.playerName }}</b></p>

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
                    <p class="font-weight-bold text-subtitle-2">R$ {{ order.price / 100 }}</p>

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
            <p>Produtos:</p><br>
            <ul>
              <li :key="product.product.name" v-for="product in order.products">
                {{ product.amount }} - {{ product.product.name }}
              </li>
            </ul>
          </v-sheet>
        </v-col>
      </v-row>

    </v-container>


  </v-main>
</template>

<script setup>

import httpService from "@/api/HttpService";
import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";

const route = useRoute()
const router = useRouter()

const order = ref()


httpService.get('orders/' + route.params.id).then(response => {

  console.log("Response: ", response)
  order.value = response.data


})
  .catch(error => {
    console.error("Error: ", error.message)
    router.push('/pay/not-found')
  })


function getImageSource() {
  return 'data:image/jpeg;base64,' + order.value.gatewayPayment.pixQrCode
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

