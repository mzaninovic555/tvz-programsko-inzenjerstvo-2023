import api from '../../common/api';
import ComponentSearchResponse from '~/views/component-search/ComponentSearchResponse';
import ManufacturerResponse from '~/views/component-search/ManufacturerResponse';

export async function getComponents(componentSearch: string, priceRange: number[], componentType?: string, manufacturerSearch?: string)
    : Promise<ComponentSearchResponse[]> {
  const url = new URLSearchParams();
  url.set('name', componentSearch);
  url.set('type', componentType ?? '');
  url.set('manufacturer', manufacturerSearch ?? '');
  url.set('minPrice', String(priceRange[0]));
  url.set('maxPrice', String(priceRange[1]));

  const response = await api.get<ComponentSearchResponse[]>(`/v1/component?${url.toString()}`);
  return response.data;
}

export async function getComponentById(id: number): Promise<ComponentSearchResponse> {
  const response = await api.get<ComponentSearchResponse>(`/v1/component/${id}`);
  return response.data;
}

export async function getManufacturers(): Promise<ManufacturerResponse[]> {
  const response = await api.get<ManufacturerResponse[]>(`/v1/manufacturer`);
  return response.data;
}
