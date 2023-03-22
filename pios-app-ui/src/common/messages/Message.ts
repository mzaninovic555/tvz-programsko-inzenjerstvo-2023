import MessageType from '~/common/messages/MessageType';

interface Message {
  type: MessageType;
  content: string;
  reference?: string;
}

export default Message;
