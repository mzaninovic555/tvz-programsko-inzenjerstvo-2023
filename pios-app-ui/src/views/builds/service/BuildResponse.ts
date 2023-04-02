import Component from '~/views/component-search/Component';

interface BuildResponse {
  link: string;
  title?: string;
  description?: string;
  isPublic: boolean;
  finalized: boolean;
  components: Component[];
  ownerUsername?: string;
}

export default BuildResponse;
