import {MessagesMessage} from 'primereact/messages';
import MessageType from './MessageType';
import Message from '~/common/messages/Message';

export function apiToMessages(message: Message): MessagesMessage {
  const type = messageTypeToSeverity(message.type);
  return {
    content: message.content,
    severity: type,
    sticky: message.type == MessageType.ERROR
  };
}

export function messageTypeToSeverity(type: MessageType) {
  switch (type) {
  case MessageType.ERROR:
    return 'error';
  case MessageType.INFO:
    return 'info';
  case MessageType.WARN:
    return 'warn';
  case MessageType.SUCCESS:
    return 'success';
  }
}
