import api from '../../common/api';
import ComponentResponse from '~/views/component-search/ComponentResponse';

export async function getTopRatedBuilds(): Promise<ComponentResponse[]> {
  const response = await api.get<ComponentResponse[]>(`/v1/component/top-rated`);
  return response.data;
}
