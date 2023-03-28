import api from '../../common/api';
import ComponentSearchResponse from '~/views/component-search/ComponentSearchResponse';

export async function getComponents(componentSearch: string, componentType: string, priceRange: number[])
    : Promise<ComponentSearchResponse[]> {
  const url = new URLSearchParams();
  url.set('name', componentSearch);
  url.set('type', componentType);
  url.set('minPrice', String(priceRange[0]));
  url.set('maxPrice', String(priceRange[1]));

  const response = await api.get<ComponentSearchResponse[]>(`/v1/component?${url.toString()}`);
  return response.data;
}

export async function getComponentById(id: number): Promise<ComponentSearchResponse> {
  const response = await api.get<ComponentSearchResponse>(`/v1/component/${id}`);
  return response.data;
}
