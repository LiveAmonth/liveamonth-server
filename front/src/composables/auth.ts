import { ref } from "vue";
import type { LoginType } from "@/modules/types/form/FormType";
import { useAuthStore } from "@/stores/auth";

export const useAuth = () => {
  const store = useAuthStore();
  const error = ref();
  const isPending = ref(false);

  const login = async (request: LoginType) => {
    error.value = null;
    isPending.value = true;
    try {
      await store.login(request);
      error.value = null;
    } catch (err) {
      error.value = err;
    } finally {
      isPending.value = false;
    }
  };
  const logout = async () => {
    error.value = null;
    isPending.value = true;
    try {
      await store.logout();
      error.value = null;
    } catch (err) {
      error.value = err;
    } finally {
      isPending.value = false;
    }
  };
  return {
    error,
    isPending,
    login,
    logout,
  };
};