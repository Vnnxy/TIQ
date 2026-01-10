<template>
  <v-container>
    <h1 class="text-h5 mb-4">Summaries</h1>

    <!-- Summary type selector -->
    <v-card class="mb-4">
      <v-card-text>
        <v-select
          v-model="summaryType"
          :items="summaryTypes"
          label="Summary type"
          density="comfortable"
        />
      </v-card-text>
    </v-card>

    <!-- Filters (example, optional) -->
    <!-- Filters -->
<v-card class="mb-4">
  <v-card-text>
    <v-form ref="form">

      <!-- CLIENT FILTERS -->
      <v-row v-if="summaryType === 'clients'">
        <v-col cols="12" md="3">
          <v-text-field
            v-model="clientFilter.year"
            label="Year"
            type="number"
          />
        </v-col>

        <v-col cols="12" md="3">
          <v-text-field
            v-model="clientFilter.month"
            label="Month"
            type="number"
          />
        </v-col>

        <v-col cols="12" md="3">
          <v-select
            v-model="clientFilter.view"
            :items="['total', 'avg', 'count']"
            label="Sort by"
          />
        </v-col>

        <v-col cols="12" md="3">
              <v-text-field
                v-model.number="clientFilter.limit"
                label="Limit"
                type="number"
              />
            </v-col>
        <v-col cols="12" md="3">
            <v-text-field
            v-model.number="clientFilter.offset"
            label="Offset"
            type="number"
            />
        </v-col>
      </v-row>

      <!-- MERCHANT FILTERS -->
      <v-row v-if="summaryType === 'merchants'">
        <v-col cols="12" md="3">
          <v-text-field v-model="merchantFilter.year" label="Year" type="number" />
        </v-col>

        <v-col cols="12" md="3">
          <v-text-field v-model="merchantFilter.month" label="Month" type="number" />
        </v-col>

        <v-col cols="12" md="3">
          <v-text-field v-model="merchantFilter.state" label="State" />
        </v-col>

        <v-col cols="12" md="3">
          <v-text-field v-model="merchantFilter.city" label="City" />
        </v-col>

        <v-col cols="12" md="3">
              <v-text-field
                v-model.number="merchantFilter.limit"
                label="Limit"
                type="number"
              />
            </v-col>
            <v-col cols="12" md="3">
              <v-text-field
                v-model.number="merchantFilter.offset"
                label="Offset"
                type="number"
              />
            </v-col>
      </v-row>

      <!-- STATE FILTERS -->
      <v-row v-if="summaryType === 'states'">
        <v-col cols="12" md="3">
          <v-text-field v-model="stateFilter.year" label="Year" type="number" />
        </v-col>

        <v-col cols="12" md="3">
          <v-text-field v-model="stateFilter.month" label="Month" type="number" />
        </v-col>
      </v-row>

      <!-- YEAR FILTERS -->
      <v-row v-if="summaryType === 'years'">
        <v-col cols="12" md="3">
          <v-text-field v-model="yearFilter.start" label="Start year" type="number" :rules="[rules.required, rules.startBeforeEnd]"/>
        </v-col>

        <v-col cols="12" md="3">
          <v-text-field v-model="yearFilter.end" label="End year" type="number" :rules="[rules.required, rules.endAfterStart]" />
        </v-col>

        <v-col cols="12" md="3">
          <v-select
            v-model="yearFilter.detail"
            :items="['quarter', 'month']"
            label="Detail"
          />
        </v-col>

      </v-row>

      <!-- LOAD BUTTON -->
      <v-row class="mt-4">
        <v-col cols="12" md="3">
          <v-btn color="primary" @click="loadSummary" :disabled="!formValid">
            Load
          </v-btn>
        </v-col>
      </v-row>

    </v-form>
  </v-card-text>
</v-card>


    <!-- Result table -->
    <v-card>
      <v-card-text>
         <v-table v-if="summaryType === 'clients' || summaryType === 'merchants'">
            <thead>
                <tr>
                <th v-for="h in headers" :key="h.key">
                    {{ h.title }}
                </th>
                </tr>
            </thead>

            <tbody>
                <tr v-for="row in items" :key="row.id">
                <td v-for="h in headers" :key="h.key">
                    {{ row[h.key] }}
                </td>
                </tr>
            </tbody>
            </v-table>
        <v-data-table
            v-else
            :headers="headers"
            :items="items"
            :loading="loading"
            >
            <template #item.period="{ item }">
                {{ formatPeriod(item.period) }}
            </template>
        </v-data-table>

        <v-alert
          v-if="error"
          type="error"
          class="mt-4"
        >
          {{ error }}
        </v-alert>
      </v-card-text>
    </v-card>
  </v-container>
</template>


<script setup>
import { ref, computed, watch } from 'vue'
import {
  getClientsSummary,
  getMerchantsSummary,
  getStateSummary,
  getYearSummary
} from '@/api/summariesApi'

const summaryTypes = [
  { title: 'Clients', value: 'clients' },
  { title: 'Merchants', value: 'merchants' },
  { title: 'States', value: 'states' },
  { title: 'Years', value: 'years' }
]

const summaryType = ref('clients')
const items = ref([])
const loading = ref(false)
const error = ref(null)

// Rules for input validation.
const rules = {
  required: value => !!value || 'Field is required',
  startBeforeEnd: value => {
    if (!value || !yearFilter.value.end) return true; // Don't show error if one is empty
    return Number(value) <= Number(yearFilter.value.end) || 'Start year must be before or equal to End year';
  },
  
  endAfterStart: value => {
    if (!value || !yearFilter.value.start) return true;
    return Number(value) >= Number(yearFilter.value.start) || 'End year must be after or equal to Start year';
  }
}

const formValid = computed(() => {
  if (summaryType.value === 'years') {
    const start = Number(yearFilter.value.start)
    const end = Number(yearFilter.value.end)
    return !!start && !!end && start <= end
  }
  return true
})

function formatPeriod(period) {
  if (!period) return ''

  const date = new Date(period)
  const year = date.getFullYear()
  var month = date.getMonth();
  const quarter = Math.floor(month / 3) + 1

  if (yearFilter.value.detail === 'quarter') {
    return `Q${quarter} ${year}`
  }
  month = month+1;
  return `${month.toString()} ${year.toString()}`
}


const clientFilter = ref({  
  year: null,
  month: null,
  view: null, //total, avg, count
  limit:20,
  offset:null
})
const merchantFilter = ref({
  year: null,
  month: null,
  state: null,
  city: null,
  limit:20,
  offset:null
})
const stateFilter = ref({
  year: null,
  month: null,
})
const yearFilter = ref({
  start: null,
  end: null,
  detail: 'quarter',
})

/* ---------- Dynamic table headers ---------- */
const headers = computed(() => {
  switch (summaryType.value) {
    case 'clients':
      return [
        { title: 'Client ID', key: 'clientId' },
        { title: 'Total', key: 'total' },
        { title: 'Transactions', key: 'transactionCount' },
        { title: 'Average', key: 'averageTransaction' }
      ]

    case 'merchants':
      return [
        { title: 'Merchant ID', key: 'merchant_id' },
        { title: 'City', key: 'city' },
        { title: 'State', key: 'state' },
        { title: 'Total', key: 'total' },
        { title: 'Transactions', key: 'transactions' }
      ]

    case 'states':
      return [
        { title: 'State', key: 'merchantState' },
        { title: 'Total', key: 'total' }
      ]

    case 'years':
      return [
        { title: 'Period', key: 'period' },
        { title: 'Average', key: 'average' },
        { title: 'Count', key: 'count' },
        { title: 'Total', key: 'total' }

      ]

    default:
      return []
  }
})

/* ---------- API dispatcher ---------- */
async function loadSummary () {
  loading.value = true
  error.value = null

  try {
    let result

    switch (summaryType.value) {
      case 'clients':
        result = await getClientsSummary(clientFilter.value)
        break

      case 'merchants':
        result = await getMerchantsSummary(merchantFilter.value)
        break

      case 'states':
        result = await getStateSummary(stateFilter.value)
        break

      case 'years':
        result = await getYearSummary(yearFilter.value)
        break
    }

    const [err, data] = result
    if (err) throw err

    items.value = data
  } catch (e) {
    error.value = 'Failed to load summary'
    items.value = []
  } finally {
    loading.value = false
  }
}
    
watch(summaryType, () => {
  items.value = []
})


/* Reload automatically when type changes */
watch(summaryType, (newType) => {
  if (newType !== 'years') {
    loadSummary()
  }
}, { immediate: true })
</script>


