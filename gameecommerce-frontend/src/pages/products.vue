<template>
  <v-container fluid>
    <h1>Produtos!</h1>
    <v-data-table
      :headers="headers"
      :items="products"
      :loading="loading"
      show-expand
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
          <create-dialog v-slot:default="{ props: activatorProps }" @create="fetchProducts">
            <v-btn
              v-if="!mobile"
              color="success"
              variant="outlined"
              text="Criar Produto"
              append-icon="mdi-plus"
              v-bind="activatorProps"
            ></v-btn>
            <v-btn
              v-else
              color="success"
              text="Criar Produto"
              variant="outlined"
              size="small"
              icon="mdi-plus"
              v-bind="activatorProps">
            </v-btn>
          </create-dialog>
        </v-toolbar>
      </template>

      <template v-slot:expanded-row="{ columns, item }">
        <tr>
          <td :colspan="columns.length" class="text-body-1">
            {{ item.description }}
          </td>
        </tr>
      </template>

      <template v-slot:item.actions="{ item }">

       <v-container>
         <v-row>
           <v-col cols="3">
             <edit-dialog v-slot:default="{ props: activatorProps }" @remove="fetchProducts">
               <v-btn icon class="mx-2" size="small" variant="flat" v-bind="activatorProps">
                 <v-icon
                   size="small">
                   mdi-pencil
                 </v-icon>
                 <v-tooltip
                   activator="parent"
                   location="top"
                 >Editar
                 </v-tooltip>
               </v-btn>
             </edit-dialog>
           </v-col>
           <v-col cols="3">
             <remove-dialog v-slot:default="{ props: activatorProps }" @remove="fetchProducts">
               <v-btn icon class="mx-2" size="small" variant="flat" v-bind="activatorProps">
                 <v-icon
                   size="small">
                   mdi-delete
                 </v-icon>
                 <v-tooltip
                   activator="parent"
                   location="top"
                 >Remover
                 </v-tooltip>
               </v-btn>
             </remove-dialog>
           </v-col>
         </v-row>
       </v-container>

      </template>

    </v-data-table>
  </v-container>
</template>

<script setup>

// localhost:8080/api/products
import {onMounted, ref} from 'vue'
import {useDisplay} from 'vuetify'
import CreateDialog from "../components/products/create-dialog.vue";
import EditDialog from "../components/products/edit-dialog.vue";
import RemoveDialog from "../components/products/remove-dialog.vue";
import httpService from "../api/HttpService";

const {mobile} = useDisplay()
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
    align: 'center',
    sortable: false,
    key: 'actions'
  }
])

const products = ref([])

const fetchProducts = async () => {
  httpService.get('products').then(response => {
    loading.value = true
    products.value = response.data;
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
