<template>
  <v-dialog v-model="dialog" max-width="600">
    <v-card>
      <v-card-title class="text-h5">
       Transaction
      </v-card-title>

      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field
              v-model.number="form.amount"
              label="Amount"
              type="number"
              required
            />
          </v-col>

          <v-col cols="12" md="6">
            <v-text-field
              v-model.number="form.clientId"
              label="Client ID"
              type="number"
              required
            />
          </v-col>

          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.date"
              label="Date"
              type="datetime-local"
              required
            />
          </v-col>

          <v-col cols="12" md="6">
            <v-text-field
              v-model.number="form.merchantId"
              label="Merchant ID"
              type="number"
              required
            />
          </v-col>
        </v-row>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn color="grey" variant="text" @click="close">
          Cancel
        </v-btn>
        <v-btn 
          color="primary" 
          variant="text" 
          @click="save"
          :loading="saving"
        >
          {{ isEdit ? 'Update' : 'Create' }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { postTransaction, putTransaction } from '@/api/transactionsApi'

const props = defineProps({
  modelValue: Boolean,
  transaction: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'saved'])

const dialog = ref(props.modelValue)
const saving = ref(false)

const form = ref({
  id: null,
  amount: null,
  clientId: null,
  date: null,
  merchantId: null
})

// Sync dialog with parent
watch(() => props.modelValue, (newVal) => {
  dialog.value = newVal
  
  // Load transaction data if editing
  if (newVal && props.transaction) {
    form.value = {
      id: props.transaction.id,
      amount: props.transaction.amount,
      clientId: props.transaction.clientId,
      date: formatDateForInput(props.transaction.date),
      merchantId: props.transaction.merchant?.id || null
    }
  } else if (newVal) {
    
    resetForm()
  }
})

watch(dialog, (newVal) => {
  emit('update:modelValue', newVal)
})

const isEdit = computed(() => !!props.transaction?.id)

function formatDateForInput(dateString) {
  if (!dateString) return ''
  
  return dateString.slice(0, 16)
}

function resetForm() {
  form.value = {
    id: null,
    amount: null,
    clientId: null,
    date: null,
    merchantId: null
  }
}

async function save() {
  saving.value = true
  
  try {
    // Prepare payload
    const payload = {
      amount: form.value.amount,
      clientId: form.value.clientId,
      date: form.value.date ? form.value.date + ':00' : null, // Add seconds
      merchant: {
        id: form.value.merchantId  
      }
    }

   
    const [err, data] = isEdit.value
      ? await putTransaction(form.value.id, payload)
      : await postTransaction(payload)


    if (err) {
      console.error('Save failed:', err)
      alert('Failed to save transaction')
    } else {
      emit('saved', data)
      close()
    }
  } catch (error) {
    console.error('Save error:', error)
    alert('An error occurred')
  } finally {
    saving.value = false
  }
}

function close() {
  dialog.value = false
  resetForm()
}
</script>