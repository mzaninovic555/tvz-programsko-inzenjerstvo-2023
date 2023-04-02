import UserSettingsResponse from '~/views/user-settings/UserSettingsResponse';
import api from '../../common/api';
import Message from '~/common/messages/Message';
import UserSettingsRequest from '~/views/user-settings/UserSettingsRequest';
import BasicResponse from '../../common/messages/BasicResponse';

export async function getUserSettings(): Promise<UserSettingsResponse> {
  const res = await api.get<UserSettingsResponse>('/v1/user-settings');
  return res.data;
}

export async function updateSettings(data: UserSettingsRequest): Promise<Message> {
  const res = await api.put<Message>('/v1/user-settings', data);
  return res.data;
}

export async function validatePassword(password: string): Promise<BasicResponse> {
  const res = await api.post<BasicResponse>('/v1/validate-password', {password: password});
  return res.data;
}

export async function deactivateAccount(username: string | undefined): Promise<BasicResponse> {
  const res = await api.post<BasicResponse>('/v1/deactivate-account', {username: username});
  return res.data;
}
