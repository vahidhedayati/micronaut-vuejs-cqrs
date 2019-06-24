<template id="add-user-template" xmlns="http://www.w3.org/1999/xhtml">
  <div>
     <div id="inputRow" class="row">
          <div class="col-sm-3">
            <div class="input-group">
            User name:
              <input type="text" class="form-control" placeholder="Enter a username..." v-model="user.username" required>
            </div>
          </div>

          <div class="col-sm-2">
            <div class="input-group">
            User Password:
              <input type="text" class="form-control" placeholder="Enter password" v-model="user.password">
            </div>
          </div>
       <div class="col-sm-2">
         <div class="input-group">
           User firstname:
           <input type="text" class="form-control" placeholder="Enter firstname" v-model="user.firstname">
         </div>
       </div>
       <div class="col-sm-2">
         <div class="input-group">
           User lastname:
           <input type="text" class="form-control" placeholder="Enter surname" v-model="user.lastname">
         </div>
       </div>

        <input type="hidden"  v-model="user.updateUser.id">

   <div class="col-sm-2">
      <div class="input-group">
      &nbsp
      </div>
        <div class="btn-group" role="group" aria-label="Add new vehicle">
          <button type="button" class="btn btn-success" @click="submit()">Add user</button>
        </div>
      </div>
  </div>
  </div>
</template>

<script>
import HotelService from '@/services/HotelService'
export default {
  data: function () {
    return {
      user:{username:'',password:'', firstname:'', surname:'', updateUser:{id:''},eventType:'UserSaveCommand'},
    }
  },
   methods: {
    submit () {
      return HotelService.postCall('/user',JSON.stringify(this.user))
              .then((res) => {
              if (res) {
                if (res.data) {
                     this.$emit('refresh-list')
                }
              }
            }).catch((error) => {
              if (error.response) {
                     this.$emit('user-errors', error.response.data.error);
              }
            });
    }
  }
 }
</script>
