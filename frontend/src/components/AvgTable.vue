<template>
  <v-card v-if="!loading" class="pa-4">
    <h3 class="text-h6 mb-2">Average Amount</h3>

    <div class="text-h4 font-weight-bold">
      {{ formattedAvg }}
    </div>

  </v-card>

</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  result: {
    type: Object,
    default: null,
  },
  loading: Boolean,
})

const formattedAvg = computed(() => {
  if (!props.result?.avgAmount) return '-'
  return `$${props.result.avgAmount}`
})
</script>
