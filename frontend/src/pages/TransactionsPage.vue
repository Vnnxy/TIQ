<template>
  <v-container>
    <h1 class="text-h5 mb-4">Transactions</h1>

    <v-card>
      <v-card-text>
        <v-select
          v-model="mode"
          :items="modes"
          label="Search type"
        />
          
        <v-form ref="clientRef" v-if="mode === 'client'">
          <v-row class="mt-4">
            <v-col cols="12" md="3">
              <v-text-field
                v-model.number="transactionParams.clientId"
                label="Client ID"
                type="number"
                clearable
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12" md="3">
              <v-text-field
                v-model.number="transactionParams.year"
                label="Year"
                type="number"
                clearable
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12" md="3">
              <v-text-field
                v-model.number="transactionParams.month"
                label="Month"
                type="number"
                :rules="[rules.month, rules.required]"
                clearable
              />
            </v-col>
            
            <v-col cols="12" md="3">
              <v-text-field
                v-model.number="transactionParams.limit"
                label="Limit"
                type="number"
                clearable
              />
            </v-col>

            <v-col cols="12" md="3">
              <v-text-field
                v-model.number="transactionParams.offset"
                label="Offset"
                type="number"
                clearable
              />
            </v-col>
          </v-row>
        </v-form>

        <v-form v-if="mode === 'id'" ref="idRef">
          <v-row class="mt-4">
            <v-col cols="12" md="3">
              <v-text-field
                v-model.number="idSearchParam.id"
                label="Transaction ID"
                type="number"
                :rules="[rules.required]"
                clearable
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
            <v-btn 
              variant="outlined" 
              color="red" 
              class="mr-2" 
              @click="handleDelete"
              :disabled="!hasResults"
            >
              Delete
            </v-btn>
            <v-btn variant="outlined" color="green" @click="openCreateMenu">
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
            <th>ID</th>
            <th>Date</th>
            <th>Amount</th>
            <th>Client</th>
            <th>Merchant Id</th>
            <th></th>
          </tr>
        </thead>

        <tbody>
          <tr v-if="!result || result.length === 0">
            <td colspan="6" class="text-center text-grey">
              {{ loading ? 'Loading...' : 'No transactions found. Use search to find transactions.' }}
            </td>
          </tr>
          <tr v-for="tx in result" :key="tx.id">
            <td>{{ tx.id }}</td>
            <td>{{ formatDate(tx.date) }}</td>
            <td>${{ tx.amount }}</td>
            <td>{{ tx.clientId }}</td>
            <td>{{ tx.merchantId }}</td>
            <td>
              <v-btn 
                size="small" 
                icon="mdi-pencil" 
                variant="text"
                @click="openCreateMenu(tx)"
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

    <create-menu 
      v-model="showForm"
      :transaction="selectedTransaction"
      @saved="refreshResults"
    />
  </v-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { getTransactions, getTransaction, deleteTransaction, deleteTransactions } from '@/api/transactionsApi'
import CreateMenu from '@/components/CreateMenu.vue'

// Loading bar vuetify
const loading = ref(false)
// Variable containing the current mode (Search by transaction or client id)
const mode = ref('client')
// Result from calling the API
const result = ref([])
// Edit form flag
const showForm = ref(false)
// Selects the current transaction for editing
const selectedTransaction = ref(null)
// Form reference for the Transaction Id
const idRef = ref(null)
// Form reference for the Client search
const clientRef = ref(null)

// Modes 
const modes = [
  { title: 'Transaction Id', value: 'id' },
  { title: 'Client Id', value: 'client' },
]

// Parameters for the transactions.
const transactionParams = ref({
  clientId: null,
  year: null,
  month: null,
  limit: null,
  offset: 0
})

// Parameters for the search by id
const idSearchParam = ref({
  id: null
})

// Rules for input validation.
const rules = {
  required: value => !!value || 'Field is required',
  month: value =>
    (value === null || value === undefined || value === '') ||
    (value >= 1 && value <= 12) ||
    'Enter a valid month'
}

// Computed property to check if there are results to delete
const hasResults = computed(() => {
  return result.value && result.value.length > 0
})

// Formats the date 
function formatDate(dateString) {
  if (!dateString) return 'N/A'
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', { 
    year: 'numeric', 
    month: 'short', 
    day: 'numeric' 
  })
}

/**
 * Retrieves the desired transaction(s) by calling the respective api call 
 */
async function search() {
  loading.value = true
  result.value = []
  
  try {
    if (mode.value === 'id') {  
      const { valid } = await idRef.value.validate()
      if (!valid) {
        loading.value = false
        return
      }
      
      const [error, data] = await getTransaction(idSearchParam.value.id)
      if (error) {
        console.error('Error fetching transaction:', error)
        alert('Transaction not found')
        result.value = []
      } else if(data) {
        result.value = [data]
      }
      else{
        result.value=[]
      }
    } else if (mode.value === 'client') {
      const { valid } = await clientRef.value.validate()
      if (!valid) {
        loading.value = false
        return
      }
      
      const [error, data] = await getTransactions(transactionParams.value)
      if (error) {
        console.error('Error fetching transactions:', error)
        alert('Failed to fetch transactions')
        result.value = []
      } else {
        result.value = data || []
      }
    }
  } catch (error) {
    console.error('Unexpected error:', error)
    alert('An error occurred while searching')
    result.value = []
  } finally {
    loading.value = false
  }
}

/**
 * Clears the inputs and clears results
 */
function clearAndLoad() {
  transactionParams.value = {
    clientId: null,
    year: null,
    month: null,
    limit: null,
    offset: 0
  }
  idSearchParam.value.id = null
  result.value = []
  
  // Reset validation
  if (clientRef.value) {
    clientRef.value.resetValidation()
  }
  if (idRef.value) {
    idRef.value.resetValidation()
  }
}

/**
 * Refreshes the current search results after save/delete
 */
async function refreshResults() {
  if (!hasResults.value) return
  
  // Re-run the same search
  await search()
}

/**
 * Deletes the transactions after showing a confirmation message.
 */
async function handleDelete() {
  if (!hasResults.value) {
    alert('No transactions to delete')
    return
  }

  let confirmMessage = ''
  
  if (mode.value === 'id') {
    confirmMessage = `Are you sure you want to delete transaction ID: ${idSearchParam.value.id}?`
  } else {
    confirmMessage = `Are you sure you want to delete ${result.value.length} transaction(s) matching these filters? This action cannot be undone.`
  }
  
  if (!confirm(confirmMessage)) return
  
  loading.value = true
  
  try {
    let error
    
    if (mode.value === 'id') {
      [error] = await deleteTransaction(idSearchParam.value.id)
    } else if (mode.value === 'client') {
      [error] = await deleteTransactions(transactionParams.value)
    }
    
    if (error) {
      console.error('Delete failed:', error)
      alert('Failed to delete transaction(s)')
    } else {
      alert('Transaction(s) deleted successfully')
      // Clear results after successful delete
      result.value = []
      clearAndLoad()
    }
  } catch (error) {
    console.error('Unexpected error during delete:', error)
    alert('An error occurred while deleting')
  } finally {
    loading.value = false
  }
}

/**
 * Opens CreateMenu.vue for creating/editing data
 * @param transaction Transaction we want to edit, null if we are creating.
 */
function openCreateMenu(transaction = null) {
  selectedTransaction.value = transaction || null 
  showForm.value = true
}
</script>