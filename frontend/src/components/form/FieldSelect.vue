<template id="driverSelect-template" xmlns="http://www.w3.org/1999/xhtml">
  <div class="form-group"> <!--4-->
    <select class="form-control" v-model="selected" @change="updateValue()"
            style="background-color:#FFFFFF;display: block; font-size: 14px;  font-style: italic;"
            onmouseover="this.className='form-control with_bg'; "
            onChange="this.style.backgroundColor=this.options[this.selectedIndex].style.backgroundColor;
            this.style.fontStyle=this.options[this.selectedIndex].style.fontStyle;">
      <option v-if="this.blankForm" style="background-color:#8ded67;  font-style: italic;"  :value="null">Select a {{field}}</option>
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
  .with_bg{background-color:#e7fcde; display: block; font-size: 14px; border: 2px solid #5ed230; cursor: pointer;}
  .without_bg{background-color:#FFFFFF; cursor: pointer;}
</style>
