interface UserSettingsRequest {
  oldPassword?: string | null;
  newPassword?: string | null;
  newPasswordRepeat?: string | null;
  email?: string | null;
  description?: string;
}

export default UserSettingsRequest;
