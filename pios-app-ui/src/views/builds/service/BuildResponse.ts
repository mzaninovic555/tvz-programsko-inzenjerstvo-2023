import Component from '~/views/component-search/Component';

interface BuildResponse {
  id: string;
  link: string;
  title?: string;
  description?: string;
  isPublic: boolean;
  isFinalized: boolean;
  components: Component[];
  ownerUsername?: string;
  isPublished: boolean;
}

export default BuildResponse;
