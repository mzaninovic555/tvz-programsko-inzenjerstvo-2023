import ActivateResponse from './ActivateResponse';
import api from '../../common/api';

export async function activate(activationToken: string) : Promise<ActivateResponse> {
  const response = await api.post<ActivateResponse>('/v1/activate', {activationToken: activationToken});
  return response?.data;
}
