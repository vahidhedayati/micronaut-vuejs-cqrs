
<template id="tablerow-template" xmlns="http://www.w3.org/1999/xhtml">
    <tr>
      <td>{{ user.id }}</td>
      <td>
          <span v-if="showForm">
              <input  v-model="updatedUser.username">
          </span>
          <span v-else>
              {{ user.username }}
          </span v-else>
      </td>
      <td>
          <span v-if="showForm">
                <input  v-model="updatedUser.firstname">
          </span>
          <span v-else>
              {{ user.firstname }}
            </span v-else>
       </td>
      <td>
          <span v-if="showForm">
                <input  v-model="updatedUser.surname">
          </span>
        <span v-else>
              {{ user.surname }}
            </span v-else>
      </td>

      <td>  {{ user.lastUpdated | shortMoment() }}
      </td>
      <td class="dropdown dropdown-table">
        <span v-if="showForm">
            <button v-on:click="save(updatedUser)">Save</button>
        </span>
        <span v-else>
          <button v-on:click="editUser(user)">Edit</button>
          <span class="hoverButton">
      <vue-dropdown :config="config"
                    @setSelectedOption="actionDropDown($event,user.id)"
      ></vue-dropdown>
            </span>
        </span>
      </td>
    </tr>
</template>

<script>
import FieldSelect from '../../form/FieldSelect'
import HotelService from '@/services/HotelService'
import moment from 'moment';
import VueMoment from 'vue-moment'
import VueDropdown from 'vue-dynamic-dropdown'
export default {
   props: ['user', 'makes', 'models', 'drivers','reload','updatedResults'],
   data () {
        return {
          response: [],
          modalErrors: [],
          newUser:{username:'',firstname:'',surname:'',updateUser:{id:''},eventType:'UserUpdateCommand'},
          deleteUser:{id:'',eventType:'UserDeleteCommand'},
          showUser:null,
          updatedUser:{},
          serverURL: process.env.SERVER_URL,
          showForm: false,
          config: {
            options: [
              {
                value: "display"
              },
              {
                value: "delete"
              },
            ],
            prefix: "",
            backgroundColor: "transparent"
          }

        }
      },
      components: {
          FieldSelect,
        VueMoment,
        moment,
        VueDropdown
        },
      filters: {
        moment: function (date) {
          return moment(date).format('MMMM Do YYYY, h:mm:ss a');
        },
        shortMoment: function (date) {
          return moment(date).format('DD MMM YYYY');
        }

      },
       methods: {
         moment: function () {
           return moment();
         },
         editUser() {
          this.showForm=true;
          this.updatedUser=this.user;
         },
         actionDropDown(event,id) {
           if (event.value==='display') {
             return HotelService.fetchRoot('/user/'+id)
               .then((res) => {
               if (res.data) {
                  this.$emit('user-show', res.data);
               }
             }).catch((error) => {
                 if (error.response) {
                 this.$emit('user-errors', error.response.data.error);
               }
             });
           }
           if (event.value==='delete') {
             if (confirm('Delete record?')) {
               this.deleteUser.id=id;
               return HotelService.postCall('/user',this.deleteUser)
                 .then((res) => {
                   if (res.data||res.status===200) {
                    this.$emit('refresh-list');
                   }
               }).catch((error) => {
                   if (error.response) {
                   //this.$emit('user-errors', error.response.data.error);
                 }
               });
             }
           }
         },
         updateValue: function (value) {
           this.$emit('input', value);
         },
         save() {
           const newName = this.updatedUser;
           this.newUser.id=newName.id
           this.newUser.code=newName.code
           this.newUser.name=newName.name
           return HotelService.postCall('/user',this.newUser)
             .then((res) => {
               if (res.data||res.status===204) {
                 this.showForm=false;
                 this.$emit('user-update', res.data);
               }
             }).catch((error) => {
                if (error.response) {
                     this.$emit('user-errors', error.response);
                }
            });
         }
     }
}

</script>
<style>
.dropdown-label.text {
    font-size:0.8em;
  }
.dropdown-label-container {
    margin-top: -30px !important;
  }
.dropdown-label-container .angle-down {
    margin-top: -10px !important;
    margin-left: -10px !important;
    background-color: orange !important;
  }
.hoverButton {
    margin-left:-70px;
    display: inline-block !important;
    width:30px !important;
    font-size:0.8em;

  }
.dropdown-table .options {
    width:80px !important;
    font-size:0.8em;
    background: red;
  }
</style>
