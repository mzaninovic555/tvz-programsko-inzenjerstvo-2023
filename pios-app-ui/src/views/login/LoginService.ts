import api from '../../common/api';
import LoginResponse from '~/views/login/LoginResponse';

export async function login(username: string, password: string): Promise<LoginResponse> {
  const response = await api.post<LoginResponse>('/v1/login', {username, password});
  return response?.data;
}
