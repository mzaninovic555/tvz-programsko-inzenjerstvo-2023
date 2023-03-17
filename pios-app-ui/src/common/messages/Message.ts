import MessageType from '~/common/messages/MessageType';

interface Message {
  type: MessageType;
  content: string;
}

export default Message;
