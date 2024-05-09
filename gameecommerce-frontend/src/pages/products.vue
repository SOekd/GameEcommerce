<template>
  <h1>Produtos!</h1>
  <v-container fluid>
    <v-data-table
      :headers="headers"
      :items="products"
      :loading="loading"
      :sort-by="[{key: 'id', direction: 'asc'}]"
    >

      <template v-slot:loading>
        <v-skeleton-loader type="table-row@10"></v-skeleton-loader>
      </template>

      <template v-slot:top>
        <v-toolbar class="px-4">
          <v-toolbar-title>Produtos</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn class="mx-2"
                 :disabled="loading"
                 @click="fetchProducts"
                 text="Recarregar"
                 variant="flat"
                 append-icon="mdi-refresh">
          </v-btn>
          <v-btn color="success"
                 variant="outlined"
                 text="Criar Produto"
                 append-icon="mdi-plus">
          </v-btn>
        </v-toolbar>
      </template>

    </v-data-table>
  </v-container>
</template>

<script setup>

// localhost:8080/api/products
import {onMounted, ref} from 'vue'
import axios from 'axios'

const loading = ref(false)

const headers = ref([
  {
    title: 'id',
    align: 'start',
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
    title: 'preço',
    align: 'start',
    sortable: true,
    key: 'price'
  },
  {
    title: 'estoque',
    align: 'start',
    sortable: true,
    key: 'stock'
  },
  {
    title: 'ações',
    align: 'start',
    sortable: false,
    key: 'actions'
  }
])

const products = ref([])
//
const apiBaseUrl = import.meta.env.VITE_API_URL
//
const fetchProducts = async () => {
  try {
    loading.value = true;
    console.log("Buscando em:", `http://${apiBaseUrl}/api/v1/products`)
    axios.get(`http://${apiBaseUrl}/api/v1/products`).then(response => {
      console.log('Produtos:', response.data);
      products.value = response.data;
      loading.value = false;
    }).catch(error => {
      console.error('Erro CORS:', error);
      loading.value = false;
    });
  } catch (error) {
    console.error('Erro ao buscar produtos:', error);
    loading.value = false;
  }
};


onMounted(fetchProducts)

</script>

<route lang="yaml">
meta:
layout: dashboard
</route>
