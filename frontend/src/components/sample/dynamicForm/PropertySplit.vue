<template>
<div>
property split

<dynamic-form
  :headingText="currentHeadingText"
  :sectionHeadingText="currentSectionHeadingText"
 :formdata=<actual-form/>
  :exampleComponent="exampleComponent"
  />
</div>
</template>

<script>
import DynamicForm from '../dynamic/DynamicForm'
import ActualForm from '@/components/sample/dynamicForm/ActualForm'

/**
* The following few lines dynamically load in a vuejs VUE file as a component that is on this parent page
* and becomes accessible by children so DynamicForm.vue calls form-content to actually load in the form's content.
* Simply provide a different Form that would present ActualForm.vue on this page from a newly generated main / form page
* to produce dynamic forms using this template
* ----------------------
* Please note this can't be used on multiple pages making or resusing the same component - therefore an alternate has
* been used. As was the only definition required was this component and the child page simply calls <form-content/>.
*/

//import Vue from 'vue'
//Vue.component('form-content', {
//   components: { ActualForm },
//    template: `<actual-form></actual-form>`
//});

/**
* This is the alternative loading in the dynamic content of actual form for this VUE JS file
* It is also declared as a component further down and then passed in above as
* formdata=`<form-content/>`
*/

/*
const FormContent = {
  components: { ActualForm },
  template: `<actual-form></actual-form>`
}
*/

import Vue from 'vue'
const exampleComponent = Vue.component('form-content', {
   components: { ActualForm },
    template: `<actual-form></actual-form>`
});


export default {
  components: {
    DynamicForm,
    ActualForm,

     "example-component": exampleComponent
  },
  data: function () {
    return {
      currentHeadingText: "Create a property from Within PropertySplit.vue",
      currentSectionHeadingText: "  Property Name & Address from PropertySplit.vue",

    }
  },
}
</script>
