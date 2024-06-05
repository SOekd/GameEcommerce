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
        title="Gerar Pedido"
      >

        <v-form fast-fail @submit.prevent>

          <v-container>

            <v-row cols="10">
              <v-alert
                text="Responda os campos corretamente para gerar um pedido!"
                type="error"
                border="start"
                variant="elevated"
                closable
                class="ma-3"
                v-model="alert"
              ></v-alert>
            </v-row>

            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="creatingProduct.name"
                  label="Nome"
                  :rules="nameRules"
                  variant="outlined"
                  required
                ></v-text-field>
              </v-col>
            </v-row>

            <v-row>
              <v-col cols="12">
                <v-text-field
                  :rules="priceRules"
                  type="number"
                  v-model="creatingProduct.price"
                  label="Preço"
                  variant="outlined"
                >
                </v-text-field>
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
            text="Gerar"
            :loading="loading"
            variant="flat"
            @click="postProduct"
          ></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import {ref, reactive} from 'vue'
import httpService from "@/api/HttpService";

const nameRules = ref([
  (v) => !!v || 'Nome é obrigatório',
  (v) => (v && v.length >= 4) || 'Nome deve ter no mínimo 4 caracteres',
  (v) => (v && v.length <= 200) || 'Nome deve ter no máximo 200 caracteres',
])


const creatingProduct = reactive({
  name: '',
  price: 0,
  description: '',
  servers: []
})


const dialog = ref(false)

function clearProduct() {
  creatingProduct.name = ''
  creatingProduct.price = 0
  creatingProduct.description = ''
  creatingProduct.servers = []
}

const loading = ref(false)
const alert = ref(false)

function postProduct() {
  loading.value = true
  httpService.post('products', JSON.stringify({
    name: creatingProduct.name,
    price: (creatingProduct.price * 100).toFixed(0),
    description: creatingProduct.description,
    servers: creatingProduct.servers
  })).then(response => {

    console.log("Response: ", response)
    dialog.value = false
    alert.value = false
    loading.value = false

    clearProduct()

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
