
import axios from 'axios'
const writeInstance = axios.create({
    baseURL: `http://localhost:8082/`
  })

  const readInstance = axios.create({
    baseURL: `http://localhost:8081/`
  })
export default {
  fetchRoot (component) {
    return readInstance.get(component)
      .catch((error) => {
      if (error.response) {
        console.log(error.response);
      } else if (error.request) {
        console.log(error.request);
      } else {
        console.log('Error', error.message);
      }
    });
  },
  postCall (component, params) {
    return writeInstance.post(component, params)

  }
}
