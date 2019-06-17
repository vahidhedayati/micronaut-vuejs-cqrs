
import axios from 'axios'
const writeInstance = axios.create({
    baseURL: `http://localhost:8082/`
  })

  const readInstance = axios.create({
    baseURL: `http://localhost:8081/`
  })
export default {
  fetchParams (component,params) {
    return readInstance.get(component, {params:params })
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

  fetchBlob (component) {
    return readInstance.get(component,{responseType: 'blob', headers: { 'Accept': 'application/vnd.ms-excel' }})
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
  fetchName (component) {
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
  createName (component, params) {
    return writeInstance.post(component, params)
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
  createNoCatch (component, params) {
    return writeInstance.post(component, params)

  },
  postCall (component, params) {
    return writeInstance.post(component, params)

  },
  putRootNoCatch (component, params) {
    return writeInstance.put(component, params)

  },
  patchRootNoCatch (component, params) {
    return writeInstance.patch(component, params)

  },
  update(component,params) {

    return writeInstance.patch(component, params)
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
  deleteAlt (component, params) {
    return writeInstance.delete(component, params)
  },
  deleteNoCatch (component, id) {
    return writeInstance.delete(component+'/' + id)
  },
  delete (component,id) {
    return writeInstance.delete(component+'/' + id)
    .catch((error) => {
        if (error.response) {
            console.log(error.response);
        } else if (error.request) {
            console.log(error.request);
        } else {
            console.log('Error', error.message);
        }
    });
  }
}
