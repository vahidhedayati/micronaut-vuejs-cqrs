<template id="driverSelect-template" xmlns="http://www.w3.org/1999/xhtml">
  <div class="form-group"> <!--4-->
    <select class="form-control" v-model="selected" @change="updateValue()">
      <option v-if="this.blankForm" disabled :value="null">Select a {{field}}</option>
      <option v-for="value in values" :value="value.id" :key="value.id">
        {{ value.name }}
      </option>
    </select>
  </div>
</template>

<script>
export default {
  props: {values: {type: Array}, field: {type: String},  actualItem: {  default: null },obj: {type: Object}},
  data: function () {
    return {
      blankForm: (this.actualItem==null),
      selected: this.actualItem
    }
  },
  created () {
    if (this.actualItem!=null) {
      this.updateValue();
    }
  },
  methods: {
    updateValue: function () {
      this.$emit('input', this.selected)
    }
  }
}
</script>

<style>
</style>
