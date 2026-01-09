<template>
  <v-dialog v-model="dialog" max-width="600">
    <v-card>
      <v-card-title class="text-h5">
       Merchant
      </v-card-title>

      <v-card-text>
        <v-form ref="inputRef">
          <v-row>
           <v-col cols="12">
            <v-checkbox
              v-model="form.isOnline"
              label="Online Merchant"
              hide-details
              density="compact"
            />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.merchantCity"
              label="Merchant City"
              type="String"
              :rules="[rules.required]"
              required
            />
          </v-col>

          <v-col cols="12" md="6">
            <v-text-field
              v-model="form.merchantState"
              label="Merchant State"
              type="String"    
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
import { postMerchant, putMerchant } from '@/api/merchantsApi'

// Table properties
const props = defineProps({
  modelValue: Boolean,
  merchant: {
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
  merchantCity: null,
  merchantState: null,
  isOnline: false
})

//Validation rules
const rules = {
    required: value => !!value || 'Field is required',
  }

// Uses watch to open dialogs and renders the desired form version.
watch(() => props.modelValue, (newVal) => {
  dialog.value = newVal
  
  if (newVal && props.merchant) {
    form.value = {
      id: props.merchant.id,
      merchantCity: props.merchant.merchantCity,
      merchantState: props.merchant.merchantState,
    }
  } else if (newVal) {
    
    resetForm()
  }
})
// Used for closing/ending an operation and notifying the MerchantPage
watch(dialog, (newVal) => {
  emit('update:modelValue', newVal)
})

// Flag that indicates if its editing or adding. True if editting, false id adding.
const isEdit = computed(() => !!props.merchant?.id)


// Resets form to empty values
function resetForm() {
  form.value = {
    id: null,
    merchantCity: null,
    merchantState: null,
    isOnline: false
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
    const payload = {
      merchantCity: form.value.merchantCity,
      merchantState: form.value.merchantState || null
    }

   // We decide based on isEdit flag
    const [err, data] = isEdit.value
      ? await putMerchant(form.value.id, payload)
      : await postMerchant(payload)


    if (err) {
      console.error('Save failed:', err)
      alert('Failed to save merchant')
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
// Online values
watch(() => form.value.isOnline, (isOnline) => {
  if (isOnline) {
    form.value.merchantCity = 'ONLINE'
    form.value.merchantState = ''
  } else {
    form.value.merchantState = ''
    form.value.merchantCity = ''
  }
})
// Activate the checkbox if its online
watch(() => props.modelValue, (newVal) => {
  dialog.value = newVal
  
  if (newVal && props.merchant) {
    form.value = {
      id: props.merchant.id,
      merchantCity: props.merchant.merchantCity,
      merchantState: props.merchant.merchantState,
      isOnline: props.merchant.merchantCity === 'ONLINE' 
    }
  } else if (newVal) {
    resetForm()
  }
})
</script>