<template>
  <v-container fluid>
    <h1>Usuários</h1>
    <v-data-table
      :headers="headers"
      :items="users"
      :loading="loading"
      show-expand
    >

      <template v-slot:loading>
        <v-skeleton-loader type="table-row@5"></v-skeleton-loader>
      </template>

      <template v-slot:item.actions="{ item }">

        <v-container>
          <v-row class="align-center justify-center">
            <v-btn
              color="error"
              variant="flat"
              @click="deleteUser(item)"
            >
              DELETAR
            </v-btn>
          </v-row>
        </v-container>

      </template>


      <template v-slot:top>
        <v-toolbar class="px-4">
          <v-toolbar-title>Usuários</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn class="mx-2"
                 v-if="!mobile"
                 :disabled="loading"
                 @click="fetchUsers"
                 text="Atualizar"
                 variant="flat"
                 append-icon="mdi-refresh">
          </v-btn>
          <v-btn v-else
                 :disabled="loading"
                 @click="fetchUsers"
                 variant="elevated"
                 size="small"
                 icon="mdi-refresh"
          >
          </v-btn>

          <create-user-dialog v-slot:default="{ props: activatorProps }" @create="fetchUsers">
            <v-btn
              v-if="!mobile"
              color="success"
              variant="outlined"
              text="Criar usuário"
              append-icon="mdi-plus"
              v-bind="activatorProps"
            ></v-btn>
            <v-btn
              v-else
              color="success"
              text="Criar usuário"
              variant="outlined"
              size="small"
              icon="mdi-plus"
              v-bind="activatorProps">
            </v-btn>
          </create-user-dialog>

        </v-toolbar>
      </template>

    </v-data-table>
  </v-container>
</template>

<script setup>

import {onMounted, ref} from 'vue'
import {useDisplay} from 'vuetify'
import CreateUserDialog from "../../components/users/create-user-dialog.vue";
import httpService from "../../api/HttpService";

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
    key: 'username'
  },
  {
    title: 'ações',
    align: 'center',
    sortable: false,
    key: 'actions'
  }
])

const users = ref([])

const fetchUsers = async () => {
  loading.value = true
  httpService.get('users').then(response => {
    users.value = response.data;
    loading.value = false
  }).catch(error => {
    console.error('Erro ao buscar usuários:', error);
    loading.value = false;
  });
};


function deleteUser(id) {
  httpService.delete(`users/${id}`).then(() => {
    fetchUsers()
  }).catch(error => {
    console.error('Erro ao deletar usuário:', error);
  });
}

onMounted(fetchUsers)

</script>

<route lang="yaml">
meta:
  layout: dashboard
</route>
