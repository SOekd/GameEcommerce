<template>

  <v-container fluid>
    <h1>Criar Compra</h1>
    <p>Gere o link da sua encomenda através desse painel. Aqui você poderá gerar o link do pix facilmente!</p>
    <v-data-table
      v-model="selected"
      :headers="headers"
      :items="products"
      :loading="loading"
      show-expand
      show-select
    >

      <template v-slot:loading>
        <v-skeleton-loader type="table-row@10"></v-skeleton-loader>
      </template>

      <template v-slot:top>
        <v-toolbar class="px-4">
          <v-toolbar-title>Produtos</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn class="mx-2"
                 v-if="!mobile"
                 :disabled="loading"
                 @click="fetchProducts"
                 text="Atualizar"
                 variant="flat"
                 append-icon="mdi-refresh">
          </v-btn>
          <v-btn v-else
                 :disabled="loading"
                 @click="fetchProducts"
                 variant="elevated"
                 size="small"
                 icon="mdi-refresh"
          >
          </v-btn>

          <create-order-dialog v-slot:default="{ props: activatorProps }" :selected="parseSelected()">
            <v-btn class="mx-2"
                   v-if="!mobile"
                   :disabled="loading"
                   v-bind="activatorProps"
                   text="Gerar"
                   variant="flat"
                   append-icon="mdi-hammer">
            </v-btn>
            <v-btn v-else
                   :disabled="loading"
                   v-bind="activatorProps"
                   variant="elevated"
                   size="small"
                   icon="mdi-hammer"
            >
            </v-btn>
          </create-order-dialog>

        </v-toolbar>
      </template>

      <template v-slot:expanded-row="{ columns, item }">
        <tr>
          <td :colspan="columns.length" class="text-body-1">
            {{ item.description }}
          </td>
        </tr>
      </template>

      <template v-slot:item.amount="{ item }">

          <v-number-input
            :max=99
            :min=1
            :model-value=1
            inset
            variant="outlined"
          >
          </v-number-input>

      </template>

    </v-data-table>
  </v-container>
</template>

<script setup>


import {useDisplay} from "vuetify";
import {onMounted, ref} from "vue";
import httpService from "@/api/HttpService";
import CreateOrderDialog from "@/components/orders/create-order-dialog.vue";

const {mobile} = useDisplay()
const loading = ref(false)

const headers = ref([
  {
    title: 'id',
    align: 'center',
    sortable: true,
    key: 'id'
  },
  {
    title: 'nome',
    align: 'start',
    sortable: true,
    key: 'name'
  },
  {
    title: 'quantidade',
    align: 'start',
    sortable: true,
    key: 'amount'
  },
  {
    title: 'preço',
    align: 'start',
    sortable: true,
    key: 'price'
  },
  {
    title: 'servidor',
    align: 'start',
    sortable: true,
    key: 'servers'
  }
])

const selected = ref([])

const products = ref([])

const parseSelected = () => {

  let currentProducts = []
  for (let i = 0; i < selected.value.length; i++) {

    let productId = selected.value[i]

    let product = products.value.find(it => it.id === productId)

    currentProducts.push(product)
  }

  return currentProducts
}


const fetchProducts = async () => {
  loading.value = true
  httpService.get('products').then(response => {
    products.value = response.data.map(it => Object.assign(it, { amount: 1 }))
    loading.value = false
  }).catch(error => {
    console.error('Erro ao buscar produtos:', error);
    loading.value = false;
  });
};

onMounted(fetchProducts)

</script>

<route lang="yaml">
meta:
  layout: dashboard
</route>
