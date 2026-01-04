<template>
  <v-card v-if="!loading">
    <v-table>
      <thead>
        <tr>
          <th>Date</th>
          <th>Amount</th>
          <th>City</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="row in result" :key="row.timestamp">
          <td>{{ row.timestamp }}</td>
          <td>{{ row.maxAmount }}</td>
          <td>{{ row.location }}</td>
        </tr>
      </tbody>
    </v-table>
  </v-card>

  <v-progress-linear v-else indeterminate />
</template>

<script setup>
defineProps({
  result: Array,
  loading: Boolean,
})
</script>
