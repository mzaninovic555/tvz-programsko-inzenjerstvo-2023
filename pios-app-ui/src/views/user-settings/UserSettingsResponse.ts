import AccountType from '~/views/user-settings/AccountType';

interface UserSettingsResponse {
  username: string;
  email: string;
  description: string;
  creationDate: string;
  accountType: AccountType;
}

export default UserSettingsResponse;
