import Manufacturer from '~/views/component-search/Manufacturer';
import Type from '~/views/component-search/Type';

interface Component {
  id: number;
  name: string;
  price: number;
  type: Type;
  data: string;
  imageBase64: string | undefined;
  manufacturer: Manufacturer;
  rating?: number;
  reviewCount: number;
  reviewed?: boolean;
}

export default Component;
