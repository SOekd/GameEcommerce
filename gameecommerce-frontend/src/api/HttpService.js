import Axios from "axios";
import Cookies from "js-cookie";

const apiBaseUrl = import.meta.env.VITE_API_URL;

class HttpService {

  constructor() {
    const jwtToken = Cookies.get('jwt');

    this.axios = Axios.create({
      baseURL: apiBaseUrl,
      headers: {
        'Content-type': 'application/json',
        'Authorization': jwtToken ? `Bearer ${jwtToken}` : '',
      },
    });
  }

  async get(endpoint) {
    return this.axios.get(endpoint);
  }

  async post(endpoint, data) {
    return this.axios.post(endpoint, data);
  }

  async put(endpoint, data) {
    return this.axios.put(endpoint, data);
  }

  async delete(endpoint) {
    return this.axios.delete(endpoint);
  }

}

export default new HttpService();
