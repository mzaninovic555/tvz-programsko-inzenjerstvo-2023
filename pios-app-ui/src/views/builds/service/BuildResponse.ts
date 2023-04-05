import ComponentResponse from '~/views/component-search/ComponentResponse';

interface BuildResponse {
  link: string;
  title?: string;
  description?: string;
  isPublic: boolean;
  isFinalized: boolean;
  components: ComponentResponse[];
  ownerUsername?: string;
}

export default BuildResponse;
