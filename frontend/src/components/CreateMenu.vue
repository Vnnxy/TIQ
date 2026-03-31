<template>
  <v-dialog v-model="dialog" max-width="600">
    <v-card>
      <v-card-title class="text-h5">
       Transaction
      </v-card-title>

      <v-card-text>
        <v-form ref="inputRef">
          <v-row>
          <v-col cols="12" md="6">
            <v-text-field
              v-model.number="form.amount"
              label="Amount"
              type="number"
              :rules="[rules.required]"
              required
            />
          </v-col>

          <v-col cols="12" md="6">
            <v-text-field
              v-model.number="form.clientId"
              label="Client ID"
              type="number"
              :rules="[rules.required]"
              required
            />
          </v-col>

          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.date"
              label="Date"
              type="datetime-local"
              :rules="[rules.required]"
              required
            />
          </v-col>

          <v-col cols="12" md="6">
            <v-text-field
              v-model.number="form.merchantId"
              label="Merchant ID"
              type="number"
              :rules="[rules.required]"
              required
            />
          </v-col>
        </v-row>
        </v-form>
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
import { getMerchant } from '@/api/merchantsApi'

// Table properties
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
const inputRef = ref(null)

// Params
const form = ref({
  id: null,
  amount: null,
  clientId: null,
  date: null,
  merchantId: null
})

//Validation rules
const rules = {
    required: value => !!value || 'Field is required',
  }

// Uses watch to open dialogs and renders the desired form version.
watch(() => props.modelValue, (newVal) => {
  dialog.value = newVal
  
  if (newVal && props.transaction) {
    form.value = {
      id: props.transaction.id,
      amount: props.transaction.amount,
      clientId: props.transaction.clientId,
      date: formatDateForInput(props.transaction.date),
      merchantId: props.transaction.merchantId || null
    }
  } else if (newVal) {
    
    resetForm()
  }
})
// Used for closing/ending an operation and notifying the TransactionPage
watch(dialog, (newVal) => {
  emit('update:modelValue', newVal)
})

// Flag that indicates if its editing or adding. True if editting, false id adding.
const isEdit = computed(() => !!props.transaction?.id)

// Date formatting
function formatDateForInput(dateString) {
  if (!dateString) return ''
  
  return dateString.slice(0, 16)
}

// Resets form to empty values
function resetForm() {
  form.value = {
    id: null,
    amount: null,
    clientId: null,
    date: null,
    merchantId: null
  }
}
/**
 * Handles saving by either Posting or Putting
 */
async function save() {
  // Input validation
  const { valid } = await inputRef.value.validate()
    if (!valid) return
  saving.value = true
  
  try {
    //We get the merchant to see if it exists
    const [merchantErr, merchantData] = await getMerchant(form.value.merchantId)

    if (merchantErr || !merchantData) {
      alert(`Validation Error: Merchant ID ${form.value.merchantId} does not exist.`)
      saving.value = false
      return 
    }

    const payload = {
      amount: form.value.amount,
      clientId: form.value.clientId,
      date: form.value.date ? form.value.date + ':00' : null, 
      merchantId: form.value.merchantId
    }

   // We decide based on isEdit flag
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
// Closes the dialog when an error occurs
function close() {
  dialog.value = false
  resetForm()
}
</script>