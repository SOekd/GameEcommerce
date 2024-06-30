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

            <v-row cols="12">
              <v-alert
                text="Responda os campos corretamente para gerar um pedido!"
                type="error"
                border="start"
                variant="elevated"
                closable
                class="ma-3"
                v-model="alert"
              ></v-alert>
              <v-alert
                v-if="link !== ''"
                type="success"
                border="start"
                variant="elevated"
                closable
                class="ma-3"
              >
                <v-btn
                  @click="copyLinkToClipboard"
                  variant="elevated"
                  color="success"
                  class="ma-2"
                >
                  Clique aqui para copiar o link!
                </v-btn>
              </v-alert>
            </v-row>

            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="creatingProduct.name"
                  label="Nome no Jogo"
                  :rules="nameRules"
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
            text="Fechar"
            color="error"
            variant="outlined"
            @click="dialog = false"
          ></v-btn>

          <v-btn
            color="success"
            text="Gerar"
            :loading="loading"
            variant="flat"
            @click="postOrder"
          ></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import {ref, reactive} from 'vue'
import httpService from "@/api/HttpService";

const props = defineProps({
  selected: {
    type: Array,
    required: true
  }
})

const nameRules = ref([
  (v) => !!v || 'Nome é obrigatório',
  (v) => (v && v.length >= 4) || 'Nome deve ter no mínimo 4 caracteres',
  (v) => (v && v.length <= 200) || 'Nome deve ter no máximo 200 caracteres',
])


const creatingProduct = reactive({
  name: ''
})


const dialog = ref(false)

function clearOrderName() {
  creatingProduct.name = ''
}

const loading = ref(false)
const alert = ref(false)

const link = ref('')

function postOrder() {
  loading.value = true

  link.value = ''

  const products = new Map();

  props.selected.forEach(product => {
    products.set(product.id, product.amount)
  })

  httpService.post('orders', JSON.stringify({
    playerName: creatingProduct.name,
    products: Object.fromEntries(products)
  })).then(response => {

    loading.value = false

    link.value = "http://localhost:3000/pay/" + response.data.link

    clearOrderName()

  })
    .catch(error => {

      console.error("Error: ", error.message)
      loading.value = false
      alert.value = true

    })
}

function copyLinkToClipboard() {

  dialog.value = false
  alert.value = false

  navigator.clipboard.writeText(link.value)
}

</script>

<style scoped>

</style>
