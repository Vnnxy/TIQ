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
              clearable
            />
          </v-col>
        </v-row>
        <v-row v-if="mode === 'id'" class="mt-4">
          <v-col cols="12" md="3">
            <v-text-field
              v-model.number="idSearchParam.id"
              label="Transaction ID"
              type="number"
              clearable
            />
          </v-col>
        </v-row>
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
      @saved="handleSaved"
    />
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTransactions, getTransaction, deleteTransaction, deleteTransactions } from '@/api/transactionsApi'
import CreateMenu from '@/components/CreateMenu.vue'


const loading = ref(false)
const mode = ref('client')
const result = ref([])
const showForm = ref(false)
const selectedTransaction = ref(null)

const modes = [
  { title: 'Transaction Id', value: 'id' },
  { title: 'Client Id', value: 'client' },
]

const transactionParams = ref({
  clientId: null,
  year: null,
  month: null,
  limit: 50,
})

const idSearchParam = ref({
  id: null
})

function formatDate(dateString) {
  if (!dateString) return 'N/A'
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', { 
    year: 'numeric', 
    month: 'short', 
    day: 'numeric' 
  })
}

async function search() {
  loading.value = true
  result.value = null
  
  if (mode.value === 'id') {  
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

function openCreateMenu(transaction = null){
  selectedTransaction.value = transaction || null 
  showForm.value = true
}
function handleSaved(savedTransaction) {
  console.log('Transaction saved:', savedTransaction)
  showForm.value = false
  search()  
}

onMounted(search)
</script>