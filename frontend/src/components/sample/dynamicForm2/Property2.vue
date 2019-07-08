<template>
<div>
<dynamic-form :headingText="currentHeadingText" :sectionHeadingText="currentSectionHeadingText">
    <actual-form slot="content" />
  </dynamic-form>
</div>
</template>

<script>
import DynamicForm from '../dynamic/DynamicForm'
import ActualForm from '@/components/sample/dynamicForm2/ActualForm'
export default {
  components: {
    DynamicForm,
    ActualForm,
  },
  data: function () {
    return {
      currentHeadingText: "Create a property from Within PropertySplit.vue",
      currentSectionHeadingText: "  Property Name & Address from PropertySplit.vue"
    }
  },
}
</script>
