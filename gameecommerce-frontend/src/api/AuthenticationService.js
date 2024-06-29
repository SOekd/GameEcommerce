import httpService from "@/api/HttpService";
import Cookies from 'js-cookie'


class AuthenticationService {

  isAuthenticated() {
    console.log('2- Cookies.get("jwt"):', Cookies.get("jwt"));
    return Cookies.get("jwt") !== undefined;
  }

  logout() {
    Cookies.remove("jwt");
  }

  async authenticate(username, password) {
    return httpService.post("/auth/login", JSON.stringify({ username: username, password: password }))
      .then(response => {
        Cookies.set("jwt", response.data.token, { expires: 1 });
        console.log('1- Cookies.get("jwt"):', Cookies.get("jwt"));
        return true;
      })
      .catch(() => {
        return false;
      });
  }

}

export default new AuthenticationService();
