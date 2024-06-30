/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import { createRouter, createWebHistory } from 'vue-router/auto'
import { setupLayouts } from 'virtual:generated-layouts'
import authenticationService from "@/api/AuthenticationService";
import Cookies from "js-cookie";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  extendRoutes: setupLayouts,
})

router.beforeEach(async (to) => {

  if (to === undefined) {
    return;
  }

  if (to.name.startsWith("/admin") && !authenticationService.isAuthenticated()) {
    return "/login";
  }

});

export default router
