import Component from '../../views/component-search/Component';
import api from '../../common/api';

export async function getComponents(componentSearch: string, componentType: string, priceRange: number[])
    : Promise<Component[]> {
  const url = new URLSearchParams();
  url.set('name', componentSearch);
  url.set('type', componentType);
  url.set('minPrice', String(priceRange[0]));
  url.set('maxPrice', String(priceRange[1]));

  const response = await api.get<Component[]>(`/v1/component?${url.toString()}`);
  return response.data;
}

export async function getComponentById(id: number): Promise<Component> {
  const response = await api.get<Component>(`/v1/component/${id}`);
  return response.data;
}
