import Component from '../../views/component-search/Component';
import api from '../../common/api';
import Type from '../../views/component-search/Type';

export async function getComponents(componentSearch: any, componentType: string, priceRange: number[])
    : Promise<Component[]> {
  return [{
    id: 1,
    name: 'Intel nešt',
    type: Type.HDD,
    price: 5,
    imageBase64: undefined,
    manufacturer: {name: 'Intel', id: 1},
    data: ''
  },
  {
    id: 2,
    name: 'Intel nešt',
    type: Type.HDD,
    price: 5,
    imageBase64: undefined,
    manufacturer: {name: 'Intel', id: 1},
    data: ''
  },
  {
    id: 3,
    name: 'Intel nešt testis',
    type: Type.HDD,
    price: 5,
    imageBase64: undefined,
    manufacturer: {name: 'Intel', id: 1},
    data: ''
  }];

  const url = `/vi/component?name=${componentSearch}&type=${componentType}&min_price=${priceRange[0]}&max_price=${priceRange[1]}`;
  const response = await api.get<Component[]>(url);
  return response.data;
}

export async function getComponentById(id: number): Promise<Component> {
  const response = await api.get<Component>(`/vi/component/${id}`);
  return response.data;
}
