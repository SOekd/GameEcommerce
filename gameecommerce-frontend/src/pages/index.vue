<template>
  <v-main class="d-flex flex-column align-center justify-center" style="min-height: 300px;">
    <h1>Game Ecommerce</h1>
    <h2>Crie pagamentos para o seu servidor de jogo de forma rápida.</h2>

    <v-btn
      class="mt-10"
      variant="outlined"
      @click="goToAdmin"
    >
      Começar
    </v-btn>
  </v-main>
</template>

<script setup>

import {useRouter} from "vue-router";

const router = useRouter()

function goToAdmin() {
  router.push('/admin')
}

</script>

