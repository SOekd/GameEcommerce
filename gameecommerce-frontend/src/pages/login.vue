<template>
  <v-main class="d-flex flex-column align-center justify-center" style="min-height: 300px;">
    <h1>Game Ecommerce</h1>
    <h2>Faça Login</h2>

    <v-alert
      text="Seu usuário ou senha estão incorretos!"
      type="error"
      border="start"
      variant="elevated"
      closable
      class="mt-5"
      v-model="alert"
    ></v-alert>

    <v-text-field
      class="mt-10"
      style="width: 60%"
      label="Usuário"
      v-model="user.username"
      persistent-hint
      variant="outlined">

    </v-text-field>

    <v-text-field
      style="width: 60%"
      label="Senha"
      type="password"
      persistent-hint
      v-model="user.password"
      variant="outlined">
    </v-text-field>

    <v-btn
      class="mt-10"
      variant="outlined"
      @click="login"
    >
      Entrar
    </v-btn>
  </v-main>
</template>

<script setup>

import {reactive, ref} from "vue";
import authenticationService from "@/api/AuthenticationService";
import {useRouter} from "vue-router";

const router = useRouter()

const alert = ref(false)

const user = reactive({
  username: '',
  password: ''
})

function login() {


  if (!user.username || !user.password) {
    alert.value = true
    return
  }


  console.log("user: " + user.username + " password: " + user.password)
  authenticationService.authenticate(user.username, user.password)
    .then(response => {
      if (!response) {
        alert.value = true
        return
      }
      console.log("login: " + response)

      router.push('/admin')
    })
    .catch(() => {
      alert.value = true
    })
}

</script>

