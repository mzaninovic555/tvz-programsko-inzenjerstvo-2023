import api from '../../common/api';
import LoginResponse from '~/views/login/LoginResponse';
import {AxiosError} from 'axios';

export async function login(username: string, password: string): Promise<LoginResponse> {
  const response = await api.post<LoginResponse>('/v1/login', {username, password}).catch((e: AxiosError) => e.response);
  return response?.data ?? {};
}
