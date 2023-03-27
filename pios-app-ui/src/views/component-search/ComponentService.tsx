import Component from '../../views/component-search/Component';
import api from '../../common/api';
import Type from '../../views/component-search/Type';

export async function getComponents(componentSearch: any, componentType: string, priceRange: number[])
    : Promise<Component[]> {
  // return [{
  //   id: 1,
  //   name: 'Intel nešt',
  //   type: Type.HDD,
  //   price: 5,
  //   imageBase64: undefined,
  //   manufacturer: {name: 'Intel', id: 1},
  //   data: ''
  // },
  // {
  //   id: 2,
  //   name: 'Intel nešt',
  //   type: Type.HDD,
  //   price: 5,
  //   imageBase64: undefined,
  //   manufacturer: {name: 'Intel', id: 1},
  //   data: ''
  // },
  // {
  //   id: 3,
  //   name: 'Intel nešt testis',
  //   type: Type.HDD,
  //   price: 5,
  //   imageBase64: undefined,
  //   manufacturer: {name: 'Intel', id: 1},
  //   data: ''
  // }];

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
