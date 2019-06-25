<template id="add-hotel-template" xmlns="http://www.w3.org/1999/xhtml">
  <div id="validated-form">
     <div id="inputRow" class="row">
          <div class="col-sm-3">
            <div class="input-group">
            Hotel name:

              <input type="text" class="form-control"  pattern="(?=.*[A-Za-z0-9]).{3,55}" placeholder="Enter a name..." title="Hotel name: 3 - 55 characters only " v-model="hotel.name" >
            </div>
          </div>
          <div class="col-sm-2">
            <div class="input-group">
            Hotel Code:
              <input type="text" class="form-control"  pattern="(?=.*[A-Z]).{2,4}" placeholder="Enter a code..." title="Hotel code: Upper Case A-Z 2 to 4 characters only" v-model="hotel.code" required>

            </div>
          </div>
       <div>
         <label>Phone:</label>
         <input type="text" v-model="hotel.phone" value />
       </div>
       <div>
         <label>Email:</label>
         <input type="text" v-model="hotel.email" />
       </div>
       <div>
         <label>Update Username:</label>
         <input type="text" v-model="hotel.updateUserName" />
       </div>
       <div>
         <label>Update UserId:</label>
         <input type="text" v-model="hotel.updateUserId" />
         <autocomplete :items="[ 'Apple', 'Banana', 'Orange', 'Mango', 'Pear', 'Peach', 'Grape', 'Tangerine', 'Pineapple']" />

       </div>

   <div class="col-sm-2">
      <div class="input-group">
      &nbsp
      </div>
        <div class="btn-group" role="group" aria-label="Add new vehicle">
          <button type="button" :disabled="submittedForm==true" class="btn btn-success" @click.prevent="submitForm()">Add hotel</button>
        </div>
      </div>
  </div>

  </div>
</template>

<script>
import HotelService from '@/services/HotelService'
import Autocomplete from '../Autocomplete'
const validateEmail= email => {
  if (!email.length) {
    return { valid: false, error: "email_needed"};
  }
  if (!email.match(/^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/)) {
    return { valid: false, error: "email_invalid" };
  }
  return { valid: true, error: null };
};
const validateName = name => {
  if (!name.length) {
    return { valid: false, error: "Name field is required" };
  }
  return { valid: true, error: null };
};
const validatePhone = phone => {
  if (!phone.length) {
    return { valid: false, error: 'phone_required' };
  }

  if (!phone.match(/^[+][(]?[0-9]{1,3}[)]?[-\s.]?[0-9]{3}[-\s.]?[0-9]{4,7}$/gm)) {
    return { valid: false, error: 'phone_invalid' };
  }

  return { valid: true, error: null };
}

export default {
   props: ['masterUser','submittedForm'],
  data: function () {
    return {
      valid: true,
      errors: [],
      hotel:{currentUser:this.masterUser,  name:'AAAAAAAAAAAAA',code:'AAAA',phone:'+44-123456789', email:'aa@aa.com', updateUserName:'Admin' , updateUserId:'1',eventType:'HotelSaveCommand'}

    }
  },
  components: {
    'validateEmail': validateEmail,
    'validatePhone': validatePhone,
    'validateName' : validateName,
    'autocomplete' : Autocomplete

  },
   methods: {
     submitForm (e) {
       this.$emit(' form-status',true);
       this.errors=[];
       const validName = validateName(this.hotel.name);
       this.errors.push(validName.error);
       this.valid = validName.valid
       const validPhone = validatePhone(this.hotel.phone);
       this.errors.push(validPhone.error);
       this.valid = validPhone.valid
       const validEmail = validateEmail(this.hotel.email);
       this.errors.push(validEmail.error);
       this.valid = validEmail.valid
       /**
        * IF form is valid for submission submit it - otherwise fail front end validation:
        */
       if (this.valid) {
        this.submit();
       } else if (this.errors.length>0) {
         this.$emit('hotel-errors',this.errors);
       }
     },
    submit () {
      this.$emit('current-hotel',  this.hotel);
       return HotelService.postCall('/hotel',this.hotel)
              .then((res) => {
              if (res) {
                if (res.data) {
                  this.$emit('hotel-update', this.hotel);
                }
              }
            }).catch((error) => {
            this.$emit('hotel-errors',   error.response.data);
           if (error.response) {
            } else if ( error.request) {
            } else {
            }
          });
    }
  }
 }
</script>
