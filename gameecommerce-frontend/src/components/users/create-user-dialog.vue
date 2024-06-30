<template>
  <div class="pa-4 text-center">
    <v-dialog
      v-model="dialog"
      max-width="600"
    >
      <template v-slot:activator="{ props: activatorProps }">
        <slot :props="activatorProps"></slot>
      </template>

      <v-card
        prepend-icon="mdi-archive-plus"
        title="Criar Usuário"
      >

        <v-form fast-fail @submit.prevent>

          <v-container>

            <v-row cols="10">
              <v-alert
                text="Responda os campos corretamente para criar um usuário!"
                type="error"
                border="start"
                variant="elevated"
                closable
                class="ma-3"
                v-model="alert"
              ></v-alert>
            </v-row>

            <v-row>
              <v-col cols="6">
                <v-text-field
                  v-model="creatingUser.name"
                  label="Nome"
                  :rules="nameRules"
                  variant="outlined"
                  required
                ></v-text-field>
              </v-col>

              <v-col cols="6">
                <v-text-field
                  v-model="creatingUser.password"
                  label="Senha"
                  :rules="passwordRules"
                  variant="outlined"
                  required
                ></v-text-field>
              </v-col>
            </v-row>

          </v-container>

        </v-form>


        <v-divider></v-divider>

        <v-card-actions>
          <v-btn
            text="Cancelar"
            color="error"
            variant="outlined"
            @click="dialog = false"
          ></v-btn>

          <v-btn
            color="success"
            text="Criar"
            :loading="loading"
            variant="flat"
            @click="postUser"
          ></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import {ref, reactive} from 'vue'
import httpService from "@/api/HttpService";

const emit = defineEmits(['create'])


const nameRules = ref([
  (v) => !!v || 'Nome é obrigatório',
  (v) => (v && v.length >= 2) || 'Nome deve ter no mínimo 4 caracteres',
  (v) => (v && v.length <= 60) || 'Nome deve ter no máximo 200 caracteres',
])

const passwordRules = ref([
  (v) => !!v || 'Senha é obrigatória',
  (v) => (v && v.length >= 6) || 'Senha deve ter no mínimo 6 caracteres',
  (v) => (v && v.length <= 60) || 'Senha deve ter no máximo 60 caracteres',
  (v) => /[A-Z]/.test(v) || 'Senha deve conter ao menos uma letra maiúscula',
  (v) => /[a-z]/.test(v) || 'Senha deve conter ao menos uma letra minúscula',
  (v) => /\d/.test(v) || 'Senha deve conter ao menos um número',
])

const emailRules = ref([
  (v) => !!v || 'Email é obrigatório',
  (v) => /.+@.+\..+/.test(v) || 'Email deve ser válido',
])

const creatingUser = reactive({
  name: '',
  password: '',
  email: '',
  roles: []
})


const dialog = ref(false)

function clearUser() {
  creatingUser.name = ''
  creatingUser.password = ''
  creatingUser.email = ''
  creatingUser.roles = []
}

const loading = ref(false)
const alert = ref(false)

function postUser() {
  loading.value = true
  httpService.post('users', JSON.stringify({
    username: creatingUser.name,
    password: creatingUser.password,
    email: creatingUser.email,
    roles: creatingUser.roles
  })).then(response => {

    dialog.value = false
    alert.value = false
    loading.value = false
    emit('create')

    clearUser()
  })
    .catch(error => {

      console.error("Error: ", error.message)
      loading.value = false
      alert.value = true

    })
}

</script>

<style scoped>

</style>
