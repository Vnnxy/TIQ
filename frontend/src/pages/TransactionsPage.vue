<template>
  <v-container>
    <h1 class="text-h5 mb-4">Transactions</h1>

    <v-card>
      <v-card-text>
          <v-select
          v-model="mode"
          :items="modes"
          label="Search type"/>
          
        <v-row v-if="mode === 'client'" class="mt-4">
          <v-col cols="12" md="3">
            <v-text-field
              v-model.number="transactionParams.clientId"
              label="Client ID"
              type="number"
              clearable
            />
          </v-col>

          <v-col cols="12" md="3">
            <v-text-field
              v-model.number="transactionParams.year"
              label="Year"
              type="number"
              clearable
            />
          </v-col>

          <v-col cols="12" md="3">
            <v-text-field
              v-model.number="transactionParams.month"
              label="Month"
              type="number"
              :rules="[rules.month]"
              clearable
            />
          </v-col>
        </v-row>
        <v-form v-if="mode === 'id'" ref="idRef">
          <v-row  class="mt-4">
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
            <v-btn variant="outlined" class = "mr-2" @click="clearAndLoad">
              Clear
            </v-btn>
            <v-btn variant="outlined" color = "red" class="mr-2" @click="handleDelete">
              Delete
            </v-btn>
            <v-btn variant="outlined" color = "green" @click="openCreateMenu">
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
            <th>City</th>
            <th>State</th>
            <th></th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="tx in result" :key="tx.id">
            <td>{{ tx.id }}</td>
            <td>{{ formatDate(tx.date) }}</td>
            <td>${{ tx.amount}}</td>
            <td>{{ tx.clientId }}</td>
            <td>{{ tx.merchant?.merchantCity  }}</td>
            <td>{{ tx.merchant?.merchantState  }}</td>
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
    />
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
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
  limit: 50,
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
  result.value = null
  
  if (mode.value === 'id') {  
      const { valid } = await idRef.value.validate()
      if (!valid) return
      const [,data] = await getTransaction(idSearchParam.value.id)
      const arrData = [data]
      result.value = arrData
  }
  if (mode.value === 'client') {  
      const [,data] = await getTransactions(transactionParams.value)
      result.value = data
  }
  loading.value = false
}

/**
 * Clears the inputs and reloads the transactions
 */
function clearAndLoad() {
  transactionParams.value = {
    clientId: null,
    year: null,
    month: null,
    limit: 50,
  }
  idSearchParam.value.id = null
  search()
}

/**
 * Deletes the transactions after showing a confirmation message.
 */
function handleDelete(){

  let confirmMessage = ''
  
  if (mode.value === 'id') {
    confirmMessage = `Are you sure you want to delete transaction ID: ${idSearchParam.value.id}?`
  } else {
    confirmMessage = 'Are you sure you want to delete all transactions matching these filters? This action cannot be undone.'
  }
  
  if (confirm(confirmMessage)) {
    if (mode.value === 'id') {
      deleteTransaction(idSearchParam.value.id)
    } else if (mode.value === 'client') {
      deleteTransactions(transactionParams.value)
    }
    
    clearAndLoad()
  }
}
/**
 * Opens CreateMenu.vue for creating/editing data
 * @param transaction Transaction we want to edit, null if we are creating.
 */
function openCreateMenu(transaction = null){
  selectedTransaction.value = transaction || null 
  showForm.value = true
}

onMounted(search)
</script>