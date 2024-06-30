<template>
  <v-app>
    <v-navigation-drawer v-model="drawer">
      <v-list
        :lines="false"
        nav
      >
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          :value="item"
          :to="item.to"
          color="primary"
        >
          <template v-slot:prepend>
            <v-icon :icon="item.icon"></v-icon>
          </template>

          <v-list-item-title
            v-text="item.text"
          >
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script setup>
import {ref} from 'vue'

const drawer = ref(null)

const items = [
  { text: 'Inicio', icon: 'mdi-home', to: '/admin' },
  { text: 'Produtos', icon: 'mdi-shopping', to: '/admin/products' },
  { text: 'Usuários', icon: 'mdi-account-multiple', to: '/admin/users' },
  { text: 'Compras', icon: 'mdi-cart-variant', to: '/admin/orders' },
  { text: 'Gerar Compra', icon: 'mdi-cart-plus', to: '/admin/create-order' }
]
</script>
