import {Messages, MessagesMessage} from 'primereact/messages';
import MessageType from './MessageType';
import Message from '~/common/messages/Message';
import {ToastMessage} from 'primereact/toast';
import {RefObject} from 'react';

const TIMEOUT_SECONDS = 3_000;

export function apiToMessages(message: Message): MessagesMessage {
  const type = messageTypeToSeverity(message.type);
  return {
    detail: message.content,
    severity: type,
    sticky: message.type == MessageType.ERROR || message.type == MessageType.WARN,
    life: message.type == MessageType.ERROR || message.type == MessageType.WARN ? undefined : TIMEOUT_SECONDS
  };
}

export function apiToToast(message: Message): ToastMessage {
  const type = messageTypeToSeverity(message.type);
  return {
    detail: message.content,
    summary: messageTypeToSummary(message.type),
    severity: type,
    sticky: message.type == MessageType.ERROR || message.type == MessageType.WARN,
    life: message.type == MessageType.ERROR || message.type == MessageType.WARN ? undefined : TIMEOUT_SECONDS
  };
}

export function messageTypeToSummary(type: MessageType) {
  if (type == MessageType.WARN) {
    return 'Warning';
  }
  return type.charAt(0).toUpperCase() + type.substring(1).toLowerCase();
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

export function showMessagesWithoutReference(msg: Message[], messages: RefObject<Messages>) {
  if (!msg.length) {
    return;
  }
  messages.current?.replace(msg.filter((x) => !x.reference).map(apiToMessages));
}
