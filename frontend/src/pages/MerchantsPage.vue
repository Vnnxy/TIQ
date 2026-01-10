<template>
  <v-container>
    <h1 class="text-h5 mb-4">Merchants</h1>

    <v-card>
      <v-card-text>
        <v-form ref="idRef">
          <v-row class="mt-4">
            <v-col cols="12" md="3">
              <v-text-field
                v-model.number="idSearchParam.id"
                label="Merchant ID"
                type="number"
                clearable
              />
            </v-col>
            <v-col cols="12" md="3">
              <v-text-field
                v-model.number="merchantsFilters.limit"
                label="Limit"
                type="number"
              />
            </v-col>
            <v-col cols="12" md="3">
              <v-text-field
                v-model.number="merchantsFilters.page"
                label="Offset"
                type="number"
              />
            </v-col>
          </v-row>
        </v-form>
        
        <v-row>
          <v-col cols="12" md="3" class="d-flex align-end">
            <v-btn color="primary" @click="search" class="mr-2">
              Search
            </v-btn>
            <v-btn variant="outlined" class="mr-2" @click="clearAndLoad">
              Clear
            </v-btn>
            <v-btn variant="outlined" color="red" class="mr-2" @click="handleDelete" :disabled="!hasResults">
              Delete
            </v-btn>
            <v-btn variant="outlined" color="green" @click="openMerchantMenu">
              Create
            </v-btn>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <v-card class="mt-4">
      
      <v-table fixed-header height="400">
        <thead>
          <tr>
            <th>Merchant ID</th>
            <th>City</th>
            <th>State</th>
            <th></th>
          </tr>
        </thead>

        <tbody>
          <tr v-if="!result || result.length === 0">
            <td colspan="4" class="text-center text-grey">
              {{ loading ? 'Loading...' : 'No merchants found' }}
            </td>
          </tr>
          <tr v-for="merchant in result" :key="merchant.id">
            <td>{{ merchant.id }}</td>
            <td>{{ merchant.merchantCity }}</td>
            <td>{{ merchant.merchantState }}</td>
            <td>
              <v-btn 
                size="small" 
                icon="mdi-pencil" 
                variant="text"
                @click="openMerchantMenu(merchant)"
              />
            </td>
          </tr>
        </tbody>
      </v-table>

      <v-progress-linear
        v-if="loading"
        indeterminate
      />
    </v-card>

    <merchant-menu 
      v-model="showForm"
      :merchant="selectedMerchant"
      @saved="loadAllMerchants"
    />
  </v-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getMerchants, getMerchant, deleteMerchant} from '@/api/merchantsApi'
import MerchantMenu from '@/components/MerchantMenu.vue'

// Loading bar vuetify
const loading = ref(false)
// Result from calling the API
const result = ref([])
// Edit form flag
const showForm = ref(false)
// Selects the current merchant for editing
const selectedMerchant = ref(null)
// Form reference for the Merchant Id
const idRef = ref(null)

// Parameters for the search by id
const idSearchParam = ref({
  id: null,
})

const merchantsFilters = ref({
  limit: 20,
  page: 0
})

// Rules for input validation.
const rules = {
  required: value => !!value || 'Field is required',
}

/**
 * Loads all merchants (called on mount and after actions)
 */
async function loadAllMerchants() {
  loading.value = true
  result.value = []
  
  try {
    const [error, data] = await getMerchants(merchantsFilters.value)
    if (error) {
      console.error('Error loading merchants:', error)
      alert('Failed to load merchants')
    } else {
      result.value = data
    }
  } catch (err) {
    console.error('Unexpected error:', err)
  } finally {
    loading.value = false
  }
}

/**
 * Searches for a specific merchant by ID
 */
async function search() {
  // If no ID entered, load all merchants
  if (!idSearchParam.value.id) {
    loadAllMerchants()
    return
  }

  // Validate the form
  const { valid } = await idRef.value.validate()
  if (!valid) return

  loading.value = true
  result.value = []
  
  try {
    const [error, data] = await getMerchant(idSearchParam.value.id)
    if (error) {
      console.error('Error loading merchant:', error)
      alert('Merchant not found')
      result.value = []
    } else if(data){
      // Wrap single merchant in array for table display
      result.value = [data]
    }
    else{
        result.value=[]
    }
  } catch (err) {
    console.error('Unexpected error:', err)
  } finally {
    loading.value = false
  }
}
// Computed property to check if there are results to delete
const hasResults = computed(() => {
  return result.value.length === 1 && 
         idSearchParam.value.id && 
         result.value[0].id === idSearchParam.value.id
})

/**
 * Clears the search input and reloads all merchants
 */
function clearAndLoad() {
  idSearchParam.value.id = null
  if (idRef.value) {
    idRef.value.resetValidation()
  }
  loadAllMerchants()
}

/**
 * Deletes the merchant after showing a confirmation message.
 */
async function handleDelete() {
  if (!hasResults.value) {
    alert('No transactions to delete')
    return
  }

  const confirmMessage = `Are you sure you want to delete merchant ID: ${idSearchParam.value.id}?`
  
  if (confirm(confirmMessage)) {
    loading.value = true
    try {
      const [error] = await deleteMerchant(idSearchParam.value.id)
      if (error) {
        console.error('Error deleting merchant:', error)
        alert('Failed to delete merchant')
      } else {
        alert('Merchant deleted successfully')
        clearAndLoad()
      }
    } catch (err) {
      console.error('Unexpected error:', err)
      alert('An error occurred while deleting')
    } finally {
      loading.value = false
    }
  }
}

/**
 * Opens MerchantMenu.vue for creating/editing data
 * @param merchant Merchant we want to edit, null if we are creating.
 */
function openMerchantMenu(merchant = null) {
  selectedMerchant.value = merchant || null 
  showForm.value = true

}

// Load all merchants when component mounts
onMounted(() => {
  loadAllMerchants()
})
</script>