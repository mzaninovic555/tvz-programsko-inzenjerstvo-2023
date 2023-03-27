import Manufacturer from '~/views/component-search/Manufacturer';
import Type from '~/views/component-search/Type';

interface Component {
  id: number;
  name: string;
  price: number;
  type: Type;
  data: string;
  manufacturer: Manufacturer;
}

export default Component;
