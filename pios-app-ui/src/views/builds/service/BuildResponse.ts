import ComponentResponse from '~/views/component-search/ComponentResponse';
import Message from '~/common/messages/Message';

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
  compatibilityMessages: Message[];
}

export default BuildResponse;
