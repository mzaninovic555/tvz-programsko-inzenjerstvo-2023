import ComponentResponse from '~/views/component-search/ComponentResponse';

interface BuildResponse {
  id: string;
  link: string;
  title?: string;
  description?: string;
  isPublic: boolean;
  isFinalized: boolean;
  components: ComponentResponse[];
  ownerUsername?: string;
  isPublished: boolean;
}

export default BuildResponse;
