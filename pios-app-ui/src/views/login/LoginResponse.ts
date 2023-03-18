import Message from '~/common/messages/Message';

interface LoginResponse {
  token?: string;
  message?: Message;
}

export default LoginResponse;
