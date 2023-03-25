import Component from '../../views/component-search/Component';
import api from '../../common/api';
import Type from '../../views/component-search/Type';

export async function getAllComponents(): Promise<Component[]> {
  return [{
    id: 3,
    name: 'Intel nešt testis',
    type: Type.HDD,
    price: 5,
    imageBase64: undefined,
    manufacturer: {name: 'Intel', id: 1},
    data: ''
  }];

  const response = await api.get<Component[]>('/vi/component');
  return response.data;
}

export async function getComponentById(id: number): Promise<Component> {
  const response = await api.get<Component>(`/vi/component/${id}`);
  return response.data;
}
