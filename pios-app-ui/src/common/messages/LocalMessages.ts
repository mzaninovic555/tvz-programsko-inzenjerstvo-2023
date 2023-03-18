import Message from '~/common/messages/Message';
import MessageType from '../messages/MessageType';

export const logoutSuccessMessage: Message = {
  type: MessageType.INFO,
  content: 'Successfully logged out'
};

export const loginSuccessMessage: Message = {
  type: MessageType.SUCCESS,
  content: 'Successfully logged in'
};
