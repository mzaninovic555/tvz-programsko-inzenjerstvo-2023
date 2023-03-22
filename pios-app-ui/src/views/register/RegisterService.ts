import LoginResponse from "~/views/login/LoginResponse";
import api from "~/common/api";
import {AxiosError} from "axios";
import RegisterResponse from "../../views/register/RegisterResponse";

export async function register(email: string, username: string, password: string, description: string)
    : Promise<LoginResponse> {
  const response = await api.post<RegisterResponse>('/v1/register', {email, username, password, description})
                            .catch((e: AxiosError) => e.response);
  return response?.data ?? {}
}
