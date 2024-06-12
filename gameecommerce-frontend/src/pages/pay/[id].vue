<template>
  <v-main class="d-flex flex-column align-center justify-center pay-card">
    <h1>Game Ecommerce</h1>
    {{ $route.params.id }}

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
                      src="https://i.imgur.com/rAMvIw2.png"
                    ></v-img>
                  </v-sheet>
                </v-col>
                <v-col>
                  <div class="d-flex flex-column">
                    <p class="font-weight-bold text-subtitle-1">Valor do Pagamento</p>
                    <p class="font-weight-bold text-subtitle-2">R$ 280,00</p>

                    <small class="mt-1">Válido até 24/05/2024 as 17:04</small>
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

    </v-container>


  </v-main>
</template>

<script setup>

</script>

<style scoped lang="scss">
.pay-card {
  height: 80%;
}
</style>

