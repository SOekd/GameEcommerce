<template>

  <v-container fluid>
    <v-container>
      <h1 class="mb-3">Compras</h1>
      <v-row
        align="center"
      >
        <v-col cols="6">
          <v-combobox
            v-model="states"
            multiple
            label="Estados"
            :items="['Pendente de Pagamento', 'Completo', 'Expirado', 'Reembolsando', 'Reembolsado', 'Aguardando Entrega', 'Entregue', 'Cancelado']"
            variant="outlined"
          ></v-combobox>
        </v-col>
        <v-col cols="6">
          <v-text-field
            v-model="player"
            label="Jogador"
            clearable
            variant="outlined"
          ></v-text-field>
        </v-col>
      </v-row>

    </v-container>
    <v-data-table-server
      :headers="headers"
      :items="orders"
      :loading="loading"
      :search="search"
      :items-length="total"
      show-expand
      @update:options="loadOrders"
    >
      <template v-slot:expanded-row="{ columns, item }">
        <v-table>
          <thead>
          <tr>
            <th class="text-left">
              Quantidade
            </th>
            <th class="text-left">
              Produtos
            </th>
          </tr>
          </thead>
          <tbody>
          <tr
            v-for="product in item.products"
            :key="product.product.name">
            <td>{{ product.amount }}</td>
            <td>{{ product.product.name }}</td>
          </tr>
          </tbody>
        </v-table>
      </template>

      <template v-slot:item.actions="{ item }">

        <v-container>
          <v-row class="align-center justify-center">
            <v-btn
              :disabled="item.state !== 'Aguardando Entrega'"
              color="success"
              variant="outlined"
              @click="deliver(item)"
            >
              Entregar
            </v-btn>
          </v-row>
        </v-container>

      </template>

      <template v-slot:item.link="{ item }">

        <a :href="getLink(item)">{{ item.link }}</a>

      </template>


    </v-data-table-server>
  </v-container>
</template>

<script setup>

import {ref} from "vue";
import httpService from "@/api/HttpService";

const headers = ref([
  {
    title: 'id',
    align: 'center',
    sortable: true,
    key: 'id'
  },
  {
    title: 'jogador',
    align: 'center',
    sortable: true,
    key: 'playerName'
  },
  {
    title: 'link',
    align: 'center',
    sortable: true,
    key: 'link'
  },
  {
    title: 'estado',
    align: 'center',
    sortable: true,
    key: 'state'
  },
  {
    title: 'preço',
    align: 'center',
    sortable: true,
    key: 'price'
  },
  {
    title: 'confirmar',
    align: 'center',
    sortable: false,
    key: 'actions'
  }
])

const orders = ref([])
const loading = ref(true)
const search = ref('')
const total = ref(0)
const states = ref([])
const player = ref('')

function loadOrders({page, itemsPerPage, sortBy}) {
  loading.value = true

  const searchRequest = {
    search: player.value,
    page: page,
    itemsPerPage: itemsPerPage,
    sortColumn: 'id',
    sortDirection: 'ASC',
    states: states.value.map(rollbackStage)
  }

  if (sortBy.length) {
    const sortKey = sortBy[0].key
    const sortOrder = sortBy[0].order

    Object.assign(searchRequest, {
      sortColumn: sortKey,
      sortDirection: sortOrder === 'asc' ? 'ASC' : 'DESC'
    })
  }

  httpService.post("/orders/search", JSON.stringify(searchRequest))
    .then(response => {
      orders.value = response.data.orders.map(order => {
        Object.assign(order, {
          state: translateStage(order.state)
        });
        return order; // Retorne o objeto `order` modificado
      });

      total.value = response.data.total
      loading.value = false
    })
}

function getLink(item) {
  return "http://localhost:3000/pay/" + item.link
}

function translateStage(state) {
  switch (state) {
    case 'PENDING_PAYMENT':
      return 'Pendente de Pagamento'
    case 'COMPLETED':
      return 'Completo'
    case 'EXPIRED':
      return 'Expirado'
    case 'REFUNDING':
      return 'Reembolsando'
    case 'REFUNDED':
      return 'Reembolsado'
    case 'WAITING_DELIVERY':
      return 'Aguardando Entrega'
    case 'DELIVERED':
      return 'Entregue'
    case 'CANCELLED':
      return 'Cancelado'
  }
}

function rollbackStage(state) {
  switch (state) {
    case 'Pendente de Pagamento':
      return 'PENDING_PAYMENT'
    case 'Completo':
      return 'COMPLETED'
    case 'Expirado':
      return 'EXPIRED'
    case 'Reembolsando':
      return 'REFUNDING'
    case 'Reembolsado':
      return 'REFUNDED'
    case 'Aguardando Entrega':
      return 'WAITING_DELIVERY'
    case 'Entregue':
      return 'DELIVERED'
    case 'Cancelado':
      return 'CANCELLED'
  }
}

function deliver(item) {
  httpService.put("/orders/deliver/" + item.id)
    .then(() => {
      refresh()
    })
}

watch(player, () => {
  refresh()
})

watch(states, () => {
  refresh()
})

function refresh() {
  search.value = String(Date.now())
}

</script>

<route lang="yaml">
meta:
  layout: dashboard
</route>
