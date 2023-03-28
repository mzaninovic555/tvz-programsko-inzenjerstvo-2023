import api from '../../common/api';
import LoginResponse from '~/views/login/LoginResponse';
import BasicResponse from '~/common/messages/BasicResponse';

export async function login(username: string, password: string): Promise<LoginResponse> {
  const response = await api.post<LoginResponse>('/v1/login', {username, password});
  return response?.data;
}

export async function validateToken(): Promise<BasicResponse> {
  const response = await api.get<BasicResponse>('/v1/validate-token');
  return response.data;
}
