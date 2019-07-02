<template id="tablerow-template" xmlns="http://www.w3.org/1999/xhtml">
    <tr>
      <td>{{ hotel.id }}</td>
      <td>
        <span v-if="showForm">
            <input  v-model="updatedHotel.name">
        </span>
        <span v-else>
            {{ hotel.name }}
        </span v-else>
      </td>
      <td>
        <span v-if="showForm">
              <input  v-model="updatedHotel.code">
        </span>
        <span v-else>
            {{ hotel.code }}
          </span v-else>
     </td>
      <td>
        <span v-if="showForm">
              <input  v-model="updatedHotel.phone">
        </span>
        <span v-else>
            {{ hotel.phone }}
          </span v-else>
      </td>
      <td>
        <span v-if="showForm">
              <input  v-model="updatedHotel.email">
        </span>
        <span v-else>
            {{ hotel.email }}
          </span v-else>
      </td>
      <td> username {{hotel.updateUserName}}
        ID: {{hotel.updateUserId}}
      </td>
      <td>  {{ hotel.lastUpdated | shortMoment() }}
      </td>
      <td class="dropdown dropdown-table">
        <span v-if="showForm">
            <button v-on:click="save(updatedHotel)">Save</button>
        </span>
        <span v-else>
          <button v-on:click="editHotel(hotel)">Edit</button>
          <span class="hoverButton">
      <vue-dropdown :config="config"
                    @setSelectedOption="actionDropDown($event,hotel.id)"
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
   props: ['hotel', 'makes', 'models', 'drivers','reload','updatedResults'],
   data () {
        return {
          response: [],
          errors: [],
          showHotel:null,
          updatedHotel:{},
          deleteHotel:{id:'',eventType:'HotelDeleteCommand'},
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
         editHotel() {
          this.showForm=true;
          this.updatedHotel=this.hotel;
         },
         actionDropDown(event,id) {
           if (event.value==='display') {
             return HotelService.fetchRoot(''+id)
               .then((res) => {
               if (res.data) {
                   this.$emit('hotel-show', res.data);
                 }
               }).catch((error) => {
                   if (error.response) {
                    this.$emit('hotel-errors', error.response.data.error);
                   }
               });
           }
           if (event.value==='delete') {
             if (confirm('Delete record?')) {
               this.deleteHotel.id=id;
               return HotelService.postCall('/hotel',this.deleteHotel)
                 .then((res) => {
                 if (res.data.error) {
                   this.$emit('hotel-errors',   res.data.error);
                 } else  if (res.data||res.status===200||res.status===202) {
                   this.$emit('remove-hotel', this.deleteHotel);
                 }

             }).catch((error) => {});

             }
           }
         },
         updateValue: function (value) {
           this.$emit('input', value);
         },
         save() {
           const newName = this.updatedHotel;
           newName.eventType='HotelUpdateCommand'
           return HotelService.postCall('/hotel',newName)
             .then((res) => {
             if (res.data.error) {
               this.$emit('hotel-errors',   res.data.error);
            } else  if (res.data||res.status===204||res.status===202) {
              this.showForm = false;
              this.$emit('hotel-update', res.data);
            }

          }).catch((error) => {});
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
