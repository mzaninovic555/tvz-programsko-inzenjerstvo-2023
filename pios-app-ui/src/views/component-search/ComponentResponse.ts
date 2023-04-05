import Manufacturer from '~/views/component-search/Manufacturer';
import Type from '~/views/component-search/Type';

interface ComponentResponse {
  id: number;
  imageBase64: string;
  manufacturer: Manufacturer;
  name: string;
  price: number;
  rating: number;
  reviewCount: number;
  reviewed?: boolean;
  type: Type;
}

export default ComponentResponse;

