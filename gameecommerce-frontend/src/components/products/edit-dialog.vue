<template>
  <div>
    <v-dialog
      v-model="dialog"
      max-width="600"
    >
      <template v-slot:activator="{ props: activatorProps }">
        <slot :props="activatorProps"></slot>
      </template>

      <v-card
        prepend-icon="mdi-archive-plus"
        :title="`Editar Produto - (${editingProduct.id}) ${editingProduct.name}`"
      >

        <v-form fast-fail @submit.prevent>

          <v-container>

            <v-row cols="10">
              <v-alert
                text="Responda os campos corretamente para editar um produto!"
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
                  v-model="editingProduct.name"
                  label="Nome"
                  :rules="nameRules"
                  variant="outlined"
                  required
                ></v-text-field>
              </v-col>
            </v-row>

            <v-row>
              <v-col cols="6">
                <v-text-field
                  v-model="editingProduct.stock"
                  label="Estoque"
                  :rules="stockRules"
                  type="number"
                  variant="outlined"
                ></v-text-field>
              </v-col>

              <v-col cols="6">
                <v-text-field
                  :rules="priceRules"
                  type="number"
                  v-model="editingProduct.price"
                  label="Preço"
                  variant="outlined"
                >
                </v-text-field>
              </v-col>
            </v-row>

            <v-row>
              <v-col cols="12">
                <v-textarea
                  v-model="editingProduct.description"
                  :rules="descriptionRules"
                  label="Descrição"
                  outlined
                  required
                ></v-textarea>
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
            text="Salvar"
            :loading="loading"
            variant="flat"
            @click="editProduct"
          ></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import {ref, reactive} from 'vue'
import httpService from "@/api/HttpService";

const emit = defineEmits(['edit'])

const props = defineProps({
  product: [Object, null]
})

const priceRules = ref([
  (v) => !!v || 'Preço é obrigatório',
  v => /^\d{0,4}\.?\d{0,2}$/.test(v) || 'Insira no máximo um número decimal com duas casas',
  (v) => (v && v >= 0.01) || 'Preço deve ser maior que 0.01',
])

const nameRules = ref([
  (v) => !!v || 'Nome é obrigatório',
  (v) => (v && v.length >= 4) || 'Nome deve ter no mínimo 4 caracteres',
  (v) => (v && v.length <= 200) || 'Nome deve ter no máximo 200 caracteres',
])

const stockRules = ref([
  (v) => !!v || 'Estoque é obrigatório',
  v => /^\d+$/.test(v) || 'Apenas números inteiros são permitidos',
  (v) => (v && v >= 0) || 'Estoque deve ser maior ou igual a 0',
  (v) => (v && v <= 999999999) || 'Estoque deve ser menor ou igual a 999999999'
])

const descriptionRules = ref([
  (v) => !!v || 'Descrição é obrigatória',
  (v) => (v && v.length >= 5) || 'Descrição deve ter no mínimo 5 caracteres.',
  (v) => (v && v.length <= 4000) || 'Descrição deve ter no máximo 4000 caracteres',
])


const editingProduct = reactive({
  id: props.product.id,
  name: props.product.name,
  price: props.product.price / 100,
  stock: props.product.stock,
  description: props.product.description
})


const dialog = ref(false)

function clearProduct() {
  editingProduct.name = ''
  editingProduct.price = 0
  editingProduct.stock = 0
  editingProduct.description = ''
}

const loading = ref(false)
const alert = ref(false)

function editProduct() {
  loading.value = true
  console.log("Editing Product: ", editingProduct)
  httpService.put(`products/${editingProduct.id}`, JSON.stringify({
    name: editingProduct.name,
    price: (editingProduct.price * 100).toFixed(0),
    stock: editingProduct.stock,
    description: editingProduct.description,
    commands: []
  })).then(response => {

    console.log("Response: ", response)
    dialog.value = false
    alert.value = false
    loading.value = false
    emit('edit')

    clearProduct()
  })
    .catch(error => {

      console.error("Error: ", error.message)
      loading.value = false
      alert.value = true

    })

}

</script>
