<template id="add-hotel-template" xmlns="http://www.w3.org/1999/xhtml">
  <div id="validated-form">
    <ul id="logs">
      <li v-for="log in logs" class="log">
        {{ log.event }}: {{ log.data }}
      </li>
    </ul>
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
       </div>

   <div class="col-sm-2">
      <div class="input-group">
      &nbsp
      </div>
        <div class="btn-group" role="group" aria-label="Add new vehicle">
          <button type="button" :disabled="submitted==true" class="btn btn-success" @click.prevent="submitForm()">Add hotel</button>
        </div>
      </div>
  </div>

  </div>
</template>

<script>
import HotelService from '@/services/HotelService'
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
/*
const socket = new WebSocket("ws://localhost:8082/ws/process")
socket.onmessage = function(msg){
  emitter.$emit("message", msg.data)
  console.log("Gt message"+msg.data)
}
socket.onerror = function(err){
  emitter.$emit("error", err)
}
*/
export default {
 //  props: ['countries', 'reload','fetchCountries','sortSearch'],
  data: function () {
    return {
      message: "",
      currentUser:"",
      logs: [],

      socket: null,
      //socketIo:{currentUser:'', errors:{}, success:false, },
      submitted:false,
      status: "disconnected",
      valid: true,
      errors: [],
      hotel:{currentUser:'', name:'AAAAAAAAAAAAA',code:'AAAA',phone:'+44-123456789', email:'aa@aa.com', updateUserName:'Admin' , updateUserId:'1',eventType:'HotelSaveCommand'}

    }
  },
  components: {
    'validateEmail': validateEmail,
    'validatePhone': validatePhone,
    'validateName' : validateName

  },
  created: function () {
    //this.authRecord=JSON.parse(localStorage.getItem('vuex')).auth.isAuthenticated;
    //this.hotel.updateUser.id=this.authRecord.id;


    /**
     * This is also sent as part of form submission - meaning to actually properly use this validation which is processed
     * via events to an undefined command handler - the currentUser is stored in physical Command.java object that exists on
     * all microservices
     */
    this.hotel.currentUser=Math.random().toString(36).substring(2) + (new Date()).getTime().toString(36);

    this.connect();

  },
  mounted() {
    this.socket.on('MESSAGE', (data) => {
      console.log("received  "+data)

//           this.$emit('hotel-errors',data);
    this.logs.push({ event: "Recieved message", msg });
  });
  },
   methods: {
     connect() {
       console.log("About to connect to websocket")
       this.socket = new WebSocket("ws://localhost:8082/ws/process");
       this.socket.onopen = () => {
         this.status = "connected";
         this.logs.push({ event: "Connected to", data: 'ws://localhost:8082'})
         console.log("Connected ")
        // this.socket.send(this.hotel.currentUser)
         this.socket.send(JSON.stringify({currentUser:this.hotel.currentUser,eventType:'userForm'}));
         /**
          * we only get a message back when something has gone wrong in gateway-command process action
          */


       };

     },
     disconnect() {
       this.socket.close();
       this.status = "disconnected";
       this.logs = [];
     },
     sendMessage() {
       console.log("Sending message "+this.hotel.currentUser)
       this.socket.send(JSON.stringify({currentUser:this.hotel.currentUser,eventType:'userForm'}));
       //this.logs.push({ event: "Sent message", data: this.message });
       this.message = "";
     },
     submitForm (e) {
      // if (this.postSuccess==false) {
         //  e.preventDefault();
       this.submitted=true;
      // }
       //this.message="current user "+this.currentUser;


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
         console.log('submitting valid form'+this.hotel.currentUser)

        this.submit();


       } else if (this.errors.length>0) {
         console.log('ERRors on page" '+this.errors)
         this.$emit('hotel-errors',this.errors);
       }
     },
    submit () {

      //This will call parent page with this action and pass this newly created object to it
      //this.$emit('add-hotel',hotel)

       return HotelService.postCall('/hotel',this.hotel)
              .then((res) => {
              if (res) {
                if (res.data) {
                    console.log('refresh entire list from hotelForm')
                     this.$emit('refresh-list')
                }
              }
            }).catch((error) => {

           this.$emit('hotel-errors',   error.response.data);

           if (error.response) {
              //this.$emit('hotel-errors', err.code);

            } else if ( error.request) {
              console.log("dddd"+error.request);
            } else {
              console.log('Error', error.message);
            }
          });


    }
  }
 }
</script>
