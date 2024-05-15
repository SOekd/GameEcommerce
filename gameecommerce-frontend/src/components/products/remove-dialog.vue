<template>
  <div class>
    <v-dialog
      v-model="dialog"
      max-width="600"
    >
      <template v-slot:activator="{ props: activatorProps }">
        <slot :props="activatorProps"></slot>
      </template>

      <v-card
        prepend-icon="mdi-archive-arrow-down"
        title="Realmente deseja remover o produto?"
      >

        <v-card-text>
          <v-alert
            text="Ocorreu um erro ao remover o produto! :("
            type="error"
            border="start"
            variant="elevated"
            closable
            class="ma-3"
            v-model="alert"
          ></v-alert>
        </v-card-text>

        <v-card-actions>
          <v-btn
            text="Cancelar"
            color="error"
            variant="outlined"
            @click="dialog = false"
          ></v-btn>

          <v-btn
            color="danger"
            text="Remover"
            :loading="loading"
            variant="flat"
            @click="removeProduct"
          ></v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import httpService from "@/api/HttpService";

const emit = defineEmits(['remove'])

const props = defineProps({
  product: [Object, null]
})


const dialog = ref(false)

const loading = ref(false)
const alert = ref(false)

function removeProduct() {
  loading.value = true
  console.log("Product: ", props.product)
  httpService.delete(`products/${props.product.id}`).then(response => {

    console.log("Response: ", response)
    dialog.value = false
    alert.value = false
    loading.value = false
    emit('remove')
  })
    .catch(error => {

      console.error("Error: ", error.message)
      loading.value = false
      alert.value = true
    })

}

</script>
