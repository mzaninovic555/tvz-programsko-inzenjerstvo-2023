import BuildResponse from '~/views/builds/service/BuildResponse';
import Message from '~/common/messages/Message';

interface BuildChangeResponse {
  build: BuildResponse;
  message: Message;
}

export default BuildChangeResponse;
