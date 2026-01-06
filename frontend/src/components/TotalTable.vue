<template>
  <v-card v-if="!loading">
    <v-table>
      <thead>
        <tr>
          <th>Date</th>
          <th>Total Amount</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="row in result" :key="row.date">
          <td>{{ row.day }}</td>
          <td>{{ row.totalAmount }}</td>
        </tr>
      </tbody>
    </v-table>
  </v-card>

  <v-progress-linear v-else indeterminate />
</template>

<script setup>

//Properties for the table.
defineProps({
  result: Array,
  loading: Boolean,
})
</script>
