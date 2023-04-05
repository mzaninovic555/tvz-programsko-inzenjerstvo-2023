import api from '../../common/api';
import ManufacturerResponse from '~/views/component-search/ManufacturerResponse';
import ComponentResponse from '~/views/component-search/ComponentResponse';

export async function getComponents(componentSearch: string, priceRange: number[], componentType?: string, manufacturerSearch?: string)
    : Promise<ComponentResponse[]> {
  const url = new URLSearchParams();
  url.set('name', componentSearch);
  url.set('type', componentType ?? '');
  url.set('manufacturer', manufacturerSearch ?? '');
  url.set('minPrice', String(priceRange[0]));
  url.set('maxPrice', String(priceRange[1]));

  const response = await api.get<ComponentResponse[]>(`/v1/component?${url.toString()}`);
  return response.data;
}

export async function getComponentById(id: number): Promise<ComponentResponse> {
  const response = await api.get<ComponentResponse>(`/v1/component/${id}`);
  return response.data;
}

export async function getManufacturers(): Promise<ManufacturerResponse[]> {
  const response = await api.get<ManufacturerResponse[]>(`/v1/manufacturer`);
  return response.data;
}
