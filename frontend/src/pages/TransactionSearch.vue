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
        <v-form v-if="mode === 'average'" ref="avgRef" >
            <v-row  class="mt-4">
            <v-col cols="12" md="4">
                <v-text-field v-model="avgParams.city" label="City" :rules="[rules.required]">
                  <template #append-inner>
                    <v-btn icon variant="text" @click="openCityMap" :disabled="!avgParams.city">
                      <v-icon>mdi-map</v-icon>
                    </v-btn>
                  </template>
                </v-text-field>
            </v-col>
            <v-col cols="12" md="4">
                <v-text-field v-model.number="avgParams.year" label="Year" type="number" :rules="[rules.required]" />
            </v-col>
            <v-col cols="12" md="4">
                <v-text-field v-model.number="avgParams.month" label="Month" type="number" :rules="[rules.month]" hint="Optional" persistent-hint />
            </v-col>
            </v-row>
        </v-form>

        <v-form v-if="mode === 'total'" ref="totalRef">
            <v-row  class="mt-4">
                <v-col cols="12" md="4">
                    <v-text-field v-model="totalParams.state" label="State" :rules="[rules.required]"/>
                </v-col>
                <v-col cols="12" md="4">
                    <v-text-field v-model.number="totalParams.month" label="Month" type="number" :rules="[rules.month, rules.required]"/>
                </v-col>
                <v-col cols="12" md="4">
                    <v-select v-model="totalParams.batchSize" :items="[10,20,50,100]" label="Batch size" type="number" />
                </v-col>
                <v-col cols="12" md="4">
                    <v-text-field v-model.number="totalParams.offset" label="Offset" type="number" hint="Optional"
                    persistent-hint/>
                </v-col>
            </v-row>
        </v-form>

        <v-form v-if="mode === 'tb'"  ref="tbRef">
            <v-row class="mt-4">
                <v-col cols="12" md="3">
                    <v-text-field
                    v-model.number="tbForm.year"
                    label="Year"
                    type="number"
                    :rules="[rules.required]"
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
                    :items="['Top', 'Bottom']"
                    label="Direction"
                    />
                </v-col>

                <v-col cols="12" md="3">
                    <v-text-field
                    v-model.number="tbForm.limit"
                    label="Number of Transactions"
                    type="number"
                    :rules="[rules.required]"
                    />
                </v-col>

                <v-col cols="12" md="4">
                    <v-text-field v-model.number="tbForm.offset" label="Offset" type="number" hint="Optional" persistent-hint />
                </v-col>
            </v-row>

        </v-form>
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

  <CityMapModal
    v-if="showMap"
    :lat="cityCoords.lat"
    :lng="cityCoords.lng"
    @close="showMap=false"
  />
</template>


<script setup>
import { ref, computed, reactive } from 'vue'

import AvgTable from '@/components/AvgTable.vue'
import TotalTable from '@/components/TotalTable.vue'
import TBTable from '@/components/TBTable.vue'

import CityMapModal from "@/components/CityMapModal.vue";
import { geocodeCity } from "@/services/geocoding.js";

import {
  getTransactionAverage,
  getTransactionTotal,
  getTransactionTB,
} from '@/api/transactionsApi'

// The current mode
const mode = ref('average')
// Loading var for vuetify
const loading = ref(false)
// Result from the api
const result = ref(null)
// Reference for the average form
const avgRef = ref(null)
// Reference for the total form
const totalRef = ref(null)
// Reference for the top bottom form
const tbRef = ref(null)

// Modes
const modes = [
  { title: 'Average', value: 'average' },
  { title: 'Total', value: 'total' },
  { title: 'Top / Bottom', value: 'tb' },
]
// Parameters for the get average api call
const avgParams = ref({
  city: '',
  year: null,
  month: null,
})
// Parameters for the get total api call
const totalParams = ref({
  state: '',
  month: null,
  batchSize: 10,
  offset: 0,
})
// Definition for the parameters for the get average api call
const tbForm = reactive({
  year: null,
  previousYears: null,
  dir: 'top',
  limit: 10,
  offset:0,
})
/**
 * Parameters for the top bottom. We handle the previous year conversion by doing start year = Year-previous years
 * or Year,Year if we only need one year.
 */
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
// rules for input validation
 const rules = {
    required: value => !!value || 'Field is required',
    month: value =>
    (value === null || value === undefined || value === '') ||
    (value >= 1 && value <= 12) ||
    'Enter a valid month'
  }
// Returns the table we want to load depending on the current mode.
const currentComponent = computed(() => {
  switch (mode.value) {
    case 'average': return AvgTable
    case 'total': return TotalTable
    case 'tb': return TBTable
  }
})
// References for the city map modal
const showMap = ref(false)
const cityCoords = ref({ lat: 0, lng: 0})

/**
 * Function that retrieves the dsta from the api depnding on the specified mode.
 */
async function search() {
    let formRef
    if (mode.value === 'average') formRef = avgRef
    else if (mode.value === 'total') formRef = totalRef
    else if (mode.value === 'tb') formRef = tbRef

    const { valid } = await formRef.value.validate()
    if (!valid) return
    loading.value = true
    result.value = null
    if (mode.value === 'average') {
        const [, data] = await getTransactionAverage(avgParams.value)
        result.value = data
    }

    if (mode.value === 'total') {
        const [, data] = await getTransactionTotal(totalParams.value)
        if(!data) result.value = []
        else result.value = data
    }

    if (mode.value === 'tb') {
        const [, data] = await getTransactionTB(tbParams.value)
        result.value = data
    }

    loading.value = false
}

/**
 * function to show the city map modal
 */
async function openCityMap() {
    const coords = await geocodeCity(avgParams.value.city) // should replace with a back-end api fetch
    cityCoords.value = { lat: coords.lat, lng: coords.lon } // translate nominatim "lon" to leaflet "lng"
    showMap.value = true
}

</script>
