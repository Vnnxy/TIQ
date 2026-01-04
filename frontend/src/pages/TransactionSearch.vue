<template>
  <v-container>
    <h1 class="text-h5 mb-4">Transaction Search</h1>

    <!-- Search selector -->
    <v-card class="mb-4">
      <v-card-text>

        <v-select
          v-model="mode"
          :items="modes"
          label="Search type"
        />

        <v-row v-if="mode === 'average'" class="mt-4">
            <v-col cols="12" md="4">
                <v-text-field v-model="avgParams.city" label="City" />
            </v-col>
            <v-col cols="12" md="4">
                <v-text-field v-model.number="avgParams.year" label="Year" type="number" />
            </v-col>
            <v-col cols="12" md="4">
                <v-text-field v-model.number="avgParams.month" label="Month" type="number" />
            </v-col>
        </v-row>

        <v-row v-if="mode === 'total'" class="mt-4">
            <v-col cols="12" md="4">
                <v-text-field v-model="totalParams.state" label="State" />
            </v-col>
            <v-col cols="12" md="4">
                <v-text-field v-model.number="totalParams.month" label="Month" type="number" />
            </v-col>
            <v-col cols="12" md="4">
                <v-text-field v-model.number="totalParams.batchSize" label="Batch size" type="number" />
            </v-col>
            <v-col cols="12" md="4">
                <v-text-field v-model.number="totalParams.offset" label="Offset" type="number" />
            </v-col>
        </v-row>

        <v-row v-if="mode === 'tb'" class="mt-4">
            <v-col cols="12" md="3">
                <v-text-field
                v-model.number="tbForm.year"
                label="Year"
                type="number"
                />
            </v-col>

            <v-col cols="12" md="3">
                <v-text-field
                v-model.number="tbForm.previousYears"
                label="Previous years"
                type="number"
                hint="Optional"
                persistent-hint
                />
            </v-col>

            <v-col cols="12" md="3">
                <v-select
                v-model="tbForm.dir"
                :items="['top', 'bottom']"
                label="Direction"
                />
            </v-col>

            <v-col cols="12" md="3">
                <v-text-field
                v-model.number="tbForm.limit"
                label="Limit"
                type="number"
                />
            </v-col>

            <v-col cols="12" md="4">
                <v-text-field v-model.number="tbForm.offset" label="Offset" type="number" />
            </v-col>
        </v-row>



        <v-btn
          class="mt-3"
          color="primary"
          @click="search"
        >
          Search
        </v-btn>
      </v-card-text>
    </v-card>

    <!-- Dynamic result -->
    <component
      :is="currentComponent"
      :result="result"
      :loading="loading"
    />
  </v-container>
</template>


<script setup>
import { ref, computed, reactive } from 'vue'

import AvgTable from '@/components/AvgTable.vue'
import TotalTable from '@/components/TotalTable.vue'
import TBTable from '@/components/TBTable.vue'

import {
  getTransactionAverage,
  getTransactionTotal,
  getTransactionTB,
} from '@/api/transactionsApi'

const mode = ref('average')
const loading = ref(false)
const result = ref(null)

const modes = [
  { title: 'Average', value: 'average' },
  { title: 'Total', value: 'total' },
  { title: 'Top / Bottom', value: 'tb' },
]

const avgParams = ref({
  city: '',
  year: null,
  month: null,
})

const totalParams = ref({
  state: '',
  month: null,
  batchSize: 10,
  offset: 0,
})

const tbForm = reactive({
  year: null,
  previousYears: null,
  dir: 'top',
  limit: 10,
  offset:0,
})

const tbParams = computed(() => {
  if (!tbForm.year) return null

  const endYear = tbForm.year
  const startYear = tbForm.previousYears
    ? endYear - tbForm.previousYears
    : endYear

  return {
    startYear,
    endYear,
    dir: tbForm.dir,
    limit: tbForm.limit,
    offset: tbForm.offset,
  }
})


const currentComponent = computed(() => {
  switch (mode.value) {
    case 'average': return AvgTable
    case 'total': return TotalTable
    case 'tb': return TBTable
  }
})

async function search() {
  loading.value = true
  result.value = null

  if (mode.value === 'average') {
    const [, data] = await getTransactionAverage(avgParams.value)
    result.value = data
  }

  if (mode.value === 'total') {
    const [, data] = await getTransactionTotal(totalParams.value)
    result.value = data
  }

  if (mode.value === 'tb') {
    const [, data] = await getTransactionTB(tbParams.value)
    result.value = data
  }

  loading.value = false
}

</script>
